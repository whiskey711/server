package ca.uvic.ecg.resource;

import ca.uvic.ecg.model.Computer;
import ca.uvic.ecg.model.vo.ComputerVo;
import ca.uvic.ecg.repository.ClinicRepository;
import ca.uvic.ecg.repository.ComputerRepository;
import ca.uvic.ecg.webModel.ErrorInfo;
import ca.uvic.ecg.webModel.RestModel;
import com.mkingzhu.dataprotocol.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/v1/{clinic}")
public class ComputerResource {

    @Autowired
    ClinicRepository clinicRepository;
    @Autowired
    ComputerRepository computerRepository;
    private ErrorInfo errorInfo;
    private HttpStatus http;

    @PostMapping("/computer")
    public ResponseEntity<RestModel> addComputer(@PathVariable("clinic") String clinicName,
                                                 @RequestBody ComputerVo computerVo) {
        do {
            try {
                int clinicId = clinicRepository.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                List<String> macAddresses = computerVo.getMacAddresses();
                if(macAddresses==null){
                    errorInfo = ErrorInfo.MissingInput;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                for(String str : macAddresses){
                    if(str==null){
                        continue;
                    }
                    str = "%"+str+"%";
                    if(computerRepository.findByMacAddressesLike(str)!=null){
                        errorInfo = ErrorInfo.DublicateData;
                        http = HttpStatus.BAD_REQUEST;
                        break;
                    }
                }
                Computer computer = computerVo.createComputer();
                computer.setClinicId(clinicId);
                computerRepository.save(computer);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(errorInfo), http);
    }

    //would return the first computer that matches the macAddress
    @GetMapping("/computer")
    public ResponseEntity<RestModel> getComputer(@PathVariable("clinic") String clinicName,
                                                 @PathParam("macAddresses") String macAddresses) {
        Entity<Computer> entity = new Entity<>();
        outterloop:
        do {
            try {
                int clinicId = clinicRepository.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if(macAddresses==null){
                    errorInfo = ErrorInfo.MissingInput;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                String[] mac = macAddresses.split(",");
                for(String str : mac){
                    if(str == null){
                        continue;
                    }
                    str = "%"+str+"%";
                    if(computerRepository.findByMacAddressesLike(str)!=null){
                        Computer computer = computerRepository.findByMacAddressesLike(str);
                        if(computer.getClinicId()!=clinicId){
                            errorInfo = ErrorInfo.UnauthroziedComputer;
                            http = HttpStatus.BAD_REQUEST;
                            break outterloop;
                        }
                        entity.setModel(computer);
                        errorInfo = ErrorInfo.OK;
                        http = HttpStatus.OK;
                        break outterloop;
                    }
                }
                errorInfo = ErrorInfo.NoData;
                http = HttpStatus.BAD_REQUEST;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(entity, errorInfo), http);
    }

    @PutMapping("/computer/{computerId}")
    public ResponseEntity<RestModel> updateComputer(@PathVariable("clinic") String clinicName,
                                                 @PathVariable("computerId") Integer computerId,
                                                 @RequestBody ComputerVo computerVo) {
        do {
            try {
                int clinicId = clinicRepository.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                if(computerVo.getComputerId()==null){
                    errorInfo = ErrorInfo.MissingInput;
                    http = HttpStatus.BAD_REQUEST;
                }
                if(computerRepository.findByComputerIdAndClinicId(computerId,clinicId)==null){
                    errorInfo = ErrorInfo.NoData;
                    http = HttpStatus.BAD_REQUEST;
                }

                Computer computer = computerRepository.getOne(computerId);
                if(computerVo.getMacAddresses()!=null){
                    computer.setMacAddresses(computerVo.getMacAddresses());
                }
                if(computerVo.getFailedEcgRawData()!=null){
                    computer.setFailedEcgRawData(computerVo.getFailedEcgRawData());
                }
                if(computerVo.getFailedEcgTest()!=null){
                    computer.setFailedEcgTest(computerVo.getFailedEcgTest());
                }
                if(computerVo.getLastRequestTime()!=null){
                    computer.setLastRequestTime(computerVo.getLastRequestTime());
                }
                computerRepository.save(computer);
                errorInfo = ErrorInfo.OK;
                http = HttpStatus.OK;
            } catch (Exception e) {
                errorInfo = ErrorInfo.UnKnown;
                http = HttpStatus.BAD_REQUEST;
            }
        } while (false);
        return new ResponseEntity<>(new RestModel<>(errorInfo), http);
    }
}
