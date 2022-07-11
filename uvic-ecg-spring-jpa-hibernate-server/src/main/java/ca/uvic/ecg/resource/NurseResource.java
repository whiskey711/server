package ca.uvic.ecg.resource;

import ca.uvic.ecg.model.*;
import ca.uvic.ecg.model.vo.*;

import ca.uvic.ecg.repository.*;
import ca.uvic.ecg.webModel.ErrorInfo;
import ca.uvic.ecg.webModel.RestModel;
import com.mkingzhu.dataprotocol.Entity;
import com.mkingzhu.dataprotocol.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/v1/{clinic}")
public class NurseResource {
    @Autowired
    NurseRepository nurseRepository;
    @Autowired
    DevicesRepository deviRepo;
    @Autowired
    RemarksRepository remarkRepo;
    @Autowired
    ClinicRepository clinicRepo;
    @Autowired
    AppointmentRecordRepository appRepo;
    @Autowired
    EcgTestRepository ecgTestRepository;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private ErrorInfo errorInfo;
    private HttpStatus http;
    private String dateFormat = "yyyy-MM-dd'T'HH:mm:ssz";

    //Get info of all the nurses  (Tested)
    @GetMapping("/getnurses")
    public ResponseEntity<RestModel> getAll(@PathVariable("clinic") String clinicName) {
        Feed<Nurse> feed = new Feed<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                List<Entity<Nurse>> entities = new LinkedList<>();
                List<Nurse> nurses = nurseRepository.findByClinicIdAndDeletedFalse(clinicId);
                for (Nurse nurse : nurses) {
                    Entity<Nurse> entity = new Entity<>();
                    entity.setModel(nurse);
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


    @PostMapping("/nurses")
    public ResponseEntity<RestModel> register(@PathVariable("clinic") String clinicName, @RequestBody NurseVo nurseVo) {
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (nurseRepository.findByNurseEmailAndClinicIdAndDeletedFalse(nurseVo.getNurseEmail(),clinicId) != null) {
                    errorInfo = ErrorInfo.DUPLICATE_USER;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                if(nurseVo.getPassword().length()<10){
                    errorInfo = ErrorInfo.Short_Pswd;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                Nurse nurse = nurseVo.createNewNurse();
                String rawPaswd = nurseVo.getPassword();
                nurse.setPassword(bCryptPasswordEncoder.encode(rawPaswd));
                nurse.setNumberOfFailures(0);
                nurse.setClinicId(clinicId);
                nurseRepository.save(nurse);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);

        return new ResponseEntity<>(new RestModel<>(errorInfo), http);
    }

    //TODO:test
    @GetMapping("/device/{device-id}")
    public ResponseEntity<RestModel> GetOneDevice(
            @PathVariable("clinic") String clinicName,
            @PathVariable("device-id") Integer Did) {
        Entity<Device> entity = new Entity<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (deviRepo.findByDeviceIdAndClinicIdAndDeletedFalse(Did,clinicId) == null) {
                    errorInfo = ErrorInfo.NoData;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                entity.setModel(deviRepo.findByDeviceIdAndClinicIdAndDeletedFalse(Did,clinicId));
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), http);
    }

    //Get All the Device information by selecting the device_id TODO: test
    @GetMapping("/Alldevice")
    public ResponseEntity<RestModel> GetAllDevices(@PathVariable("clinic") String clinicName) {
        Feed<Device> feed = new Feed<>();
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                List<Entity<Device>> entities = new LinkedList<>();
                List<Device> devices = deviRepo.findByClinicIdAndDeletedFalse(clinicId);
                for (Device device : devices) {
                    Entity<Device> entity = new Entity<>();
                    entity.setModel(device);
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

    @GetMapping("/devices")
    public ResponseEntity<RestModel> GetAllAvailableDevices(@PathVariable("clinic") String clinicName,
                                                            @RequestParam("pickupDate") String pickupDate,
                                                            @RequestParam("deviceReturnDate") String deviceReturnDate,
                                                            @RequestParam("deviceLocation") String deviceLoc
    ) {
        Feed<Device> feed = new Feed<>();
        do {
            try {
                //parsing string to Date to Calendar.
                SimpleDateFormat df1 = new SimpleDateFormat(dateFormat);
                TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                Calendar pickUpC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                Calendar returnC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                df1.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date pickUpd = df1.parse(pickupDate);
                Date returnDate = df1.parse(deviceReturnDate);
                pickUpC.setTime(pickUpd);
                returnC.setTime(returnDate);
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                //Avoid NullpointerException
                List<Entity<Device>> entities = new LinkedList<>();
                List<Device> devices = deviRepo.findByClinicIdAndDeletedFalse(clinicId);

                //Using iterator to avoid ConcurrentModificationException;
                Iterator<Device> iterator = devices.iterator();
                while (iterator.hasNext()) {
                    Device device = iterator.next();
                    if (!appRepo.findByDeviceIdAndClinicIdAndDeleted(device.getDeviceId(),clinicId,false).isEmpty()) {
                        if (!appRepo.findByPickupDateAfterAndDeviceReturnDateBeforeAndDeviceIdAndDeletedFalse(pickUpC, returnC, device.getDeviceId()).isEmpty()) {
                            iterator.remove();
                        }
                    }
                }
                for (Device device : devices) {
                    if (device.getDeviceLocation().equals(deviceLoc)) {
                        Entity<Device> entity = new Entity<>();
                        entity.setModel(device);
                        entities.add(entity);
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

    @PostMapping("/addDevices")
    public ResponseEntity<RestModel> AddNewDevice(@PathVariable("clinic") String clinicName,
                                                  @RequestBody DevicesVo devicesVo) {
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if (deviRepo.findByDeviceNameAndClinicIdAndDeletedFalse(devicesVo.getDeviceName(),clinicId) != null) {
                    errorInfo = ErrorInfo.DUPLICATE_Device;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                Device device = devicesVo.createNewDevice();
                device.setClinicId(clinicId);
                deviRepo.save(device);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(errorInfo), http);
    }

    @PutMapping("/return-status/{deviceId}")
    public ResponseEntity<RestModel> returnPhone(@PathVariable("clinic") String clinicName,
                                                 @PathVariable("deviceId") Integer dId,
                                                 @RequestBody AppointmentVo appointmentVo) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        outterloop:
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                Device updateDevice = deviRepo.findByDeviceIdAndClinicIdAndDeletedFalse(dId,clinicId);
                if(updateDevice==null) {
                    errorInfo = ErrorInfo.InvalidDeviceId;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                List<AppointmentRecord> appointmentRecords = appRepo.findByDeviceIdAndAppointmentStartTimeBeforeAndDeviceActualReturnTimeIsNull(dId,nowTime);
                if(appointmentRecords.isEmpty()){
                    errorInfo = ErrorInfo.Has_Returned;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                for(AppointmentRecord appointmentRecord:appointmentRecords){
                    EcgTest ecgTest = ecgTestRepository.findByEcgTestIdAndDeletedFalse(appointmentRecord.getEcgTestId());
                    if(ecgTest==null){
                        errorInfo = ErrorInfo.No_EcgTest;
                        http = HttpStatus.BAD_REQUEST;
                        continue;
                    }
                    if(ecgTest.getStatus()!= StatusTypes.StatusType.TERMINATED){
                        errorInfo = ErrorInfo.Is_Running;
                        http = HttpStatus.BAD_REQUEST;
                        break outterloop;
                    }
                    appointmentRecord.setDeviceActualReturnTime(nowTime);
                    appRepo.save(appointmentRecord);
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
}
