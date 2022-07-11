package ca.uvic.ecg.model.vo;
import java.util.List;
import ca.uvic.ecg.model.Computer;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Calendar;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ComputerVo {
    private Integer computerId;
    private List<String> macAddresses;
    private Calendar lastRequestTime;
    private String failedEcgTest;
    private String failedEcgRawData;

    public ComputerVo(){}

    public Computer createComputer(){
        Computer computer = new Computer();
        computer.setMacAddresses(macAddresses);
        if(lastRequestTime !=null){
            computer.setLastRequestTime(lastRequestTime);
        }
        if(failedEcgRawData !=null){
            computer.setFailedEcgRawData(failedEcgRawData);
        }
        if(failedEcgTest !=null){
            computer.setFailedEcgTest(failedEcgTest);
        }
        return computer;
    }


    public Integer getComputerId() {
        return computerId;
    }

    public void setComputerId(Integer computerId) {
        this.computerId = computerId;
    }

    public List<String> getMacAddresses() {
        return macAddresses;
    }

    public void setMacAddresses(List<String> macAddresses) { this.macAddresses = macAddresses;
    }

    public Calendar getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(Calendar lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

    public String getFailedEcgTest() {
        return failedEcgTest;
    }

    public void setFailedEcgTest(String failedEcgTest) {
        this.failedEcgTest = failedEcgTest;
    }

    public String getFailedEcgRawData() {
        return failedEcgRawData;
    }

    public void setFailedEcgRawData(String failedEcgRawData) {
        this.failedEcgRawData = failedEcgRawData;
    }
}
