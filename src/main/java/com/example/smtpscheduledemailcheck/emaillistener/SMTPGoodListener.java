package com.example.smtpscheduledemailcheck.emaillistener;

import com.example.smtpscheduledemailcheck.emaillistener.dto.EmailContent;
import com.example.smtpscheduledemailcheck.emaillistener.dto.file.FileHolder;
import com.example.smtpscheduledemailcheck.emaillistener.dto.file.SupportedFile;
import com.example.smtpscheduledemailcheck.emaillistener.util.EmailAttachmentNameUtil;
import com.example.smtpscheduledemailcheck.emaillistener.util.NullSafetyProvider;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.angus.mail.imap.IMAPFolder;
import org.eclipse.angus.mail.util.BASE64DecoderStream;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class SMTPGoodListener {

    public void check(String host, Integer port, String mailStoreType, String username, String password) throws MessagingException, IOException {
        Map<String, List<Message>> moveToMap = new HashMap<>();

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.host", host);
        props.setProperty("mail.imaps.port", port.toString());

        Session session = Session.getInstance(props);
        //session.setDebug(true); // Enable debug mode for troubleshooting

        Store store = session.getStore(mailStoreType);
        store.connect(username, password);

        IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
        Message[] messages = inbox.getMessages();

        for (Message message : messages) {
            updateMoveToMap(message, moveToMap);
            processMessages(message);
        }

        moveMessages(moveToMap, store, inbox);

        inbox.close();
        store.close();
    }

    private void moveMessages(Map<String, List<Message>> moveToMap, Store store, IMAPFolder inbox) {
        moveToMap.forEach((key, value) -> {
            try {
                Folder folder = store.getFolder(key);
                if (!folder.exists()) {
                    if (folder.create(Folder.HOLDS_MESSAGES)) {
                        folder.setSubscribed(true);
                    }
                }
                inbox.moveMessages(value.toArray(new Message[0]), folder);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });

    }

    //ToDo: This code assumes there will be only one to address but that mimght not be the case.
    //Add check and filter the address for current account being used to listen to emails and find out the correct to address
    private void updateMoveToMap(Message message, Map<String, List<Message>> moveToMap) throws MessagingException {
        List<String> to = NullSafetyProvider.arrayToList(message.getRecipients(Message.RecipientType.TO)).stream().map(Address::toString).toList();
        String client = to.get(0).split("@")[0].split("\\+")[1];

        if(moveToMap.containsKey(client)) {
            moveToMap.get(client).add(message);
        }
        else {
            moveToMap.put(client, Arrays.asList(message));
        }
    }

    private void processMessages(Message msg) throws MessagingException, IOException {
            EmailContent email = EmailContent.builder()
                .size(msg.getSize())
                .messageNumber(msg.getMessageNumber())
                .folder(msg.getFolder().getFullName())
                .flags(msg.getFlags().toString())
                .sentDate(msg.getSentDate())
                .receivedDate(msg.getReceivedDate())
                .from(NullSafetyProvider.arrayToList(msg.getFrom()).stream().map(Address::toString).collect(Collectors.toList()))
                .to(NullSafetyProvider.arrayToList(msg.getRecipients(Message.RecipientType.TO)).stream().map(Address::toString).collect(Collectors.toList()))
                .cc(NullSafetyProvider.arrayToList(msg.getRecipients(Message.RecipientType.CC)).stream().map(Address::toString).collect(Collectors.toList()))
                .bcc(NullSafetyProvider.arrayToList(msg.getRecipients(Message.RecipientType.BCC)).stream().map(Address::toString).collect(Collectors.toList()))
                .subject(msg.getSubject())
                .attachmentList(new ArrayList<>())
                .build();

            if (msg.getContent() instanceof Multipart multipart) {
                fillContent((MimeMultipart) multipart, 0, multipart.getCount(), email);
            }
        log.info("----------------------------------Email already parsed------------------------------------");
    }

    /**
     * @param mimeMultipart
     * @throws MessagingException
     * @throws IOException
     */
    private void fillContent(MimeMultipart mimeMultipart, int nextItemIndex, int totalItems, EmailContent email) throws MessagingException, IOException {
        if (nextItemIndex == totalItems) {
            return;
        }
        BodyPart bodyPart = mimeMultipart.getBodyPart(nextItemIndex);
        Object o2 = bodyPart.getContent();
        if (o2 instanceof MimeMultipart) {
            fillContent((MimeMultipart) o2, 0, ((MimeMultipart) o2).getCount(), email);
        } else if (o2 instanceof BASE64DecoderStream) {
            //ToDo: Do something about this string below
            if (bodyPart.getContentType().toLowerCase().contains("application/pdf")) {
                FileHolder attachment =
                    new FileHolder(
                        EmailAttachmentNameUtil.getFileNameFromContentType(bodyPart.getContentType()),
                        SupportedFile.PDF_INVOICE,
                        ((BASE64DecoderStream) o2).readAllBytes()
                    );
                email.getAttachmentList().add(attachment);
            } else {
                log.info("Non PDF file attachment found in mail");
                log.info("From: " + email.getFrom());
                log.info("To: " + email.getTo());
                log.info("CC: " + email.getCc());
                log.info("BCC: " + email.getBcc());
                log.info("Date: " + email.getReceivedDate());
                log.info("Subject: " + email.getSubject());
            }
        } else if (o2 instanceof String) {
            if (bodyPart.getContentType().toLowerCase().contains("text/plain")) {
                email.setContent(o2.toString().trim());
            } else if (bodyPart.getContentType().toLowerCase().contains("text/html")) {
                email.setHtmlContent(o2.toString().trim());
            } else {
                log.error("Unidentified string type " + bodyPart.getContentType() + " found in mail.");
                log.info("From: " + email.getFrom());
                log.info("To: " + email.getTo());
                log.info("CC: " + email.getCc());
                log.info("BCC: " + email.getBcc());
                log.info("Date: " + email.getReceivedDate());
                log.info("Subject: " + email.getSubject());
                log.info("Content: " + o2);
            }
        } else {
            System.out.println("Something else found");
        }
        nextItemIndex += 1;
        fillContent(mimeMultipart, nextItemIndex, totalItems, email);
    }
}
