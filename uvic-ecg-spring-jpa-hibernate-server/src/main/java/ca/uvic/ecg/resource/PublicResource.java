package ca.uvic.ecg.resource;


import ca.uvic.ecg.model.Clinics;
import ca.uvic.ecg.model.Nurse;
import ca.uvic.ecg.model.VerificationCode;
import ca.uvic.ecg.model.resultJson;
import ca.uvic.ecg.model.vo.NurseVo;
import com.mkingzhu.dataprotocol.Entity;
import com.mkingzhu.dataprotocol.Feed;
import ca.uvic.ecg.webModel.RestModel;
import ca.uvic.ecg.webModel.ErrorInfo;
import ca.uvic.ecg.repository.ClinicRepository;
import ca.uvic.ecg.repository.NurseRepository;
import ca.uvic.ecg.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/v1/public")
public class PublicResource {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    NurseRepository nurseRepository;

    @Autowired
    VerificationCodeRepository vcRepo;
    @Autowired
    NurseResource nurseResource;
    @Autowired
    ClinicRepository clinicRepo;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public Integer LENGTH_REQUIREMENT = 10;
    static Random random = new Random();
    //Default view of Unauthorized user

    private ErrorInfo errorInfo;
    private HttpStatus httpCode;

    @GetMapping("/unauthorized")
    public ResponseEntity<RestModel> unauthorized() {
        errorInfo = ErrorInfo.UNAUTHORIZED;
        httpCode = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(new RestModel<>(errorInfo), httpCode);
    }

    //This method is to verify the verification code to reset the password
    @PostMapping("/verify")
    public ResponseEntity<RestModel> verification(@RequestBody String vCode) {
        Entity<Nurse> entity = new Entity<>();
        do {
            if (vcRepo.findByContent(vCode) == null) {
                errorInfo = ErrorInfo.WrongCode;
                httpCode = HttpStatus.BAD_REQUEST;
                break;
            }
            VerificationCode verifiedCode = vcRepo.findByContent(vCode);
            if (verifiedCode.getExpired_time().isBefore(LocalDateTime.now()) || !verifiedCode.getStatus()) {
                errorInfo = ErrorInfo.Expired;
                httpCode = HttpStatus.BAD_REQUEST;
                break;
            }
            verifiedCode.setStatus(false);
            vcRepo.save(verifiedCode);
            Nurse nurse = nurseRepository.findByNurseEmailAndDeletedFalse(verifiedCode.getEmail());
            //resultJson result = new resultJson ("Verified successful");
            entity.setModel(nurse);
            errorInfo = ErrorInfo.OK;
            httpCode = HttpStatus.OK;
        } while (false);

        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpCode);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<RestModel> forgetPassword(@RequestBody NurseVo nurseVo) {
        SimpleMailMessage message = new SimpleMailMessage();
        Entity<resultJson> entity = new Entity<>();
        String VerifyCode = generateVerifyCode();
        do {
            try {
                String email = nurseVo.getNurseEmail();
                if(email==null){
                    errorInfo = ErrorInfo.NoEmail;
                    httpCode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (nurseRepository.findByNurseEmailAndDeletedFalse(email) == null) {
                    errorInfo = ErrorInfo.NoEmail;
                    httpCode = HttpStatus.BAD_REQUEST;
                    break;
                }
                VerifyCode = VerifyExistCode(VerifyCode, email);
                message.setFrom("ArbutusHolsterEcg");
                message.setTo(email);
                message.setSubject("Reset password for user of " + email + " .");
                message.setText("Your verification code is " + VerifyCode + " if you are not requesting reset the password for " +
                        email + " , please ignore " + "this email if so, please click the link below and " +
                        "follow the instructions");
                mailSender.send(message);
                resultJson result = new resultJson(VerifyCode);
                entity.setModel(result);
                errorInfo = ErrorInfo.OK;
                httpCode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UNKNOWN;
                httpCode = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpCode);
    }

    //Update nurse info with input of one Nurse (Tested)
    @PutMapping("/password-management")
    public ResponseEntity<RestModel> resetPassword(@RequestBody Nurse user) {

        do {
            try {
                if (nurseRepository.findByNurseEmailAndDeletedFalse(user.getNurseEmail()) == null) {
                    errorInfo = ErrorInfo.NoData;
                    httpCode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(user.getPassword().length()<LENGTH_REQUIREMENT){
                    errorInfo = ErrorInfo.Short_Pswd;
                    httpCode = HttpStatus.BAD_REQUEST;
                    break;
                }
                Nurse nurse = nurseRepository.findByNurseEmailAndDeletedFalse(user.getNurseEmail());
                nurse.setNextRequest(null);
                nurse.setNumberOfFailures(0);
                String rawPaswd = user.getPassword();
                nurse.setPassword(bCryptPasswordEncoder.encode(rawPaswd));
                nurseRepository.save(nurse);
                //nurseRepository.changePassword(user.getNurseEmail(),user.getPassword());
                httpCode = HttpStatus.OK;
                errorInfo = ErrorInfo.UpdatedSuccess;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UNKNOWN;
                httpCode = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(errorInfo), httpCode);
    }

    @GetMapping("/clinics")
    public ResponseEntity<RestModel> getAllClinics() {
        Feed<Clinics> feed = new Feed<>();
        do {
            try {
                List<Entity<Clinics>> entities = new LinkedList<>();
                List<Clinics> clinics = clinicRepo.findAll();
                for (Clinics clinic : clinics) {
                    Entity<Clinics> entity = new Entity<>();
                    entity.setModel(clinic);
                    entities.add(entity);
                }
                feed.setEntities(entities);
                errorInfo = ErrorInfo.OK;
                httpCode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UNKNOWN;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(feed, errorInfo), httpCode);
    }

    @PostMapping("/registerEmail")
    public ResponseEntity<RestModel> registerEmail(@RequestBody NurseVo nurseVo) {
        SimpleMailMessage message = new SimpleMailMessage();
        Entity<resultJson> entity = new Entity<>();
        String VerifyCode = generateVerifyCode();
        do {
            try {
                String email = nurseVo.getNurseEmail();
                if(email==null){
                    errorInfo = ErrorInfo.NoEmail;
                    httpCode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (nurseRepository.findByNurseEmailAndDeletedFalse(email) != null) {
                    errorInfo = ErrorInfo.DUPLICATE_USER;
                    httpCode = HttpStatus.BAD_REQUEST;
                    break;
                }
                VerifyCode = VerifyExistCode(VerifyCode, email);
                message.setFrom("ArbutusHolsterEcg");
                message.setTo(email);
                message.setSubject("Register new acoount of Ecg");
                message.setText("Your verification code is " + VerifyCode + " if you are not requesting register new " +
                        "account for " + email + " , please ignore this email. if so, please follow the instructions");
                mailSender.send(message);
                resultJson result = new resultJson(VerifyCode);
                entity.setModel(result);
                errorInfo = ErrorInfo.OK;
                httpCode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UNKNOWN;
                httpCode = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpCode);
    }


    public String generateVerifyCode() {
        StringBuffer strbuffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            strbuffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return strbuffer.toString();
    }

    public String VerifyExistCode(String verifyCode, String email) {
        if (vcRepo.findByContent(verifyCode) == null) {
            VerificationCode newCode = new VerificationCode();
            newCode.setContent(verifyCode);
            newCode.setExpired_time(LocalDateTime.now().plusMinutes(5));
            newCode.setStatus(true);
            newCode.setEmail(email);
            vcRepo.save(newCode);
            return verifyCode;
        } else {
            if (vcRepo.findByContent(verifyCode).getStatus()) {
                verifyCode = generateVerifyCode();
                return VerifyExistCode(verifyCode, email);
            }
            VerificationCode updatedCode = vcRepo.findByContent(verifyCode);
            updatedCode.setExpired_time(LocalDateTime.now().plusMinutes(5));
            updatedCode.setStatus(true);
            updatedCode.setEmail(email);
            vcRepo.save(updatedCode);
            return verifyCode;
        }
    }
}
