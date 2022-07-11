package ca.uvic.ecg.model.vo;

import ca.uvic.ecg.model.Device;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DevicesVo {
    private Integer deviceId;
    private Integer phoneId;
    private String deviceName;
    private String deviceMacAddress;
    private String deviceLocation;

    public Device createNewDevice(){
        Device device = new Device();
        device.setPhoneId(this.phoneId);
        device.setDeviceName(this.deviceName);
        device.setDeviceMacAddress(this.deviceMacAddress);
        device.setDeviceLocation(this.deviceLocation);
        device.setDeleted(false);
        device.setOccupied(false);
        return device;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(String deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    public String getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(String deviceLocation) {
        this.deviceLocation = deviceLocation;
    }
}
