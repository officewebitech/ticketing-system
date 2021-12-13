package os.ticketingsystem.login.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import os.ticketingsystem.email.EmailSender;
import os.ticketingsystem.login.dto.UserDTO;
import os.ticketingsystem.login.mapper.UserMapper;
import os.ticketingsystem.login.repository.UserRepository;
import os.ticketingsystem.login.security.registrationToken.ConfirmationToken;
import os.ticketingsystem.login.security.registrationToken.ConfirmationTokenService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegisterService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ConfirmationTokenService tokenService;
    private final EmailSender emailSender;


    @Autowired
    public RegisterService(UserMapper userMapper, UserRepository userRepository,
                           ConfirmationTokenService tokenService, EmailSender emailSender) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailSender = emailSender;
    }


    public void registerUser(UserDTO userDTO) {
        userRepository.save(userMapper.registerUser(userDTO));

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                userRepository.findByEmail(userDTO.getEmail()).get()
        );

        tokenService.saveConfirmationToken(confirmationToken);

        emailSender.send(userDTO.getEmail(),
                buildEmail(userDTO.getFirstName(), token));

    }

    @Transactional
    public String confirmToken(String token) {

        String email = tokenService
                .getToken(token).get().getUser().getEmail();

        tokenService.setConfirmedAt(token);
        enableAppUser(email);

        return "Contul a fost confirmat cu succes !";
    }

    private int enableAppUser(String email) {
        return userRepository.enableUser(email);
    }

    private String buildEmail(String name, String token) {
        return "<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"en\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<meta charset=\"utf-8\"/>\n" +
                "<meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\"/>\n" +
                "<!--[if mso]><xml><o:OfficeDocumentSettings><o:PixelsPerInch>96</o:PixelsPerInch><o:AllowPNG/></o:OfficeDocumentSettings></xml><![endif]-->\n" +
                "<!--[if !mso]><!-->\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Cormorant+Garamond\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Lato\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Droid+Serif\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Fira+Sans\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Lora\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Quattrocento\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Permanent+Marker\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Oswald\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Montserrat\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Merriweather\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "<!--<![endif]-->\n" +
                "<style>\n" +
                "\t\t* {\n" +
                "\t\t\tbox-sizing: border-box;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\tbody {\n" +
                "\t\t\tmargin: 0;\n" +
                "\t\t\tpadding: 0;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\tth.column {\n" +
                "\t\t\tpadding: 0\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ta[x-apple-data-detectors] {\n" +
                "\t\t\tcolor: inherit !important;\n" +
                "\t\t\ttext-decoration: inherit !important;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t#MessageViewBody a {\n" +
                "\t\t\tcolor: inherit;\n" +
                "\t\t\ttext-decoration: none;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\tp {\n" +
                "\t\t\tline-height: inherit\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t@media (max-width:620px) {\n" +
                "\t\t\t.icons-inner {\n" +
                "\t\t\t\ttext-align: center;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.icons-inner td {\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.row-content {\n" +
                "\t\t\t\twidth: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.stack .column {\n" +
                "\t\t\t\twidth: 100%;\n" +
                "\t\t\t\tdisplay: block;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body style=\"background-color: #FFFFFF; margin: 0; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-2\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #f7f6f5;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #fff; color: #000000;\" width=\"600\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<th class=\"column\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 0px; padding-bottom: 0px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"100%\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"image_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "<tr>\n" +
                "<td style=\"width:100%;padding-right:0px;padding-left:0px;padding-bottom:35px;\">\n" +
                "<div align=\"center\" style=\"line-height:10px\">\n" +
                "\t<img src='https://files.webitech.ro/img/email.png' style='width:300px' />\n" +
                "</div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</th>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-3\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #f7f6f5;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #fff; color: #000000;\" width=\"600\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<th class=\"column\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 0px; padding-bottom: 0px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"100%\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\">\n" +
                "<tr>\n" +
                "<td style=\"padding-bottom:40px;padding-left:15px;padding-right:15px;padding-top:40px;\">\n" +
                "<div style=\"font-family: Tahoma, Verdana, sans-serif\">\n" +
                "<div style=\"font-size: 12px; font-family: 'Lato', Tahoma, Verdana, Segoe, sans-serif; mso-line-height-alt: 18px; color: #222222; line-height: 1.5;\">\n" +
                "<p style=\"margin: 0; font-size: 16px; text-align: center; mso-line-height-alt: 24px;\"><span style=\"font-size:16px;\"><strong>Salut " + name  + ",</strong></span></p>\n" +
                "<p style=\"margin: 0; font-size: 16px; text-align: center;\">Iti multumim pentru inregistrare. Va rugam sa activati contul in timp util.</p>\n" +
                "<p style=\"margin: 10px 0; font-size: 20px; font-weight: 600; text-align: center; mso-line-height-alt: 18px;\">  Cod de activare: <br> " + token + "</p>\n" +
                "<p style=\"margin: 0; font-size: 16px; text-align: center;\">Codul de activare expira in 30 de minute.</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</th>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</table><!-- End -->\n" +
                "</body>\n" +
                "</html>\n";
    }
}
