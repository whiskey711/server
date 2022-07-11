package ca.uvic.ecg.model.vo;

import ca.uvic.ecg.model.EcgTest;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EcgTestAndAppVo{
    private Integer patientId;
    private Integer nurseId;
    private Integer deviceId;
    private String comment;
    private Integer appointmentId;


    public EcgTest copy(){
        EcgTest ecgTest = new EcgTest();
        ecgTest.setPatientId(this.patientId);
        ecgTest.setNurseId(this.nurseId);
        ecgTest.setDeviceId(this.deviceId);
        ecgTest.setComment(this.comment);
        return ecgTest;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }
}
