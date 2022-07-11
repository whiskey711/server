package ca.uvic.ecg.model.vo;

import ca.uvic.ecg.model.AppointmentRecord;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppointmentVo {
    private Integer appointmentRecordId;
    @JsonProperty("patient")
    private PatientVo patientVo;
    @JsonProperty("device")
    private DevicesVo devicesVo;
    @JsonProperty("ecgTest")
    private EcgTestVo ecgTestVo;
    @JsonProperty("nurse")
    private NurseVo nurseVo;
    private Calendar appointmentStartTime;
    private Calendar appointmentEndTime;
    private Calendar pickupDate;
    private Calendar deviceReturnDate;
    private String instruction;
    private Calendar delayDeviceReturnTime;
    private Calendar deviceActualReturnTime;

    public AppointmentVo(){}

    public AppointmentRecord createAppointmentRecord(){
        AppointmentRecord appointmentRecord = new AppointmentRecord();
        appointmentRecord.setPatientId(this.patientVo.getPatientId());
        appointmentRecord.setDeviceId(this.devicesVo.getDeviceId());
        appointmentRecord.setNurseId(this.nurseVo.getNurseId());
        appointmentRecord.setAppointmentStartTime(this.appointmentStartTime);
        appointmentRecord.setAppointmentEndTime(this.appointmentEndTime);
        appointmentRecord.setPickupDate(this.pickupDate);
        appointmentRecord.setDeviceReturnDate(this.deviceReturnDate);
        appointmentRecord.setInstruction(this.instruction);
        appointmentRecord.setDeleted(false);
        return appointmentRecord;
    }

    public Calendar getDelayDeviceReturnTime() {
        return delayDeviceReturnTime;
    }

    public void setDelayDeviceReturnTime(Calendar delayDeviceReturnTime) {
        this.delayDeviceReturnTime = delayDeviceReturnTime;
    }

    public Calendar getDeviceActualReturnTime() {
        return deviceActualReturnTime;
    }

    public void setDeviceActualReturnTime(Calendar deviceActualReturnTime) {
        this.deviceActualReturnTime = deviceActualReturnTime;
    }

    public EcgTestVo getEcgTestVo() {
        return ecgTestVo;
    }

    public void setEcgTestVo(EcgTestVo ecgTestVo) {
        this.ecgTestVo = ecgTestVo;
    }

    public Integer getAppointmentRecordId() {
        return appointmentRecordId;
    }

    public void setAppointmentRecordId(Integer appointmentRecordId) {
        this.appointmentRecordId = appointmentRecordId;
    }

    public PatientVo getPatientVo() {
        return patientVo;
    }

    public void setPatientVo(PatientVo patientVo) {
        this.patientVo = patientVo;
    }

    public DevicesVo getDevicesVo() {
        return devicesVo;
    }

    public void setDevicesVo(DevicesVo devicesVo) {
        this.devicesVo = devicesVo;
    }

    public NurseVo getNurseVo() {
        return nurseVo;
    }

    public void setNurseVo(NurseVo nurseVo) {
        this.nurseVo = nurseVo;
    }

    public Calendar getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(Calendar appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public Calendar getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(Calendar appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public Calendar getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Calendar pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Calendar getDeviceReturnDate() {
        return deviceReturnDate;
    }

    public void setDeviceReturnDate(Calendar deviceReturnDate) {
        this.deviceReturnDate = deviceReturnDate;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

}
