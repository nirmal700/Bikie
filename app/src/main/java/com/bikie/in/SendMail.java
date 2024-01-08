package com.bikie.in;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.bikie.in.Users.BookingHistoryUser;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail extends AsyncTask<Void, Void, Void> {
    private Context context;
    private Session session;
    private String email;
    private String subject;
    private String message;
    private String attachmentFilePath; // Add this variable for attachment
    private ProgressDialog progressDialog;


    public SendMail(Context context, String email, String subject, String message, String attachmentFilePath) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.attachmentFilePath = attachmentFilePath;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Use a Handler to run UI operations on the main thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // Initialize ProgressDialog
                progressDialog = new ProgressDialog(context);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);

            }
        });    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // Use a Handler to run UI operations on the main thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(BuildConfig.MAIL_ID, BuildConfig.MAIL_PASSWORD);
            }
        });
        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(BuildConfig.MAIL_ID));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(subject);

            // Create a multipart message to handle both text and attachment
            Multipart multipart = new MimeMultipart();

            // Attach HTML content
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Attach file if provided
            if (attachmentFilePath != null && !attachmentFilePath.isEmpty()) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentFilePath);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(attachmentFilePath);
                multipart.addBodyPart(attachmentBodyPart);
            }

            // Set the content of the message
            mm.setContent(multipart);

            // Send the message
            Transport.send(mm);
        } catch (MessagingException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
        return null;
    }
}