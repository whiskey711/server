package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    Report findByReportIdAndDeletedFalse(Integer reportId);


    Report findByEcgTestIdAndPatientIdAndDeletedFalse(Integer ecgTestId, Integer patientId);
}
