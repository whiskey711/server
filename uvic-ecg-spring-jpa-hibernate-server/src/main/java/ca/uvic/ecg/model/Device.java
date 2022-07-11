package ca.uvic.ecg.model;

import javax.persistence.*;


@Entity
@Table(name = "devices", catalog = "ecg")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id", nullable = false)
    private Integer deviceId;
    @Column(name = "phone_id", nullable = false)
    private Integer phoneId;
    @Column(name = "device_name", nullable = false, length = 45)
    private String deviceName;
    @Column(name = "occupied")
    private Boolean occupied;
    @Column(name = "deleted")
    private Boolean deleted;
    @Column(name = "clinic_id", length = 25)
    private Integer clinicId;
    @Column(name = "device_mac_address")
    private String deviceMacAddress;
    @Column(name = "device_location")
    private String deviceLocation;

    public Device() {

    }

    public String getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(String deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(String deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
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
}
