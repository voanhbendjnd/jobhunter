package vn.hoidanit.jobhunter.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.swing.Spring;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import vn.hoidanit.jobhunter.domain.Entity.Job;
import vn.hoidanit.jobhunter.repository.JobRepository;

@Service
public class EmailService {
    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final JobRepository jobRepository;

    public EmailService(MailSender mailSender, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine,
            JobRepository jobRepository) {
        this.mailSender = mailSender;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.jobRepository = jobRepository;
    }

    public void sendSimpleEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("benva.ce190709@gmail.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello");
        this.mailSender.send(msg);
    }

    // gửi email đồng bộ
    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        // prepare message using a spring helper
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            // mimeMessage đầu vào
            // isMultipart có thể là hình ảnh
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            helper.setTo(to); // tới ai
            helper.setSubject(subject); // tiêu đề
            helper.setText(content, isHtml); // nội dung
            this.javaMailSender.send(mimeMessage); // gọi để gửi email>>>
        } catch (MailException | MessagingException e) {
            System.out.println("Error!: " + e);
        }
    }

    public void sendEmailFromTemplateSync(String to, String subject, String templateName) {
        Context context = new Context();
        List<Job> job = this.jobRepository.findAll();
        String name = "Ben10";
        context.setVariable("jobs", job);
        context.setVariable("name", name);
        String content = this.templateEngine.process(templateName, context); // convert from html to String
        this.sendEmailSync(to, subject, content, false, true);
    }
}
