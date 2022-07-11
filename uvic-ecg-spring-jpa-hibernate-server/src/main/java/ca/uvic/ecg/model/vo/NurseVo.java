package ca.uvic.ecg.model.vo;

import ca.uvic.ecg.model.Nurse;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NurseVo {
    private Integer nurseId;
    private String nurseLastName;
    private String nurseMidName;
    private String nurseFirstName;
    private String nursePhoneNumber;
    private String nurseEmail;
    private String password;

    public Nurse createNewNurse(){
        Nurse nurse = new Nurse();
        nurse.setNurseLastName(this.nurseLastName);
        nurse.setNurseMidName(this.nurseMidName);
        nurse.setNurseFirstName(this.nurseFirstName);
        nurse.setNursePhoneNumber(this.nursePhoneNumber);
        nurse.setNurseEmail(this.nurseEmail);
        nurse.setPassword(this.password);
        nurse.setDeleted(false);
        return nurse;
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
