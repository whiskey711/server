package ca.uvic.ecg.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


@Entity
@Table(name = "ecg_test", catalog = "ecg")
public class EcgTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecg_test_id", nullable = false)
    private Integer ecgTestId;
    @Column(name = "actual_start_time")
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    private Calendar startTime;
    @Column(name = "scheduled_end_time")
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    private Calendar scheduledEndTime;
    @Column(name = "actual_end_time")
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    private Calendar actualEndTime;
    @Column(name = "patient_id")
    private Integer patientId;
    @Column(name = "nurse_id")
    private Integer nurseId;
    @Column(name = "phone_id")
    private Integer phoneId;
    @Column(name = "device_id")
    private Integer deviceId;
    @Column(name = "clinic_id", nullable = false)
    private Integer clinicId;
    @Column(name = "comment")
    private String comment;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private StatusTypes.StatusType status;
    @Column(name = "restart_time")
    private Calendar restartTime;


    public EcgTest() {

    }

    public Calendar getRestartTime() {
        return restartTime;
    }

    public void setRestartTime(Calendar restartTime) {
        this.restartTime = restartTime;
    }


    public StatusTypes.StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusTypes.StatusType input) {
        this.status = input;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

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

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public void updateComment(String comment) {
        if (this.comment == null) {
            this.comment = comment;
        } else {
            this.comment = this.comment.concat("\n").concat(comment);
        }
    }
}
