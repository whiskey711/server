package ca.uvic.ecg.model;

import java.util.Calendar;

public class PhoneReturn {
    private String deviceMacAddress;
    private Calendar returnDate;

    public PhoneReturn() {

    }

    public String getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(String deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    public Calendar getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Calendar returnDate) {
        this.returnDate = returnDate;
    }
}
