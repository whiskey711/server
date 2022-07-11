package ca.uvic.ecg.resource;

import ca.uvic.ecg.model.*;
import ca.uvic.ecg.model.vo.EcgTestVo;
import ca.uvic.ecg.repository.*;
import ca.uvic.ecg.webModel.ErrorInfo;
import ca.uvic.ecg.webModel.RestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkingzhu.dataprotocol.Entity;
import com.mkingzhu.dataprotocol.Feed;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/v1/{clinic}/patient")
public class TestandRawdataResource {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    PatientInfoRepository pIRepo;
    @Autowired
    EcgRawDataRepository ecgRawRepo;
    @Autowired
    EcgTestRepository ecgTestRepo;
    @Autowired
    EcgTestRepository ecgRepo;
    @Autowired
    VerificationCode2Repository vc2Repo;
    @Autowired
    ClinicRepository clinicRepo;
    @Autowired
    DbFilesStorageService dbFilesStorageService;
    @Autowired
    DevicesRepository devicesRepository;
    @Autowired
    AppointmentRecordRepository appointmentRecordRepository;
    @Autowired
    NurseRepository nurseRepository;

    private ErrorInfo errorInfo;
    private HttpStatus httpcode;
    private static final int periodToFindDataWhenRecording = 60;
    private static final int periodToFindDataWhenHookingUp = 5;
    private static final int hookupFreq = 5;
    private static final int reocrdFreq = 60;
    private static final int terFreq = 360;
    private static final int OKStatus = 1;
    private String dateFormat = "yyyy-MM-dd'T'HH:mm:ssz";

    //Get one Ecg Data of a patient by selecting the ecg-test-id
    @GetMapping("/{patient-id}/ecg-test/{ecg-test-id}")
    public ResponseEntity<RestModel> GetEcgTest(
            @PathVariable("clinic") String clinicName,
            @PathVariable("patient-id") int Pid, @PathVariable("ecg-test-id") int ETId) {
        Entity<EcgTest> entity = new Entity<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (pIRepo.findByPatientIdAndClinicIdAndDeletedFalse(Pid,clinicId) == null) {
                    errorInfo = ErrorInfo.NoPatient;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ETId,clinicId).getPatientId() != Pid) {
                    errorInfo = ErrorInfo.UnauthroziedPatient;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (ecgRepo.findByPatientIdAndClinicIdAndDeletedFalse(Pid,clinicId) == null) {
                    errorInfo = ErrorInfo.NoData;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                EcgTest ecgTest = ecgRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ETId,clinicId);
                entity.setModel(ecgTest);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpcode);
    }

    @GetMapping("/ecg-test/{test-id}")
    public ResponseEntity<RestModel> GetRawDataList(
            @PathVariable("clinic") String clinicName, @PathVariable("test-id") int etID) {
        Feed<EcgRawData> feed = new Feed<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                List<EcgRawData> results = ecgRawRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(etID,clinicId);
                List<Entity<EcgRawData>> entities = new LinkedList<>();
                for(EcgRawData result : results){
                    Entity<EcgRawData> entity = new Entity<>();
                    result.setEcgRawData(null);
                    entity.setModel(result);
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



    //Get all the ecg-test for one patient TODO:Test
    @GetMapping("/{patient-id}/ecg-tests")
    public ResponseEntity<RestModel> GetAllEcgTest(@PathVariable("clinic") String clinicName,
                                                   @PathVariable("patient-id") int Pid) {
        Feed<EcgTest> feed = new Feed<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                List<Entity<EcgTest>> entities = new LinkedList<>();
                List<EcgTest> tests = ecgTestRepo.findByClinicIdAndDeletedFalse(clinicId);
                for (EcgTest test : tests) {
                    Entity<EcgTest> entity = new Entity<>();
                    entity.setModel(test);
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

    @GetMapping("/ecg-test/ecg-raw-data/{ecg-raw-data-id}")
    public ResponseEntity<RestModel> getOneData(@PathVariable("clinic") String clinicName,
                                                @PathVariable("ecg-raw-data-id") int dId){
        Entity<EcgRawData> entity = new Entity<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                EcgRawData data = ecgRawRepo.findByEcgRawDataIdAndClinicIdAndDeletedFalse(dId,clinicId);
                if(data==null){
                    errorInfo = ErrorInfo.NoData;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                entity.setModel(data);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpcode);
    }



    /**
     *Find all Ecg Raw Data of specific Ecg Test. Then check the status, if it's hook up, then return the data received
     * in past 5 seconds. If it's recording, then return the data received in past one mins.
     * @param clinicName
     * @param Pid
     * @param etId
     * @param flag
     * @return
     */

    @GetMapping("/{patient-id}/{ecg-test-id}/ecg-raw-data/{status-flag}")
    public ResponseEntity<RestModel> getHookupData(
            @PathVariable("clinic") String clinicName,
            @PathVariable("patient-id") int Pid,
            @PathVariable("ecg-test-id") int etId,
            @PathVariable("status-flag") String flag) {
        Entity<EcgRawData> entity = new Entity<>();

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (pIRepo.findByPatientIdAndClinicIdAndDeletedFalse(Pid,clinicId) == null) {
                    errorInfo = ErrorInfo.InvalidPatientId;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (ecgRawRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(etId,clinicId) == null) {
                    errorInfo = ErrorInfo.InvalidEcgTestId;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(etId,clinicId).getPatientId() != Pid) {
                    errorInfo = ErrorInfo.UnauthroziedPatient;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                List<EcgRawData> datas = ecgRawRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(etId,clinicId);
                for (EcgRawData data : datas) {
                    if (data.getStatusFlag().equals(false) && flag.equals("hookup")) {
                        if (data.getReceivedTime().getTime().equals(time.getTime())) {
                            entity.setModel(data);
                        }
                        Calendar dataAfterFive = data.getReceivedTime();
                        dataAfterFive.add(Calendar.SECOND, periodToFindDataWhenHookingUp);
                        Date timeAfterFiveSecond = dataAfterFive.getTime();
                        //reset the calendar
                        dataAfterFive.add(Calendar.SECOND, -periodToFindDataWhenHookingUp);
                        var test2 = data.getReceivedTime().getTime();
                        var test = time.getTime();
                        if (data.getReceivedTime().getTime().before(time.getTime()) && timeAfterFiveSecond.after(time.getTime())) {
                            entity.setModel(data);
                        }
                    } else if (data.getStatusFlag().equals(true) && flag.equals("record")) {
                        findDataInPastOneMin(data, time, entity);
                    } else if (data.getStatusFlag().equals(false) && flag.equals("rehookup")) {
                        var test = time.getTime();
                        var test2 = data.getReceivedTime().getTime();
                        findDataInPastOneMin(data, time, entity);
                    }
                }
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpcode);

    }

    private void findDataInPastOneMin(EcgRawData data, Calendar time, Entity<EcgRawData> entity) {
        if (data.getReceivedTime().getTime().equals(time.getTime())) {
            entity.setModel(data);
        }
        Calendar dataAfterMin = data.getReceivedTime();
        dataAfterMin.add(Calendar.SECOND, periodToFindDataWhenRecording);
        Date timeAfterMin = dataAfterMin.getTime();
        dataAfterMin.add(Calendar.SECOND, -periodToFindDataWhenRecording);
        if (data.getReceivedTime().getTime().before(time.getTime()) && timeAfterMin.after(time.getTime())) {
            entity.setModel(data);
        }
    }

    //Quartz or Crontab

    @PostMapping("ecg-test/ecg-raw-data")
    @Transactional
    public ResponseEntity<RestModel> uploadEcgRawData(
            @PathVariable("clinic") String clinicName,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("newData") String newData,
            @RequestHeader("verificationCode") String VerificationCode) throws IOException {
        Entity<PhoneResponse> entity = new Entity<>();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        SimpleMailMessage message = new SimpleMailMessage();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                PhoneResponse result = new PhoneResponse(0, false);
                if (vc2Repo.findByContent(VerificationCode) == null) {
                    errorInfo = ErrorInfo.INVALID_VERIFYCODE;
                    httpcode = HttpStatus.UNAUTHORIZED;
                    break;
                }
                if (vc2Repo.findByContent(VerificationCode).getExpired_time().isBefore(LocalDateTime.now())) {
                    errorInfo = ErrorInfo.Expired;
                    httpcode = HttpStatus.UNAUTHORIZED;
                    break;
                }
                if (clinicRepo.findByClinicNameAndDeletedFalse(clinicName) == null) {
                    errorInfo = ErrorInfo.InvalidCliniId;
                    httpcode = HttpStatus.FORBIDDEN;
                    break;
                }
                if (ecgTestRepo.findByDeviceIdAndStartTimeBeforeAndScheduledEndTimeAfterAndClinicIdAndDeletedFalse(
                        vc2Repo.findByContent(VerificationCode).getDeviceId(), nowTime, nowTime,clinicId) == null) {
                    errorInfo = ErrorInfo.No_EcgTest;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }

                EcgRawData jsnewData = new ObjectMapper().readValue(newData, EcgRawData.class);
                jsnewData.setClinicId(clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId());
                jsnewData.setEcgTestId(ecgTestRepo.findByDeviceIdAndStartTimeBeforeAndScheduledEndTimeAfterAndClinicIdAndDeletedFalse(
                        vc2Repo.findByContent(VerificationCode).getDeviceId(), nowTime, nowTime,clinicId).getEcgTestId());
                jsnewData.setDeleted(false);
                
                jsnewData.setReceivedTime(nowTime);
                if(file!=null){
                    jsnewData.setSize(file.getSize());
                }


                if(jsnewData.getPhoneStatus()==OKStatus){
                    //If the data is already in the database, then updateECGTest the data.
                    List<EcgRawData> rawdatas = ecgRawRepo.findByStartTimeAndDeletedFalse(jsnewData.getStartTime());
                    if (!rawdatas.isEmpty()) {
                        for (EcgRawData rawData : rawdatas) {
                            String fileInString = Base64.encodeBase64String(file.getBytes());
                            EcgRawData ecgRawData = ecgRawRepo.findByEcgRawDataIdAndClinicIdAndDeletedFalse(
                                    rawData.getEcgRawDataId(),rawData.getClinicId());
                            ecgRawData.setEcgRawData(fileInString);
                            ecgRawData.setSize(file.getSize());
                            ecgRawRepo.save(ecgRawData);
                        }
                    }
                }

                //Delete the Expired Hookup Data(15 second after)
                Calendar tenSecBefore = nowTime;
                tenSecBefore.add(Calendar.SECOND, -10);

                List<EcgRawData> Expiredrawdatas = ecgRawRepo.findByReceivedTimeBeforeAndStatusFlagAndDeletedFalse(tenSecBefore, false);
                //reset the calendar
                tenSecBefore.add(Calendar.SECOND, 10);
                if (!Expiredrawdatas.isEmpty()) {
                    for (EcgRawData data : Expiredrawdatas) {
                        ecgRawRepo.deleteByEcgRawDataId(data.getEcgRawDataId());
                    }
                }
                //Find the data is hookup or record data .
                if (ecgTestRepo.findByDeviceIdAndStartTimeBeforeAndScheduledEndTimeAfterAndClinicIdAndDeletedFalse(
                        vc2Repo.findByContent(VerificationCode).getDeviceId(), nowTime, nowTime,clinicId).getStatus()== StatusTypes.StatusType.HOOKUP) {

                    result.setcontainData(true);
                    result.setFrequency(hookupFreq);
                    jsnewData.setStatusFlag(false);
                    entity.setModel(result);
                    EcgRawData ecgRawData = dbFilesStorageService.storeFile(file, jsnewData);
                } else if (ecgTestRepo.findByDeviceIdAndStartTimeBeforeAndScheduledEndTimeAfterAndClinicIdAndDeletedFalse(
                        vc2Repo.findByContent(VerificationCode).getDeviceId(), nowTime, nowTime,clinicId).getStatus()== StatusTypes.StatusType.RECORDING) {

                    result.setcontainData(true);
                    result.setFrequency(reocrdFreq);
                    jsnewData.setStatusFlag(true);
                    entity.setModel(result);
                    EcgRawData ecgRawData = dbFilesStorageService.storeFile(file, jsnewData);
                } else if (ecgTestRepo.findByDeviceIdAndStartTimeBeforeAndScheduledEndTimeAfterAndClinicIdAndDeletedFalse(
                        vc2Repo.findByContent(VerificationCode).getDeviceId(), nowTime, nowTime,clinicId).getStatus()== StatusTypes.StatusType.TERMINATED) {

                    //terminate the test
                    result.setcontainData(false);
                    result.setFrequency(terFreq);
                    jsnewData.setStatusFlag(false);
                    // set the actuall end time of the ecg test
                    EcgTest temp = ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(jsnewData.getEcgTestId(),clinicId);
                    temp.setActualEndTime(jsnewData.getEndTime());
                    ecgTestRepo.save(temp);
                    entity.setModel(result);
                } else {
                    result.setFrequency(hookupFreq);
                    result.setcontainData(false);
                    entity.setModel(result);
                }
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = new ErrorInfo("the msg is "+e,2000);
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpcode);
    }

    @PutMapping("/ecg-tests")
    public ResponseEntity<RestModel> updateEcgTest(
            @PathVariable("clinic") String clinicName, @RequestBody EcgTestVo ecgTestVo){
        Entity<resultJson> entity = new Entity<resultJson>();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if(ecgTestVo.getEcgTestId() == null){
                    errorInfo = ErrorInfo.InvalidEcgTestId;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestVo.getEcgTestId(),clinicId)==null) {
                    errorInfo = ErrorInfo.No_EcgTest;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }

                EcgTest test = ecgTestRepo.getOne(ecgTestVo.getEcgTestId());
                if(ecgTestVo.getComment()!=null){
                    test.updateComment(ecgTestVo.getComment());
                }
                ecgTestRepo.save(test);

                //find the ecg-test-id to return;
                resultJson result = new resultJson(test.getEcgTestId().toString());
                entity.setModel(result);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpcode);
    }

    @GetMapping("/running-tests")
    public ResponseEntity<RestModel> getAllRunningTest(@PathVariable("clinic") String clinicName){
        Feed<EcgTest> feed = new Feed<>();
        do{
            try{
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                List<EcgTest> tests = ecgTestRepo.findByStatusAndDeletedFalseOrStatusAndDeletedFalse(StatusTypes.StatusType.HOOKUP,StatusTypes.StatusType.RECORDING);

                List<Entity<EcgTest>> entities = new LinkedList<>();
                for(EcgTest test : tests){
                    Entity entity = new Entity();
                    entity.setModel(test);
                    entities.add(entity);
                }
                feed.setEntities(entities);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            }catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        }while(false);

        return new ResponseEntity<>(new RestModel<>(feed, errorInfo), httpcode);
    }

    @PostMapping("/ecg-tests")
    public ResponseEntity<RestModel> createNewEcgTest(
            @PathVariable("clinic") String clinicName, @RequestBody EcgTestVo ecgTestVo) {
        Entity<resultJson> entity = new Entity<resultJson>();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance();
        outerloop:
        do {
            try {
                if(ecgTestVo.getAppointmentId()==null){
                    errorInfo = ErrorInfo.No_Appointment;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                AppointmentRecord appointmentRecord = appointmentRecordRepository.findByAppointmentRecordIdAndDeleted(ecgTestVo.getAppointmentId(),false);
                if(appointmentRecord==null){
                    errorInfo = ErrorInfo.No_Appointment;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(appointmentRecord.getPickupDate().after(nowTime)){
                    errorInfo = ErrorInfo.AppointmentNotStarted;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if(appointmentRecord.getEcgTestId()!= null){
                    errorInfo = ErrorInfo.DUPLICATE_EcgTest;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                EcgTest newEcgTest = new EcgTest();
                newEcgTest.setPatientId(appointmentRecord.getPatientId());
                newEcgTest.setNurseId(appointmentRecord.getNurseId());
                newEcgTest.setClinicId(clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId());
                newEcgTest.setDeviceId(appointmentRecord.getDeviceId());
                newEcgTest.setDeleted(false);
                newEcgTest.setPhoneId(devicesRepository.findByDeviceIdAndClinicIdAndDeletedFalse(newEcgTest.getDeviceId(),clinicId).getPhoneId());
                newEcgTest.setStatus(StatusTypes.StatusType.NOTSTARTED);

                newEcgTest.setStartTime(appointmentRecord.getAppointmentStartTime());
                newEcgTest.setScheduledEndTime(appointmentRecord.getAppointmentEndTime());
                var ecgTestId = ecgTestRepo.save(newEcgTest).getEcgTestId();

                //TODO
                //connect ECGTest and Appointment

                appointmentRecord.setEcgTestId(ecgTestId);
                appointmentRecordRepository.save(appointmentRecord);
                //find the ecg-test-id to return;
                resultJson result = new resultJson(ecgTestId.toString());
                entity.setModel(result);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), httpcode);
    }

    @DeleteMapping("/ecg-tests/{ecg-test-id}")
    public ResponseEntity<RestModel> deleteEcgTest(
            @PathVariable("clinic") String clinicName, @PathVariable("ecg-test-id") Integer ecgTestId) {
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if(ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId)==null){
                    errorInfo = ErrorInfo.No_EcgTest;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                EcgTest ecgTest = ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId);
                ecgTest.setDeleted(true);
                ecgTestRepo.save(ecgTest);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(errorInfo), httpcode);
    }


    @PutMapping("/ecg-test/{ecg-test-id}/start-hookup/{device-id}")
    public ResponseEntity<RestModel> startHookUp(
            @PathVariable("clinic") String clinicName, @PathVariable("ecg-test-id") int ecgTestId,
            @PathVariable("device-id") int deviceId, @RequestBody EcgTest ecgTest) {
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (devicesRepository.findByDeviceIdAndClinicIdAndDeletedFalse(deviceId,clinicId) == null) {
                    errorInfo = ErrorInfo.InvalidDeviceId;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId) == null) {
                    errorInfo = ErrorInfo.InvalidEcgTestId;
                    httpcode = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (!verifyClinicName(clinicName, ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId).getClinicId())) {
                    break;
                }
                EcgTest test = ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId);
                test.setStatus(StatusTypes.StatusType.HOOKUP);

                ecgTestRepo.save(test);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(errorInfo), httpcode);
    }

    @PutMapping("/ecg-test/{ecg-test-id}/stop/{device-id}")
    public ResponseEntity<RestModel> stop(
            @PathVariable("clinic") String clinicName, @PathVariable("ecg-test-id") int ecgTestId,
            @PathVariable("device-id") int deviceId, @RequestBody EcgTest ecgTest) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (deviceIdisNull(deviceId,clinicId)) {
                    break;
                }
                if (testIdisNull(ecgTestId,clinicId)) {
                    break;
                }
                if (!verifyClinicName(clinicName, ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId).getClinicId())) {
                    break;
                }
                EcgTest test = ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId);
                test.setStatus(StatusTypes.StatusType.TERMINATED);

                test.setActualEndTime(nowTime);
                ecgTestRepo.save(test);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(errorInfo), httpcode);
    }

    @PutMapping("/ecg-test/{ecg-test-id}/resume/{device-id}")
    public ResponseEntity<RestModel> Resume(
            @PathVariable("clinic") String clinicName, @PathVariable("ecg-test-id") int ecgTestId,
            @PathVariable("device-id") int deviceId) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (deviceIdisNull(deviceId,clinicId)) {
                    break;
                }
                if (testIdisNull(ecgTestId,clinicId)) {
                    break;
                }
                if (!verifyClinicName(clinicName, ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId).getClinicId())) {
                    break;
                }
                EcgTest test = ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId);
                test.setStatus(StatusTypes.StatusType.NOTSTARTED);

                test.setRestartTime(nowTime);
                ecgTestRepo.save(test);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(errorInfo), httpcode);
    }

    @PutMapping("/ecg-test/{ecg-test-id}/start-record/{device-id}")
    public ResponseEntity<RestModel> startRecording(
            @PathVariable("clinic") String clinicName, @PathVariable("ecg-test-id") int ecgTestId,
            @PathVariable("device-id") int deviceId, @RequestBody EcgTest ecgTest) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (deviceIdisNull(deviceId,clinicId)) {
                    break;
                }
                if (testIdisNull(ecgTestId,clinicId)) {
                    break;
                }
                if (!verifyClinicName(clinicName, ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId).getClinicId())) {
                    break;
                }
                EcgTest test = ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId);
                test.setStatus(StatusTypes.StatusType.RECORDING);

                //When the nurse start the recording for the first Time, set the startTime and endTime
                if(test.getStartTime()==null){
                    test.setStartTime(time);
                    time.add(Calendar.HOUR,24);
                    AppointmentRecord app = appointmentRecordRepository.findByEcgTestIdAndDeleted(test.getEcgTestId(),false);
                    if(time.after(app.getAppointmentEndTime())){
                        test.setScheduledEndTime(app.getAppointmentEndTime());
                    }else{
                        test.setScheduledEndTime(time);
                    }
                }
                ecgTestRepo.save(test);
                errorInfo = ErrorInfo.OK;
                httpcode = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                httpcode = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(errorInfo), httpcode);
    }

    @GetMapping("/ecg-test")
    public ResponseEntity<RestModel> getEcgTestList(
            @PathVariable("clinic") String clinicName, @RequestParam(value = "period-start-time") String start,
            @RequestParam(value="period-end-time") String end) {
        Feed<EcgTest> feed = new Feed<>();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        do {
            try {
                Calendar startTime = Calendar.getInstance();
                Calendar endTime = Calendar.getInstance();
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if(start!=null&&end!=null) {
                    SimpleDateFormat df1 = new SimpleDateFormat(dateFormat);
                    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                    df1.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = df1.parse(start);
                    Date date2 = df1.parse(end);
                    startTime.setTime(date);
                    endTime.setTime(date2);
                }
                List<EcgTest> results = ecgTestRepo.findByStartTimeAfterAndActualEndTimeBeforeAndClinicIdAndDeletedFalse(startTime,endTime,clinicId);
                List<Entity<EcgTest>> entities = new LinkedList<>();
                for(EcgTest result : results){
                    Entity<EcgTest> entity  = new Entity<>();
                    entity.setModel(result);
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

    //Verify the ClinicId
    private boolean verifyClinicName(String clinicName, Integer pid) {
        if (clinicRepo.findByClinicNameAndDeletedFalse(clinicName) == null) {
            this.errorInfo = ErrorInfo.InvalidCliniId;
            this.httpcode = HttpStatus.BAD_REQUEST;
            return false;
        }
        if (clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId() != pid) {
            this.errorInfo = ErrorInfo.ConflictClinicId;
            this.httpcode = HttpStatus.BAD_REQUEST;
            return false;
        }
        return true;
    }

    private boolean deviceIdisNull(Integer deviceId,Integer clinicId) {
        if (devicesRepository.findByDeviceIdAndClinicIdAndDeletedFalse(deviceId,clinicId) == null) {
            errorInfo = ErrorInfo.InvalidDeviceId;
            httpcode = HttpStatus.BAD_REQUEST;
            return true;
        }
        return false;
    }

    private boolean testIdisNull(Integer ecgTestId, Integer clinicId) {
        if (ecgTestRepo.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTestId,clinicId) == null) {
            errorInfo = ErrorInfo.InvalidEcgTestId;
            httpcode = HttpStatus.BAD_REQUEST;
            return true;
        }
        return false;
    }
}
