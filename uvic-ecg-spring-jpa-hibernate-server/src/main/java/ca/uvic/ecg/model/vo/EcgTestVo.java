package ca.uvic.ecg.model.vo;

import ca.uvic.ecg.model.AppointmentRecord;
import ca.uvic.ecg.model.EcgTest;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import java.util.Calendar;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EcgTestVo {
    private Integer ecgTestId;
    private Calendar startTime;
    private Calendar scheduledEndTime;
    private Calendar actualEndTime;
    private Integer appointmentId;
    private String comment;

    private StatusTypesVo.StatusType status;

    private List<EcgRawDataVo> rawDataList;

    public EcgTestVo(){}

    public Integer getEcgTestId() {
        return ecgTestId;
    }

    public void setEcgTestId(Integer ecgTestId) {
        this.ecgTestId = ecgTestId;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getScheduledEndTime() {
        return scheduledEndTime;
    }

    public void setScheduledEndTime(Calendar scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    public Calendar getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Calendar actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public StatusTypesVo.StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusTypesVo.StatusType status) {

        this.status = status;
    }

    public List<EcgRawDataVo> getRawDataList() {
        return rawDataList;
    }

    public void setRawDataList(List<EcgRawDataVo> rawDataList) {
        this.rawDataList = rawDataList;
    }
}
