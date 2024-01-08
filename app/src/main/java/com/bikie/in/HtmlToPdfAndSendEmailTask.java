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

public class HtmlToPdfAndSendEmailTask extends AsyncTask<String, Void, File> {

    private Context context;
    private String toEmail;
    private String filename; // New parameter for the filename

    public HtmlToPdfAndSendEmailTask(Context context, String toEmail, String filename) {
        this.context = context;
        this.toEmail = toEmail;
        this.filename = filename;
    }

    @Override
    protected File doInBackground(String... params) {
        if (params.length < 1) {
            return null;
        }

        String htmlContent = params[0];
        String pdfFileName = (filename != null && !filename.isEmpty()) ? filename : "BikieInvoice.pdf";

        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), pdfFileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(pdfFile);

            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);

            outputStream.close();

            return pdfFile;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(File pdfFile) {
        super.onPostExecute(pdfFile);

        if (pdfFile != null) {
            Toast.makeText(context, "PDF generated successfully", Toast.LENGTH_SHORT).show();
            // Log the location of the generated PDF file
            String pdfFilePath = pdfFile.getAbsolutePath();
            Log.d("PDF Location", pdfFilePath);

            // Now send email with the generated PDF as an attachment
            sendEmail(pdfFile);
        } else {
            Toast.makeText(context, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(File attachment) {
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
            message.setText("Please find the attached PDF.");

            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please find the attached PDF.");

            // Create the attachment body part
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(attachment);

            // Create the Multipart object and add parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
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
    }
}
