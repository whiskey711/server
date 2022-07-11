package ca.uvic.ecg.resource;


import ca.uvic.ecg.model.PatientInfo;
import ca.uvic.ecg.model.vo.PatientVo;
import com.mkingzhu.dataprotocol.Entity;
import com.mkingzhu.dataprotocol.Feed;
import ca.uvic.ecg.webModel.RestModel;
import ca.uvic.ecg.webModel.ErrorInfo;
import ca.uvic.ecg.repository.ClinicRepository;
import ca.uvic.ecg.repository.PatientInfoRepository;
import ca.uvic.ecg.repository.RemarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/v1/{clinic}/patient")
public class PatientsResource {
    @Autowired
    RemarksRepository remarkRepo;
    @Autowired
    PatientInfoRepository pIRepo;
    @Autowired
    ClinicRepository clinicRepo;

    private ErrorInfo errorInfo;
    private HttpStatus httpcode;
    private LocalDate nullDate = LocalDate.of(2447, 1, 1);

    //Get the remarks (Tested)
    @GetMapping("/{patient-id}/remark")
    public ResponseEntity<RestModel> getRemarks(
            @PathVariable("clinic") String clinicName,
            @PathVariable("patient-id") Integer pid) {
        Entity<String> entity = new Entity<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (pIRepo.findByPatientIdAndClinicIdAndDeletedFalse(pid,clinicId).getRemark() == null) {
                    errorInfo = ErrorInfo.NoData;
                    httpcode = HttpStatus.OK;
                    break;
                }
                String patientReamrk = pIRepo.findByPatientIdAndClinicIdAndDeletedFalse(pid,clinicId).getRemark();
                entity.setModel(patientReamrk);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpcode);
    }


    //Todo: Test
    @GetMapping("/information")
    public ResponseEntity<RestModel> getPatientInfo(@PathVariable("clinic") String clinicName,
                                                    @RequestParam("lastname") String lastname,
                                                    @RequestParam("firstname") String firstname,
                                                    @RequestParam(value = "birthday",required = false) Date birthdate,
                                                    @RequestParam("phn") String phn) {
        Feed<PatientInfo> feed = new Feed<>();
        List<Entity<PatientInfo>> entities = new LinkedList<>();
        List<PatientInfo> patients = pIRepo.findAll();
        //List<PatientInfo> patients = pIRepo.findByPatientLastNameAndClinicId(lastname);
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                LocalDate birthday = nullDate;
                if(!(birthdate==null)){
                    birthday = birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                //1000
                if (!lastname.isEmpty() && birthday.isEqual(nullDate) && phn.isEmpty() && firstname.isEmpty()) {
                    if (pIRepo.findByPatientLastNameAndClinicIdAndDeletedFalse(lastname,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientLastNameAndClinicIdAndDeletedFalse(lastname,clinicId);

                }
                //0100
                if (lastname.isEmpty() && !birthday.isEqual(nullDate) && phn.isEmpty() && firstname.isEmpty()) {
                    if (pIRepo.findByBirthdateAndClinicIdAndDeletedFalse(birthday,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByBirthdateAndClinicIdAndDeletedFalse(birthday,clinicId);
                }
                //0010
                if (lastname.isEmpty() && birthday.isEqual(nullDate) && !phn.isEmpty() && firstname.isEmpty()) {
                    if (pIRepo.findByPhnAndClinicIdAndDeletedFalse(phn,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPhnAndClinicIdAndDeletedFalse(phn,clinicId);
                }
                //0001
                if (lastname.isEmpty() && birthday.isEqual(nullDate) && phn.isEmpty() && !firstname.isEmpty()) {
                    if (pIRepo.findByPatientFirstNameAndClinicIdAndDeletedFalse(firstname,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientFirstNameAndClinicIdAndDeletedFalse(firstname,clinicId);
                }
                //0011
                if (lastname.isEmpty() && birthday.isEqual(nullDate) && !phn.isEmpty() && !firstname.isEmpty()) {
                    if (pIRepo.findByPatientFirstNameAndPhnAndClinicIdAndDeletedFalse(firstname, phn,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientFirstNameAndPhnAndClinicIdAndDeletedFalse(firstname, phn,clinicId);
                }
                //0101
                if (lastname.isEmpty() && !birthday.isEqual(nullDate) && phn.isEmpty() && !firstname.isEmpty()) {
                    if (pIRepo.findByPatientFirstNameAndBirthdateAndClinicIdAndDeletedFalse(firstname, birthday,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientFirstNameAndBirthdateAndClinicIdAndDeletedFalse(firstname, birthday,clinicId);
                }
                //0111
                if (lastname.isEmpty() && !birthday.isEqual(nullDate) && !phn.isEmpty() && !firstname.isEmpty()) {
                    if (pIRepo.findByPatientFirstNameAndBirthdateAndPhnAndClinicIdAndDeletedFalse(firstname, birthday, phn,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientFirstNameAndBirthdateAndPhnAndClinicIdAndDeletedFalse(firstname, birthday, phn,clinicId);
                }
                //1011
                if (!lastname.isEmpty() && birthday.isEqual(nullDate) && !phn.isEmpty() && !firstname.isEmpty()) {
                    if (pIRepo.findByPatientFirstNameAndBirthdateAndPatientLastNameAndClinicIdAndDeletedFalse(firstname, birthday, lastname,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientFirstNameAndBirthdateAndPatientLastNameAndClinicIdAndDeletedFalse(firstname, birthday, lastname,clinicId);
                }
                //1100
                if (!lastname.isEmpty() && !birthday.isEqual(nullDate) && phn.isEmpty() && firstname.isEmpty()) {
                    if (pIRepo.findByPatientLastNameAndBirthdateAndClinicIdAndDeletedFalse(lastname, birthday,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientLastNameAndBirthdateAndClinicIdAndDeletedFalse(lastname, birthday,clinicId);
                }
                //1101
                if (!lastname.isEmpty() && !birthday.isEqual(nullDate) && phn.isEmpty() && !firstname.isEmpty()) {
                    if (pIRepo.findByPatientLastNameAndBirthdateAndPatientFirstNameAndClinicIdAndDeletedFalse(lastname, birthday, firstname,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientLastNameAndBirthdateAndPatientFirstNameAndClinicIdAndDeletedFalse(lastname, birthday, firstname,clinicId);
                }
                //1010
                if (!lastname.isEmpty() && birthday.isEqual(nullDate) && !phn.isEmpty() && firstname.isEmpty()) {
                    if (pIRepo.findByPatientLastNameAndPhnAndClinicIdAndDeletedFalse(lastname, phn,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientLastNameAndPhnAndClinicIdAndDeletedFalse(lastname, phn,clinicId);
                }
                //1001
                if (!lastname.isEmpty() && birthday.isEqual(nullDate) && phn.isEmpty() && !firstname.isEmpty()) {
                    if (pIRepo.findByPatientLastNameAndPatientFirstNameAndClinicIdAndDeletedFalse(lastname, firstname,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientLastNameAndPatientFirstNameAndClinicIdAndDeletedFalse(lastname, firstname,clinicId);
                }
                //0110
                if (lastname.isEmpty() && !birthday.isEqual(nullDate) && !phn.isEmpty() && firstname.isEmpty()) {
                    if (pIRepo.findByBirthdateAndPhnAndClinicIdAndDeletedFalse(birthday, phn,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByBirthdateAndPhnAndClinicIdAndDeletedFalse(birthday, phn,clinicId);
                }
                //1110
                if (!lastname.isEmpty() && !birthday.isEqual(nullDate) && !phn.isEmpty() && firstname.isEmpty()) {
                    if (pIRepo.findByPatientLastNameAndBirthdateAndPhnAndClinicIdAndDeletedFalse(lastname, birthday, phn,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientLastNameAndBirthdateAndPhnAndClinicIdAndDeletedFalse(lastname, birthday, phn,clinicId);
                }
                //1111
                if (!lastname.isEmpty() && !birthday.isEqual(nullDate) && !phn.isEmpty() && !firstname.isEmpty()) {
                    if (pIRepo.findByPatientLastNameAndBirthdateAndPhnAndPatientFirstNameAndClinicIdAndDeletedFalse(lastname, birthday, phn, firstname,clinicId).isEmpty()) {
                        errorInfo = ErrorInfo.NoData;
                        httpcode = HttpStatus.NO_CONTENT;
                        break;
                    }
                    patients = pIRepo.findByPatientLastNameAndBirthdateAndPhnAndPatientFirstNameAndClinicIdAndDeletedFalse(lastname, birthday, phn, firstname,clinicId);
                }

                for (PatientInfo patient : patients) {
                    Entity<PatientInfo> entity = new Entity<>();
                    entity.setModel(patient);
                    entities.add(entity);
                }
                feed.setEntities(entities);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(feed, errorInfo), httpcode);
    }


    @PostMapping("/information")
    public ResponseEntity<RestModel> createPatient(@PathVariable("clinic") String clinicName,
                                                   @RequestBody PatientVo newPatient) {
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (!pIRepo.findByPatientLastNameAndBirthdateAndPhnAndClinicIdAndDeletedFalse(
                        newPatient.getPatientLastName(),
                        newPatient.getBirthdate(),
                        newPatient.getPhn(),
                        clinicId).isEmpty()) {
                    errorInfo = ErrorInfo.DUPLICATE_USER;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(newPatient.getPhoneNumber()==null){
                    errorInfo = ErrorInfo.MissingInput;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(newPatient.getEmail()==null){
                    errorInfo = ErrorInfo.MissingInput;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(newPatient.getPatientFirstName()==null){
                    errorInfo = ErrorInfo.MissingInput;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(newPatient.getPatientLastName()==null){
                    errorInfo = ErrorInfo.MissingInput;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(newPatient.getCity()==null){
                    errorInfo = ErrorInfo.MissingInput;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(newPatient.getProvince()==null){
                    errorInfo = ErrorInfo.MissingInput;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(newPatient.getBirthdate()==null){
                    errorInfo = ErrorInfo.MissingInput;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(newPatient.getPhn()==null){
                    errorInfo = ErrorInfo.MissingInput;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(newPatient.getGender()==null){
                    errorInfo = ErrorInfo.MissingInput;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                PatientInfo patientInfo = newPatient.createNewPatientInfo();
                patientInfo.setDeleted(false);
                patientInfo.setClinicId(clinicId);
                pIRepo.save(patientInfo);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(errorInfo), httpcode);
    }

    @PutMapping("/information/{patient-id}")
    public ResponseEntity<RestModel> updatePatient(@PathVariable("clinic") String clinicName,
                                                   @PathVariable("patient-id") int pId,//Todo: pid is not necessary
                                                   @RequestBody PatientVo updatedPatient) {
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (pIRepo.findByPatientIdAndClinicIdAndDeletedFalse(pId,clinicId) == null) {
                    errorInfo = ErrorInfo.NoData;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                PatientInfo patientInfo = pIRepo.getOne(pId);
                patientInfo.update(updatedPatient);
                pIRepo.save(patientInfo);
                errorInfo = ErrorInfo.UpdatedSuccess;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(errorInfo), httpcode);
    }

    @GetMapping("/information/{patient-id}")
    public ResponseEntity<RestModel> getOnePatient(@PathVariable("clinic") String clinicName,
                                                   @PathVariable("patient-id") int pId) {
        Entity<PatientInfo> entity = new Entity<>();
        do {
            try {

                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                PatientInfo patient = pIRepo.findByPatientIdAndClinicIdAndDeletedFalse(pId,clinicId);
                if ( patient == null) {
                    errorInfo = ErrorInfo.NoData;
                    httpcode = HttpStatus.NO_CONTENT;
                    break;
                }
                entity.setModel(patient);
                errorInfo = ErrorInfo.UpdatedSuccess;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpcode);
    }
}
