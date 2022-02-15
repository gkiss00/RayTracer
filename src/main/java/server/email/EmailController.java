package server.email;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class EmailController {

    @GetMapping("/sendEmail")
    public String senEmail() {
        EmailServiceImp.sendEmail(
                "PereNoel@gmail.com",
                "gautierkiss.p1@gmail.com",
                "Test",
                "je t'aime"
        );
        return "Email sent successfully";
    }

    @GetMapping("/sendEmailWithAttachment")
    public String sendEmailWithAttachment() {
        EmailServiceImp.sendEmailWithAttachment(
                "gkgkgk395@gmail.com",
                "gautierkiss.p1@gmail.com",
                "Test",
                "je t'aime",
                "Image.png"
        );
        return "Email sent successfully";
    }
}
