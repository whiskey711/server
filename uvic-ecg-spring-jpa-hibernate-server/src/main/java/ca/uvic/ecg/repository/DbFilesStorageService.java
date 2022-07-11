package ca.uvic.ecg.repository;

import ca.uvic.ecg.exceptions.FileStorageException;
import ca.uvic.ecg.exceptions.MyFileNotFoundException;
import ca.uvic.ecg.model.EcgRawData;
import ca.uvic.ecg.model.Report;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DbFilesStorageService {
    @Autowired
    private EcgRawDataRepository ecgDataRepository;

    @Autowired
    private ReportRepository reportRepository;

    public EcgRawData storeFile(MultipartFile file, EcgRawData newData) {
        // Normalize file name
        if (file.isEmpty()) {
            return ecgDataRepository.save(newData);
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            //DbFiles dbFile = new DbFiles(fileName, file.getContentType(), file.getBytes());

            //Blob fileInBlob = new SerialBlob(file.getBytes());
            String fileInString = Base64.encodeBase64String(file.getBytes());
            newData.setEcgRawData(fileInString);

            //return dbFileRepo.save(dbFile);
            return ecgDataRepository.save(newData);
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    public Report storeReport(MultipartFile file, Report report) {
        // Normalize file name
        if (file.isEmpty()) {
            return reportRepository.save(report);
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            //DbFiles dbFile = new DbFiles(fileName, file.getContentType(), file.getBytes());

            //Blob fileInBlob = new SerialBlob(file.getBytes());
            String fileInString = Base64.encodeBase64String(file.getBytes());
            report.setReport(fileInString);

            //return dbFileRepo.save(dbFile);
            return reportRepository.save(report);
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    public EcgRawData getFile(Integer fileId) {
        return ecgDataRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
}
