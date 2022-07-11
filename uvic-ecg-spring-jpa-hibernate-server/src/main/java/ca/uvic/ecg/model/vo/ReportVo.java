package ca.uvic.ecg.model.vo;

import ca.uvic.ecg.model.EcgTest;
import ca.uvic.ecg.model.PatientInfo;
import ca.uvic.ecg.model.Report;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReportVo {
    private Integer reportId;
    private PatientInfo patientInfo;
    private EcgTest ecgTest;
    private String report;

    public ReportVo(){}

    public Report createNewReport(){
        Report report = new Report();
        report.setEcgTestId(this.ecgTest.getEcgTestId());
        report.setPatientId(this.patientInfo.getPatientId());
        report.setReport(this.report);
        report.setDeleted(false);
        return report;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public PatientInfo getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

    public EcgTest getEcgTest() {
        return ecgTest;
    }

    public void setEcgTest(EcgTest ecgTest) {
        this.ecgTest = ecgTest;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
