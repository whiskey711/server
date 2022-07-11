package ca.uvic.ecg.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClinicVo {
    private String address;
    private String phone;
    private String email;
    private String fax;
    private String clinicName;
    private String mailTemplate;

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

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(String mailTemplate) {
        this.mailTemplate = mailTemplate;
    }
}
