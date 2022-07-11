package ca.uvic.ecg.model.vo;

import ca.uvic.ecg.model.PatientInfo;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PatientVo {
    private Integer patientId;
    private String patientLastName;
    private String patientMidName;
    private String patientFirstName;
    private LocalDate birthdate;
    private String address1;
    private String address2;
    private String province;
    private String city;
    private String email;
    private String phn;
    private String phoneNumber;
    private String workNumber;
    private String homeNumber;
    private String gender;
    private String postCode;
    private String pacemaker;
    private String superPhysician;
    private String weight;
    private String height;
    private String indications;
    private String medications;
    private String referPhysician;
    private String remark;
    private String age;

    public PatientInfo createNewPatientInfo(){
        //should check for null?
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatientLastName(this.patientLastName);
        patientInfo.setPatientMidName(this.patientMidName);
        patientInfo.setPatientFirstName(this.patientFirstName);
        patientInfo.setBirthday(this.birthdate);
        patientInfo.setAddress1(this.address1);
        patientInfo.setAddress2(this.address2);
        patientInfo.setProvince(this.province);
        patientInfo.setCity(this.city);
        patientInfo.setEmail(this.email);
        patientInfo.setPhn(this.phn);
        patientInfo.setPhoneNumber(this.phoneNumber);
        patientInfo.setWorkNumber(this.workNumber);
        patientInfo.setHomeNumber(this.homeNumber);
        patientInfo.setGender(this.gender);
        patientInfo.setPostCode(this.postCode);
        patientInfo.setPacemaker(this.pacemaker);
        patientInfo.setSuperPhysician(this.superPhysician);
        patientInfo.setWeight(this.weight);
        patientInfo.setHeight(this.height);
        patientInfo.setIndications(this.indications);
        patientInfo.setMedications(this.medications);
        patientInfo.setReferPhysician(this.referPhysician);
        patientInfo.setRemark(this.remark);
        patientInfo.setAge(this.age);
        patientInfo.setDeleted(false);
        return patientInfo;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getPatientMidName() {
        return patientMidName;
    }

    public void setPatientMidName(String patientMidName) {
        this.patientMidName = patientMidName;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPacemaker() {
        return pacemaker;
    }

    public void setPacemaker(String pacemaker) {
        this.pacemaker = pacemaker;
    }

    public String getSuperPhysician() {
        return superPhysician;
    }

    public void setSuperPhysician(String superPhysician) {
        this.superPhysician = superPhysician;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getReferPhysician() {
        return referPhysician;
    }

    public void setReferPhysician(String referPhysician) {
        this.referPhysician = referPhysician;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
