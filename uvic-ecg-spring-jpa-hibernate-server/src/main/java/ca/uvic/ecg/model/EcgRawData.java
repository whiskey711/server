package ca.uvic.ecg.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Table(name = "ecg_raw_data", catalog = "ecg")
public class EcgRawData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecg_raw_data_id", nullable = false)
    private Integer ecgRawDataId;
    @Column(name = "ecg_test_id")
    private Integer ecgTestId;
    @Column(name = "received_time")
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    private Calendar receivedTime;
    @Column(name = "ecg_raw_data")
    private String ecgRawData;
    @Column(name = "deleted")
    private Boolean deleted;
    @Column(name = "clinic_id", nullable = false)
    private Integer clinicId;
    @Column(name = "start_time")
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    private Calendar startTime;
    @Column(name = "end_time")
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    private Calendar endTime;
    @Column(name = "status_flag")
    private Boolean statusFlag;
    @Column(name ="phone_status")
    private Integer phoneStatus;
    @Column(name = "size")
    private Long size;

    public EcgRawData() {

    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(Integer phoneStatus) {
        this.phoneStatus = phoneStatus;
    }

    public Integer getEcgRawDataId() {
        return ecgRawDataId;
    }

    public void setEcgRawDataId(Integer ecgRawDataId) {
        this.ecgRawDataId = ecgRawDataId;
    }

    public Integer getEcgTestId() {
        return ecgTestId;
    }

    public void setEcgTestId(Integer ecgTestId) {
        this.ecgTestId = ecgTestId;
    }

    public String getEcgRawData() {
        return ecgRawData;
    }

    public void setEcgRawData(String ecgRawData) {
        this.ecgRawData = ecgRawData;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Boolean getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Boolean statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Calendar getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Calendar receivedTime) {
        this.receivedTime = receivedTime;
    }


    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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
}
