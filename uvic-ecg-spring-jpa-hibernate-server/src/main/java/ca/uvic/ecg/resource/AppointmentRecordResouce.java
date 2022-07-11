package ca.uvic.ecg.resource;

import ca.uvic.ecg.model.AppointmentRecord;
import ca.uvic.ecg.model.Device;
import ca.uvic.ecg.model.EcgTest;
import ca.uvic.ecg.model.PatientInfo;
import ca.uvic.ecg.model.vo.DevicesVo;
import ca.uvic.ecg.model.vo.PatientVo;
import ca.uvic.ecg.model.vo.SubAppointment;
import ca.uvic.ecg.model.vo.AppointmentVo;
import ca.uvic.ecg.repository.*;
import ca.uvic.ecg.webModel.ErrorInfo;
import ca.uvic.ecg.webModel.RestModel;
import com.mkingzhu.dataprotocol.Entity;
import com.mkingzhu.dataprotocol.Feed;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/v1/{clinic}")
public class AppointmentRecordResouce {
    @Autowired
    AppointmentRecordRepository appRepo;
    @Autowired
    ClinicRepository clinicRepo;
    @Autowired
    PatientInfoRepository patientRepo;
    @Autowired
    DevicesRepository deviRepo;
    @Autowired
    EcgTestRepository ecgTestRepository;

    private ErrorInfo errorInfo;
    private HttpStatus http;
    private final String dateFormat = "yyyy-MM-dd'T'HH:mm:ssz";

    @GetMapping("/appointment-records/{patient-id}")
    public ResponseEntity<RestModel> getRecordByPatient(@PathVariable("clinic") String clinicName,
                                                        @PathVariable("patient-id") Integer pId) {

        Feed<AppointmentVo> feed = new Feed<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();

                List<Entity<AppointmentVo>> entities = new LinkedList<>();
                List<AppointmentRecord> apps = appRepo.findByPatientIdAndClinicIdAndDeletedFalse(pId,clinicId);
                for (AppointmentRecord app : apps) {
                    AppointmentVo vo = app.copyToVo();
                    PatientInfo patientInfo = patientRepo.findByPatientIdAndClinicIdAndDeletedFalse(pId,clinicId);
                    vo.getPatientVo().setPatientFirstName(patientInfo.getPatientFirstName());
                    vo.getPatientVo().setPatientLastName(patientInfo.getPatientLastName());
                    Device device = deviRepo.findByDeviceIdAndClinicIdAndDeletedFalse(vo.getDevicesVo().getDeviceId(),clinicId);
                    vo.getDevicesVo().setDeviceLocation(device.getDeviceLocation());
                    Entity<AppointmentVo> entity = new Entity<>();
                    entity.setModel(vo);
                    entities.add(entity);
                }
                feed.setEntities(entities);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(feed, errorInfo), http);
    }

    @GetMapping("/appointment-records")
    public ResponseEntity<RestModel> getRecord(@PathVariable("clinic") String clinicName,
                                               @RequestParam(value = "start-time", required = false) String startTimeString,
                                               @RequestParam(value = "end-time", required = false) String endTimeString,
                                               @RequestParam(value = "location", required = false) String location) {
        Feed<AppointmentVo> feed = new Feed<>();
        do {
            try {
                Calendar startTime = Calendar.getInstance();
                Calendar endTime = Calendar.getInstance();
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                List<Entity<AppointmentVo>> entities = new LinkedList<>();
                //if startTime and endTime is not empty
                if (startTimeString != null && endTimeString != null) {
                    SimpleDateFormat df1 = new SimpleDateFormat(dateFormat);
                    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                    df1.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = df1.parse(startTimeString);
                    Date date2 = df1.parse(endTimeString);
                    startTime.setTime(date);
                    endTime.setTime(date2);
                }

                if(location!=null){
                    List<Device> devices = deviRepo.findByDeviceLocationAndDeletedFalseAndClinicId(location,clinicId);
                    for(Device device : devices){
                        if(startTimeString!=null&&endTimeString!=null){
                            List<AppointmentRecord> apps = appRepo.findByDeviceIdAndAppointmentStartTimeAfterAndAppointmentEndTimeBeforeAndDeletedFalse
                                    (device.getDeviceId(),startTime,endTime);
                            entities = addAppointmentToResult(clinicId, entities, apps);
                        }else{
                            List<AppointmentRecord> apps = appRepo.findByDeviceIdAndClinicIdAndDeleted(device.getDeviceId(),clinicId,false);
                            entities = addAppointmentToResult(clinicId, entities, apps);
                        }

                    }
                }else{
                    if(startTimeString!=null&&endTimeString!=null){
                        List<AppointmentRecord> apps = appRepo.findByAppointmentStartTimeAfterAndAppointmentEndTimeBeforeAndDeletedFalse(startTime,endTime);
                        entities = addAppointmentToResult(clinicId, entities, apps);
                    }
                }

                feed.setEntities(entities);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(feed, errorInfo), http);
    }

    @PostMapping("/appointment-records")
    public ResponseEntity<RestModel> addRecord(@PathVariable("clinic") String clinicName,
                                               @RequestBody AppointmentVo record) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        outerloop:
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (appRepo.findByAppointmentStartTimeAndDeviceIdAndClinicIdAndDeleted(record.getAppointmentStartTime(), record.getDevicesVo().getDeviceId(), clinicId,false) != null) {
                    errorInfo = ErrorInfo.DUPLICATE_RECORD;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                List<AppointmentRecord> records = appRepo.findByDeviceIdAndClinicIdAndDeleted(record.getDevicesVo().getDeviceId(),clinicId,false);
                if (!records.isEmpty()) {
                    for (AppointmentRecord sameDidrecord : records) {
                        if (record.getPickupDate().before(sameDidrecord.getDeviceReturnDate()) && record.getPickupDate().after(sameDidrecord.getPickupDate())) {
                            errorInfo = ErrorInfo.Conflict_Schedule;
                            http = HttpStatus.BAD_REQUEST;
                            break outerloop;
                        }
                        if (record.getDeviceReturnDate().before(sameDidrecord.getDeviceReturnDate()) && record.getDeviceReturnDate().after(sameDidrecord.getPickupDate())) {
                            errorInfo = ErrorInfo.Conflict_Schedule;
                            http = HttpStatus.BAD_REQUEST;
                            break outerloop;
                        }
                    }
                }
                AppointmentRecord appointmentRecord = record.createAppointmentRecord();
                appointmentRecord.setReservationTime(nowTime);
                appointmentRecord.setClinicId(clinicId);
                appRepo.save(appointmentRecord);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(errorInfo), http);
    }

    @PutMapping("/appointment-record/{appointment-record-id}")
    public ResponseEntity<RestModel> updateAppointment(@PathVariable("clinic") String clinicName,
                                                       @PathVariable("appointment-record-id") int apId,
                                                       @RequestBody AppointmentVo appointment) {
        outerloop:
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (appRepo.findByAppointmentRecordIdAndClinicIdAndDeletedFalse(apId,clinicId) == null) {
                    errorInfo = ErrorInfo.No_Appointment;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                //using getOne to get the reference to the appointment
                AppointmentRecord newApp = appRepo.getOne(apId);
                if(newApp==null){
                    errorInfo = ErrorInfo.No_Appointment;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                Calendar pick = appointment.getPickupDate();
                Calendar returnD = appointment.getDeviceReturnDate();
                int dId = appointment.getDevicesVo().getDeviceId();
                if (pick != null && returnD != null && appointment.getDevicesVo().getDeviceId() != null) {
                    ////check appointment pick up and return time overlap
                    List<AppointmentRecord> appointmentRecords = appRepo.findByPickupDateBeforeAndDeviceReturnDateAfterAndDeviceIdAndClinicIdAndAppointmentRecordIdNotAndDeletedFalse
                            (pick, pick, dId, clinicId, apId);
                    List<AppointmentRecord> appointmentRecords2 = appRepo.findByPickupDateBeforeAndDeviceReturnDateAfterAndDeviceIdAndClinicIdAndAppointmentRecordIdNotAndDeletedFalse
                            (returnD, returnD, dId, clinicId, apId);
                    if (appointmentRecords.isEmpty() && appointmentRecords2.isEmpty()) {
                        newApp.setPickupDate(pick);
                        newApp.setDeviceReturnDate(returnD);
                    } else {
                        errorInfo = ErrorInfo.Conflict_Schedule;
                        http = HttpStatus.BAD_REQUEST;
                        break outerloop;
                    }
                    Calendar start = appointment.getAppointmentStartTime();
                    Calendar end = appointment.getAppointmentEndTime();
                    if (start != null && end != null && appointment.getDevicesVo().getDeviceId() != null) {
                        //check appointment start and end time overlap
                        if (start.before(pick) || start.after(returnD)) {
                            errorInfo = ErrorInfo.Invalid_Input;
                            http = HttpStatus.BAD_REQUEST;
                            break;
                        }

                        if (end.after(returnD) || end.before(pick)) {
                            errorInfo = ErrorInfo.Invalid_Input;
                            http = HttpStatus.BAD_REQUEST;
                            break;
                        }
                        newApp.setAppointmentStartTime(start);
                        newApp.setAppointmentEndTime(end);
                    }
                }

                newApp.update(appointment);
                appRepo.save(newApp);

                //update ECG test as well
                //check if there is a valid ECG TestId
                if(newApp.getEcgTestId()==null
                        || ecgTestRepository.findByEcgTestIdAndClinicIdAndDeletedFalse(newApp.getEcgTestId(),clinicId)==null){
                    errorInfo = ErrorInfo.InvalidEcgTestId;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                int ecgTestId = newApp.getEcgTestId();
                EcgTest ecgTest = ecgTestRepository.findByEcgTestIdAndDeletedFalse(ecgTestId);
                //check if need to update scheduledEndTime
                if(ecgTest.getScheduledEndTime()!=null){
                    ecgTest.setScheduledEndTime(newApp.getAppointmentEndTime());
                }
                //check if need to update startEndTime
                if(ecgTest.getStartTime()!=null){
                    ecgTest.setStartTime(newApp.getAppointmentStartTime());
                }
                if(newApp.getPatientId()!=null){
                    ecgTest.setPatientId(newApp.getPatientId());
                }
                if(newApp.getNurseId()!=null){
                    ecgTest.setNurseId(newApp.getNurseId());
                }
                //check if need to update device Id and phone Id
                if(newApp.getDeviceId()!=null){
                    ecgTest.setDeviceId(newApp.getDeviceId());
                    ecgTest.setPhoneId(deviRepo.findByDeviceIdAndClinicIdAndDeletedFalse(newApp.getDeviceId(),clinicId).getPhoneId());
                }

                ecgTestRepository.save(ecgTest);


                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(errorInfo), http);
    }

    @DeleteMapping("/appointment-record/{appointment-record-Id}")
    public ResponseEntity<RestModel> deleteAppointment(@PathVariable("clinic") String clinicName,
                                                       @PathVariable("appointment-record-Id") int apId) {
        outerloop:
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (appRepo.findByAppointmentRecordIdAndClinicIdAndDeletedFalse(apId,clinicId) == null) {
                    errorInfo = ErrorInfo.No_Appointment;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                //using getOne to get the reference to the appointment
                AppointmentRecord newApp = appRepo.getOne(apId);
                newApp.setDeleted(true);
                appRepo.save(newApp);

                //update ECG test as well
                if(newApp.getEcgTestId()==null){
                    break;
                }
                int ecgTestId = newApp.getEcgTestId();
                EcgTest ecgTest = ecgTestRepository.findByEcgTestIdAndDeletedFalse(ecgTestId);
                ecgTest.setDeleted(true);
                ecgTestRepository.save(ecgTest);

                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(errorInfo), http);
    }

    private List<Entity<AppointmentVo>> addAppointmentToResult(int clinicId, List<Entity<AppointmentVo>> entities, List<AppointmentRecord> apps) throws IllegalAccessException, InvocationTargetException {
        for(AppointmentRecord result :apps){
            AppointmentVo appointmentVo = result.copyToVo();
            PatientInfo patientInfo = patientRepo.findByPatientIdAndClinicIdAndDeletedFalse(appointmentVo.getPatientVo().getPatientId(),clinicId);
            appointmentVo.getPatientVo().setPatientFirstName(patientInfo.getPatientFirstName());
            appointmentVo.getPatientVo().setPatientLastName(patientInfo.getPatientLastName());
            Device device = deviRepo.findByDeviceIdAndClinicIdAndDeletedFalse(appointmentVo.getDevicesVo().getDeviceId(),clinicId);
            appointmentVo.getDevicesVo().setDeviceLocation(device.getDeviceLocation());
            Entity<AppointmentVo> entity = new Entity<>();
            entity.setModel(appointmentVo);
            entities.add(entity);
        }
        return entities;
    }
}
