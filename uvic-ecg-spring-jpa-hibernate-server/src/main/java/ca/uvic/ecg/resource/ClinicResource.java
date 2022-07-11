package ca.uvic.ecg.resource;

import ca.uvic.ecg.model.Clinics;
import ca.uvic.ecg.model.resultJson;
import ca.uvic.ecg.model.vo.AppointMail;
import com.mkingzhu.dataprotocol.Entity;
import ca.uvic.ecg.webModel.RestModel;
import ca.uvic.ecg.webModel.ErrorInfo;
import ca.uvic.ecg.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/{clinic}")
public class ClinicResource {
    @Autowired
    ClinicRepository clinicRepo;
    @Autowired
    private JavaMailSender mailSender;

    private ErrorInfo errorInfo;
    private HttpStatus http;

    @GetMapping("/mail-template")
    public ResponseEntity<RestModel> getMailTemplate(@PathVariable("clinic") String clinicName) {
        Entity<resultJson> entity = new Entity<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                Clinics clinic = clinicRepo.findByClinicNameAndDeletedFalse(clinicName);
                if (clinic.getMailTemplate() == null) {
                    errorInfo = ErrorInfo.NoTemplate;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                resultJson result = new resultJson(clinic.getMailTemplate());
                entity.setModel(result);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), http);
    }

    @PatchMapping("mail-template")
    @Transactional
    public ResponseEntity<RestModel> createMailTemplate(@PathVariable("clinic") String clinicName, @RequestBody Clinics input) {
        do {
            try {
                if (clinicRepo.findByClinicNameAndDeletedFalse(clinicName) == null) {
                    errorInfo = ErrorInfo.WrongClinicName;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (input.getMailTemplate() == null) {
                    errorInfo = ErrorInfo.NoTemplate;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                String template = input.getMailTemplate();
                Clinics clinics = clinicRepo.findByClinicNameAndDeletedFalse(clinicName);
                clinics.setMailTemplate(template);
                clinicRepo.save(clinics);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(errorInfo), http);
    }

    @PostMapping("appoint-mail")
    public ResponseEntity<RestModel> sendMail(@PathVariable("clinic") String clinicName,
                                              @RequestBody AppointMail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        Entity<resultJson> entity = new Entity<>();
        do {
            try {
                message.setTo(mail.getPatientEmail());
                message.setSubject("Appointment notification email");
                message.setText(mail.getMailContent());
                mailSender.send(message);
                resultJson result = new resultJson("Success");
                entity.setModel((result));
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UNKNOWN;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), http);
    }
}
