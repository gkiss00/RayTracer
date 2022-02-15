package server.email;

import jdk.jfr.Experimental;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.FileInputStream;
import java.util.Properties;

public class EmailSender {
    public static JavaMailSenderImpl getMailSender() {
        String pathToEmailSenderProperties = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "server.emailSender.properties";
        System.out.println(pathToEmailSenderProperties);

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(pathToEmailSenderProperties));
        } catch (Exception e) {

        }
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(properties.getProperty("host"));
        javaMailSenderImpl.setPort(Integer.parseInt(properties.getProperty("port")));
        javaMailSenderImpl.setUsername(properties.getProperty("user.name"));
        javaMailSenderImpl.setPassword(properties.getProperty("user.password"));

        Properties props = javaMailSenderImpl.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return javaMailSenderImpl;
    }
}
