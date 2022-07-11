package ca.uvic.ecg.resource;

import ca.uvic.ecg.model.*;
import ca.uvic.ecg.repository.*;
import ca.uvic.ecg.webModel.ErrorInfo;
import ca.uvic.ecg.webModel.RestModel;
import com.mkingzhu.dataprotocol.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("/v1/{clinic}")
public class PhoneResource {
    @Autowired
    DevicesRepository deviRepo;
    @Autowired
    VerificationCode2Repository vc2Repo;
    @Autowired
    ClinicRepository clinicRepo;
    @Autowired
    PhoneRepository phoneRepo;
    @Autowired
    EcgTestRepository ecgTestRepo;
    @Autowired
    AppointmentRecordRepository appointmentRecordRepository;

    private ErrorInfo errorInfo;
    private HttpStatus http;
    private String resultMsg = "Phone’s information has successfully added in the database";
    private HttpHeaders responeseHeader = new HttpHeaders();

    //Get info of all the nurses  (Tested)
    @PostMapping("/phones")
    public ResponseEntity<RestModel> connectPhone(@PathVariable("clinic") String clinicName,
                                                  @RequestBody Device deviceMac) {
        Entity<resultJson> entity = new Entity<>();

        String VerifyCode = generateVerifyCode();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (deviceMac.getDeviceMacAddress().isEmpty()) {
                    errorInfo = ErrorInfo.NoData;
                    http = HttpStatus.FORBIDDEN;
                    break;
                }
                String macAddress = deviceMac.getDeviceMacAddress();
                if (deviRepo.findByDeviceMacAddressAndClinicIdAndDeletedFalse(macAddress,clinicId) == null) {
                    errorInfo = ErrorInfo.NoData;
                    http = HttpStatus.FORBIDDEN;
                    break;
                }
                VerifyCode = VerifyExistCode(VerifyCode, macAddress,clinicId);
                resultJson result = new resultJson(VerifyCode);
                entity.setModel(result);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
                responeseHeader.set("verificationCode", VerifyCode);
            } catch (Exception e) {
                errorInfo = ErrorInfo.UNKNOWN;
                http = HttpStatus.BAD_REQUEST;
                return new ResponseEntity<>(new RestModel<>(entity, errorInfo), http);
            }
        } while (false);
        if(http != HttpStatus.OK){
            return new ResponseEntity<>(new RestModel<>(entity, errorInfo), http);
        }else{
            return ResponseEntity.ok().headers(responeseHeader).body(new RestModel<>(entity, errorInfo));
        }

    }

    @PostMapping("/phones/information")
    public ResponseEntity<RestModel> addNewPhone(@PathVariable("clinic") String clinicName,
                                                 @RequestBody Phones Content,
                                                 @RequestHeader("verificationCode") String VerificationCode) {
        Entity<resultJson> entity = new Entity<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (!verifyVC(VerificationCode)) {
                    break;
                }
                if (phoneRepo.findByPhoneMacAndClinicIdAndDeletedFalse(Content.getPhoneMac(),clinicId) != null) {
                    errorInfo = ErrorInfo.DUPLICATE_Phone;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                Content.setDeleted(false);
                Content.setClinicId(clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId());
                phoneRepo.save(Content);
                VerificationCode2 UpdatedVerificationCode = vc2Repo.findByContent(VerificationCode);
                UpdatedVerificationCode.setPhoneId(Content.getPhoneId());
                vc2Repo.save(UpdatedVerificationCode);
                resultJson result = new resultJson(resultMsg);
                entity.setModel(result);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UNKNOWN;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), http);
    }

    @PutMapping("/phones/return-status")
    public ResponseEntity<RestModel> returnPhone(@PathVariable("clinic") String clinicName,
                                                 @RequestHeader("verificationCode") String VerificationCode) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        outterloop:
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (!verifyVC(VerificationCode)) {
                    break;
                }
                VerificationCode2 vc2 = vc2Repo.findByContent(VerificationCode);
                Device updateDevice = deviRepo.findByDeviceIdAndClinicIdAndDeletedFalse(vc2.getDeviceId(),clinicId);
                if(updateDevice==null){
                    errorInfo = ErrorInfo.InvalidDeviceId;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                int dId = updateDevice.getDeviceId();
                List<AppointmentRecord> appointmentRecords = appointmentRecordRepository.findByDeviceIdAndAppointmentStartTimeBeforeAndDeviceActualReturnTimeIsNull(dId,nowTime);
                if(appointmentRecords.isEmpty()){
                    errorInfo = ErrorInfo.Has_Returned;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                for(AppointmentRecord appointmentRecord:appointmentRecords){
                    EcgTest ecgTest = ecgTestRepo.findByEcgTestIdAndDeletedFalse(appointmentRecord.getEcgTestId());
                    if(ecgTest==null){
                        errorInfo = ErrorInfo.No_EcgTest;
                        http = HttpStatus.BAD_REQUEST;
                        continue ;
                    }
                    if(ecgTest.getStatus()!= StatusTypes.StatusType.TERMINATED){

                        errorInfo = ErrorInfo.Is_Running;
                        http = HttpStatus.BAD_REQUEST;
                        break outterloop;
                    }
                    appointmentRecord.setDeviceActualReturnTime(nowTime);
                    appointmentRecordRepository.save(appointmentRecord);
                }

                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UNKNOWN;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(errorInfo), http);
    }

    @PostMapping("/phones/comment")
    public ResponseEntity<RestModel> addComment(@PathVariable("clinic") String clinicName,
                                                 @RequestBody EcgTest comment,
                                                 @RequestHeader("verificationCode") String VerificationCode) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (!verifyVC(VerificationCode)) {
                    break;
                }
                int deviceId = vc2Repo.findByContent(VerificationCode).getDeviceId();
                AppointmentRecord appointmentRecord = appointmentRecordRepository.
                        findByPickupDateBeforeAndDeviceReturnDateAfterAndDeviceIdAndClinicIdAndDeletedFalse(nowTime,nowTime,deviceId,clinicId);
                if(appointmentRecord==null){
                    errorInfo = ErrorInfo.No_Appointment;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                EcgTest toBeUpdate = ecgTestRepo.findByEcgTestIdAndDeletedFalse(appointmentRecord.getEcgTestId());
                if (toBeUpdate == null) {
                    errorInfo = ErrorInfo.No_EcgTest;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                toBeUpdate.updateComment(comment.getComment());
                ecgTestRepo.save(toBeUpdate);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UNKNOWN;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(errorInfo), http);
    }










    public String generateVerifyCode() {
        StringBuffer strbuffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            strbuffer.append(PublicResource.CHARS[PublicResource.random.nextInt(PublicResource.CHARS.length)]);
        }
        return strbuffer.toString();
    }

    public String VerifyExistCode(String verifyCode, String deviceMacAddress,Integer clinicId) {
        if (vc2Repo.findByContent(verifyCode) == null) {
            VerificationCode2 newCode = new VerificationCode2();
            newCode.setContent(verifyCode);
            newCode.setExpired_time(LocalDateTime.now().plusMinutes(30));
            newCode.setDeviceId(deviRepo.findByDeviceMacAddressAndClinicIdAndDeletedFalse(deviceMacAddress,clinicId).getDeviceId());
            vc2Repo.save(newCode);
            return verifyCode;
        } else {
            if (vc2Repo.findByContent(verifyCode).getExpired_time().isAfter(LocalDateTime.now())) {
                verifyCode = generateVerifyCode();
                return VerifyExistCode(verifyCode, deviceMacAddress,clinicId);
            }
            VerificationCode2 updatedCode = vc2Repo.findByContent(verifyCode);
            updatedCode.setExpired_time(LocalDateTime.now().plusMinutes(30));
            updatedCode.setDeviceId(deviRepo.findByDeviceMacAddressAndClinicIdAndDeletedFalse(deviceMacAddress,clinicId).getDeviceId());
            vc2Repo.save(updatedCode);
            return verifyCode;
        }
    }


    private boolean verifyVC(String VerificationCode) {
        if (vc2Repo.findByContent(VerificationCode) == null) {
            errorInfo = ErrorInfo.INVALID_VERIFYCODE;
            http = HttpStatus.BAD_REQUEST;
            return false;
        }
        if (vc2Repo.findByContent(VerificationCode).getExpired_time().isBefore(LocalDateTime.now())) {
            errorInfo = ErrorInfo.Expired;
            http = HttpStatus.BAD_REQUEST;
            return false;
        }
        return true;
    }
}
