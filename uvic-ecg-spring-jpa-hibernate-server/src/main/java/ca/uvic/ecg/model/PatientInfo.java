package ca.uvic.ecg.model;

import ca.uvic.ecg.model.vo.PatientVo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "patient_information", catalog = "ecg")
public class PatientInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id", nullable = false)
    private Integer patientId;
    @Column(name = "patient_last_name", nullable = false, length = 45)
    private String patientLastName;
    @Column(name = "patient_mid_name", length = 45)
    private String patientMidName;
    @Column(name = "patient_first_name", length = 45, nullable = false)
    private String patientFirstName;
    @Column(name = "birthdate", nullable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthdate;
    @Column(name = "address1", nullable = false, length = 50)
    private String address1;
    @Column(name = "address2", length = 50)
    private String address2;
    @Column(name = "province", length = 5, nullable = false)
    private String province;
    @Column(name = "city", length = 35, nullable = false)
    private String city;
    @Column(name = "email", length = 51)
    private String email;
    @Column(name = "phn", length = 25)
    private String phn;
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    @Column(name = "work_number", length = 20)
    private String workNumber;
    @Column(name = "home_number", length = 20)
    private String homeNumber;
    @Column(name = "gender", length = 20, nullable = false)
    private String gender;
    @Column(name = "post_code", length = 10)
    private String postCode;
    @Column(name = "deleted")
    private Boolean deleted;
    @Column(name = "clinic_id", nullable = false)
    private Integer clinicId;
    @Column(name = "pacemaker", length = 45)
    private String pacemaker;
    @Column(name = "supervising_physician", length = 45)
    private String superPhysician;
    @Column(name = "weight", length = 45)
    private String weight;
    @Column(name = "height", length = 45)
    private String height;
    @Column(name = "indications", length = 45)
    private String indications;
    @Column(name = "medications", length = 45)
    private String medications;
    @Column(name = "referring_physician", length = 45)
    private String referPhysician;
    @Column(name = "remark", length = 55)
    private String remark;
    @Column(name = "age")
    private String age;


    public void update(PatientVo voAddPatient) {
        if(voAddPatient.getPatientLastName()!=null){
            this.patientLastName = voAddPatient.getPatientLastName();
        }
        if(voAddPatient.getPatientMidName()!=null){
            this.patientMidName = voAddPatient.getPatientMidName();
        }
        if(voAddPatient.getPatientFirstName()!=null){
            this.patientFirstName = voAddPatient.getPatientFirstName();
        }
        if(voAddPatient.getBirthdate()!=null){
            this.birthdate = voAddPatient.getBirthdate();
        }
        if(voAddPatient.getAddress1()!=null){
            this.address1 = voAddPatient.getAddress1();
        }
        if(voAddPatient.getAddress2()!=null){
            this.address2 = voAddPatient.getAddress2();
        }
        if(voAddPatient.getProvince()!=null){
            this.province = voAddPatient.getProvince();
        }
        if(voAddPatient.getCity()!=null){
            this.city = voAddPatient.getCity();
        }
        if(voAddPatient.getEmail()!=null){
            this.email = voAddPatient.getEmail();
        }
        if(voAddPatient.getPhn()!=null){
            this.phn = voAddPatient.getPhn();
        }
        if(voAddPatient.getPhoneNumber()!=null){
            this.phoneNumber = voAddPatient.getPhoneNumber();
        }
        if(voAddPatient.getWorkNumber()!=null){
            this.workNumber = voAddPatient.getWorkNumber();
        }
        if(voAddPatient.getHomeNumber()!=null){
            this.homeNumber = voAddPatient.getHomeNumber();
        }
        if(voAddPatient.getGender()!=null){
            this.gender = voAddPatient.getGender();
        }
        if(voAddPatient.getPostCode()!=null) {
            this.postCode = voAddPatient.getPostCode();
        }
        if(voAddPatient.getPacemaker()!=null){
            this.pacemaker = voAddPatient.getPacemaker();
        }
        if(voAddPatient.getSuperPhysician()!=null){
            this.superPhysician = voAddPatient.getSuperPhysician();
        }
        if(voAddPatient.getWeight()!=null){
            this.weight = voAddPatient.getWeight();
        }
        if(voAddPatient.getHeight()!=null){
            this.height = voAddPatient.getHeight();
        }
        if(voAddPatient.getIndications()!=null){
            this.indications = voAddPatient.getIndications();
        }
        if(voAddPatient.getMedications()!=null){
            this.medications = voAddPatient.getMedications();
        }
        if(voAddPatient.getReferPhysician()!=null){
            this.referPhysician = voAddPatient.getReferPhysician();
        }
        if(voAddPatient.getRemark()!=null){
            this.remark = voAddPatient.getRemark();
        }
        if(voAddPatient.getAge()!=null){
            this.age = voAddPatient.getAge();
        }
    }

    public PatientInfo() {

    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public void setBirthday(LocalDate birthday) {
        this.birthdate = birthday;
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
}
