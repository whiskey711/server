package ca.uvic.ecg.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.*;

@Entity
@Table(name = "computer", catalog = "ecg")
public class Computer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "computer_Id", nullable = false)
    private Integer computerId;
    @Column(name = "mac_addresses", nullable = false)
    private String macAddresses;
    @Column(name = "last_request_time")
    private Calendar lastRequestTime;
    @Column(name = "failed_ecg_test")
    private String failedEcgTest;
    @Column(name = "failed_ecg_raw_data")
    private String failedEcgRawData;
    @Column(name = "clinic_Id", nullable = false)
    private Integer clinicId;


    public Computer(){}

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getComputerId() {
        return computerId;
    }

    public void setComputerId(Integer computerId) {
        this.computerId = computerId;
    }

    public String getMacAddresses() {
        return macAddresses;
    }

    public void setMacAddresses(String macAddresses) {
        this.macAddresses = macAddresses;
    }

    public void setMacAddresses(List<String> macAddresses) {
        String result = String.join(",",macAddresses);
        this.macAddresses = result;
    }

    public Calendar getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(Calendar last_request_time) {
        this.lastRequestTime = last_request_time;
    }

    public String getFailedEcgTest() {
        return failedEcgTest;
    }

    public void setFailedEcgTest(String failed_ecg_test) {
        this.failedEcgTest = failed_ecg_test;
    }

    public String getFailedEcgRawData() {
        return failedEcgRawData;
    }

    public void setFailedEcgRawData(String failed_ecg_raw_data) {
        this.failedEcgRawData = failed_ecg_raw_data;
    }
}


