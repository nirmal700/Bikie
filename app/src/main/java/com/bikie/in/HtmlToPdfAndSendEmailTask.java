package com.bikie.in;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.io.ByteArrayOutputStream;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class HtmlToPdfAndSendEmailTask extends AsyncTask<String, Void, ByteArrayOutputStream> {

    private Context context;
    private String toEmail;
    private String htmlContent;

    public HtmlToPdfAndSendEmailTask(Context context, String toEmail) {
        this.context = context;
        this.toEmail = toEmail;
    }

    @Override
    protected ByteArrayOutputStream doInBackground(String... params) {
        if (params.length < 1) {
            return null;
        }

        htmlContent = params[0];

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);

            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ByteArrayOutputStream outputStream) {
        super.onPostExecute(outputStream);

        Log.e("Output Stream", "onPostExecute: "+outputStream );
        if (outputStream != null) {
            Toast.makeText(context, "PDF generated successfully", Toast.LENGTH_SHORT).show();
            // Now send email with the generated PDF as an attachment
            sendEmail(outputStream);
        } else {
            Toast.makeText(context, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(ByteArrayOutputStream attachment) {
        new Thread(() -> {
            final String username = BuildConfig.MAIL_ID;
            final String password = BuildConfig.MAIL_PASSWORD;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            return new javax.mail.PasswordAuthentication(username, password);
                        }
                    });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject("PDF Attachment");


                // Create the message body part for HTML content
                MimeBodyPart htmlBodyPart = new MimeBodyPart();
                htmlBodyPart.setContent(htmlContent, "text/html");


                // Create the attachment body part
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.setFileName("BikieInvoice.pdf");
                attachmentBodyPart.setContent(attachment.toByteArray(), "application/pdf");

                // Create the Multipart object and add parts
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(htmlBodyPart);
                multipart.addBodyPart(attachmentBodyPart);

                // Set the content of the message to the multipart object
                message.setContent(multipart);

                // Send the message
                Transport.send(message);

                Log.d("Email", "Email sent successfully");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}