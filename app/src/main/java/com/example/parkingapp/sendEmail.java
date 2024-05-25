package com.example.parkingapp;

import android.os.AsyncTask;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class sendEmail {

    public void send(String email, String OTP) {
        new SendEmailTask().execute(email, OTP);
    }

    private class SendEmailTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String email = params[0];
            String OTP = params[1];

            String to = email;
            String from = "mcassignment01@gmail.com";
            String host = "smtp.gmail.com";

            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            properties.setProperty("mail.smtp.port", "587");
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.starttls.enable", "true");

            final String username = "mcassignment01@gmail.com";
            final String password = "akkp vadz ntyq fkmj";  //app password for the gmail

            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject("Your OTP Code");
                message.setText("Parking App. Your OTP code is: " + OTP);

                Transport.send(message);
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }

            return null;
        }
    }
}
