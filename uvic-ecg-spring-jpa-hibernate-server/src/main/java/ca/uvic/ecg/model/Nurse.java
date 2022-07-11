package ca.uvic.ecg.model;


import javax.persistence.*;
import java.util.Calendar;


@Entity
@Table(name = "nurses", catalog = "ecg")
public class Nurse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nurse_id", nullable = false)
    private Integer nurseId;
    @Column(name = "nurse_last_name", nullable = false, length = 45)
    private String nurseLastName;
    @Column(name = "nurse_mid_name", length = 45)
    private String nurseMidName;
    @Column(name = "nurse_first_name", length = 45)
    private String nurseFirstName;
    @Column(name = "nurse_phone_number", length = 20)
    private String nursePhoneNumber;
    @Column(name = "nurse_email", length = 51)
    private String nurseEmail;
    @Column(name = "clinic_id", nullable = false)
    private Integer clinicId;
    @Column(name = "password", length = 30)
    private String password;
    @Column(name = "deleted")
    private Boolean deleted;
    @Column(name = "number_of_failures")
    private Integer numberOfFailures;
    @Column(name = "next_request")
    private Calendar nextRequest;

    public Nurse() {

    }

    public Integer getNumberOfFailures() {
        return numberOfFailures;
    }

    public void setNumberOfFailures(Integer failTime) {
        this.numberOfFailures = failTime;
    }

    public Calendar getNextRequest() {
        return nextRequest;
    }

    public void setNextRequest(Calendar nextRequest) {
        this.nextRequest = nextRequest;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public String getNurseLastName() {
        return nurseLastName;
    }

    public void setNurseLastName(String nurseLastName) {
        this.nurseLastName = nurseLastName;
    }

    public String getNurseMidName() {
        return nurseMidName;
    }

    public void setNurseMidName(String nurseMidName) {
        this.nurseMidName = nurseMidName;
    }

    public String getNurseFirstName() {
        return nurseFirstName;
    }

    public void setNurseFirstName(String nurseFirstName) {
        this.nurseFirstName = nurseFirstName;
    }

    public String getNursePhoneNumber() {
        return nursePhoneNumber;
    }

    public void setNursePhoneNumber(String nursePhoneNumber) {
        this.nursePhoneNumber = nursePhoneNumber;
    }

    public String getNurseEmail() {
        return nurseEmail;
    }

    public void setNurseEmail(String nurseEmail) {
        this.nurseEmail = nurseEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
