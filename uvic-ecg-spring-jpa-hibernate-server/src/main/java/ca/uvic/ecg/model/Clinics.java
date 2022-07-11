package ca.uvic.ecg.model;

import javax.persistence.*;

@Entity
@Table(name = "clinics", catalog = "ecg")
public class Clinics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinic_id", nullable = false)
    private Integer clinicId;
    @Column(name = "address", length = 65)
    private String address;
    @Column(name = "phone", length = 15)
    private String phone;
    @Column(name = "email", length = 15)
    private String email;
    @Column(name = "fax", length = 15)
    private String fax;
    @Column(name = "clinic_name", nullable = false, length = 25)
    private String clinicName;
    @Column(name = "deleted")
    private Boolean deleted;
    @Column(name = "mail_template")
    private String mailTemplate;

    public Clinics() {

    }

    public String getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(String mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }
}
