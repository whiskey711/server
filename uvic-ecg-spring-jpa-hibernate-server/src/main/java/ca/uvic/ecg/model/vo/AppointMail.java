package ca.uvic.ecg.model.vo;

public class AppointMail {
    private String patientEmail;
    private String mailContent;

    public AppointMail() {

    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }
}
