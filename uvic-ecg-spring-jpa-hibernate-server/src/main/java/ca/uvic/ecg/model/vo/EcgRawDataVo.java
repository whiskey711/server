package ca.uvic.ecg.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import java.util.Calendar;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EcgRawDataVo {
    private Integer ecgRawDataId;
    private Integer ecgTestId;
    private Calendar receivedTime;
    private String ecgRawData;
    private ClinicVo clinicVo;
    private Calendar startTime;
    private Calendar endTime;
    private Boolean statusFlag;
    private Integer phoneStatus;
    private Long size;
    private Boolean deleted;

    public EcgRawDataVo(){}

    public Integer getEcgRawDataId() {
        return ecgRawDataId;
    }

    public void setEcgRawDataId(Integer ecgRawDataId) {
        this.ecgRawDataId = ecgRawDataId;
    }

    public Calendar getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Calendar receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getEcgRawData() {
        return ecgRawData;
    }

    public void setEcgRawData(String ecgRawData) {
        this.ecgRawData = ecgRawData;
    }

    public ClinicVo getClinicVo() {
        return clinicVo;
    }

    public void setClinicVo(ClinicVo clinicVo) {
        this.clinicVo = clinicVo;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Boolean getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Boolean statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Integer getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(Integer phoneStatus) {
        this.phoneStatus = phoneStatus;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
