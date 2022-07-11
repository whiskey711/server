package ca.uvic.ecg.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "phones", catalog = "ecg")
public class Phones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_id", nullable = false)
    private Integer phoneId;
    @Column(name = "phone_name", nullable = false, length = 45)
    private String phoneName;
    @Column(name = "phone_mac", length = 30)
    private String phoneMac;
    @Column(name = "deleted")
    private Boolean deleted;
    @Column(name = "clinic_id", nullable = false)
    private Integer clinicId;
    @Column(name = "model", length = 25)
    private String model;

    public Phones() {

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneMac() {
        return phoneMac;
    }

    public void setPhoneMac(String phoneMac) {
        this.phoneMac = phoneMac;
    }
}
