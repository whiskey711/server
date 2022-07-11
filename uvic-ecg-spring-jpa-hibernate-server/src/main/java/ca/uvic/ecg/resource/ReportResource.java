package ca.uvic.ecg.resource;

import ca.uvic.ecg.model.AppointmentRecord;
import ca.uvic.ecg.model.PatientInfo;
import ca.uvic.ecg.model.Report;
import ca.uvic.ecg.model.vo.AppointmentVo;
import ca.uvic.ecg.model.vo.ReportVo;
import ca.uvic.ecg.repository.*;
import ca.uvic.ecg.webModel.ErrorInfo;
import ca.uvic.ecg.webModel.RestModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@RestController
@RequestMapping("/v1/{clinic}")
public class ReportResource {
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    ClinicRepository clinicRepo;
    @Autowired
    PatientInfoRepository patientInfoRepository;
    @Autowired
    EcgTestRepository ecgTestRepository;
    @Autowired
    DbFilesStorageService dbFilesStorageService;

    private ErrorInfo errorInfo;
    private HttpStatus http;

    @PostMapping("/report")
    @Transactional
    public ResponseEntity<RestModel> uploadReport(
            @PathVariable("clinic") String clinicName,
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart("report") String report
    ) throws IOException {
        outerloop:
        do {
            try {
                int clinicId = clinicRepo.findByClinicNameAndDeletedFalse(clinicName).getClinicId();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ReportVo newReportVo = objectMapper.readValue(report, ReportVo.class);
                if (patientInfoRepository.findByPatientIdAndClinicIdAndDeletedFalse(newReportVo.getPatientInfo().getPatientId(), clinicId) == null) {
                    errorInfo = ErrorInfo.NoPatient;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (ecgTestRepository.findByEcgTestIdAndDeletedFalse(newReportVo.getEcgTest().getEcgTestId()) == null) {
                    errorInfo = ErrorInfo.No_EcgTest;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                if (reportRepository.findByEcgTestIdAndPatientIdAndDeletedFalse(newReportVo.getEcgTest().getEcgTestId(),newReportVo.getPatientInfo().getPatientId()) != null) {
                    errorInfo = ErrorInfo.DublicateData;
                    http = HttpStatus.BAD_REQUEST;
                    break;
                }
                Report newReport = newReportVo.createNewReport();
                dbFilesStorageService.storeReport(file,newReport);
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
