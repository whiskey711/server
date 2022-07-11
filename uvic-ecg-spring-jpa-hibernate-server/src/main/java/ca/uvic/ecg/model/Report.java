package ca.uvic.ecg.model;

import javax.persistence.*;

@Entity
@Table(name = "report", catalog = "ecg")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    public Integer reportId;

    @Column(name = "ecg_test_id", nullable = false)
    public Integer ecgTestId;

    @Column(name = "patient_id", nullable = false)
    public Integer patientId;

    @Column(name = "report", nullable = false)
    public String report;

    @Column(name = "deleted")
    private Boolean deleted;

    public Report(){}

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getEcgTestId() {
        return ecgTestId;
    }

    public void setEcgTestId(Integer ecgTestId) {
        this.ecgTestId = ecgTestId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
