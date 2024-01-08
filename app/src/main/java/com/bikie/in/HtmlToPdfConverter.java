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
import java.io.IOException;

public class HtmlToPdfConverter extends AsyncTask<String, Void, File> {

    private Context context;
    private String email;
    private String subject;
    private String message;

    public HtmlToPdfConverter(Context context,String email, String subject, String message) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected File doInBackground(String... params) {
        if (params.length < 2) {
            return null;
        }

        String htmlContent = params[0];
        String pdfFileName = params[1];

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
            String pdfFilePath = pdfFile.getAbsolutePath();
            Log.e("PDF Location", pdfFilePath);
            Toast.makeText(context, "PDF generated successfully", Toast.LENGTH_SHORT).show();
            SendMail sendMail = new SendMail(context,email,subject,message,pdfFilePath);
            sendMail.execute();
            // Now you can do something with the generated PDF file, like open it with a PDF viewer
        } else {
            Toast.makeText(context, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        }
    }
}