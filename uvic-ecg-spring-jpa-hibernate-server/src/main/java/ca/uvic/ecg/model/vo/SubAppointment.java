package ca.uvic.ecg.model.vo;

import ca.uvic.ecg.model.AppointmentRecord;

public class SubAppointment extends AppointmentRecord {
    private String deviceLocation;
    private String LastName;
    private String FirstName;

    public SubAppointment() {

    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(String deviceLocation) {
        this.deviceLocation = deviceLocation;
    }
}
