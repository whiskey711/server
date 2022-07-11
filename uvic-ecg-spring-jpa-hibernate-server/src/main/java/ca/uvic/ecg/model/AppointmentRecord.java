package ca.uvic.ecg.model;

import ca.uvic.ecg.model.vo.*;

import javax.persistence.*;
import java.util.Calendar;


@Entity
@Table(name = "appointment_records", catalog = "ecg")
public class AppointmentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_record_id", nullable = false)
    public Integer appointmentRecordId;
    @Column(name = "patient_id", nullable = false)
    public Integer patientId;
    @Column(name = "device_id", nullable = false)
    public Integer deviceId;
    @Column(name = "nurse_id", nullable = false)
    public Integer nurseId;
    @Column(name = "appointment_start_time")
    public Calendar appointmentStartTime;
    @Column(name = "appointment_end_time")
    public Calendar appointmentEndTime;
    @Column(name = "reservation_time")
    public Calendar reservationTime;
    @Column(name = "device_pickup_date")
    public Calendar pickupDate;
    @Column(name = "device_return_date")
    public Calendar deviceReturnDate;
    @Column(name = "device_actual_return_time")
    public Calendar deviceActualReturnTime;
    @Column(name = "instruction")
    public String instruction;
    @Column(name = "deleted")
    public Boolean deleted;
    @Column(name = "clinic_id", nullable = false)
    public Integer clinicId;
    @Column(name = "ecg_test_id")
    public Integer ecgTestId;
    @Column(name = "delay_device_return_time")
    public Calendar delayDeviceReturnTime;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "device_Id", referencedColumnName = "device_Id", insertable = false, updatable = false)
    private Device device;

    public AppointmentRecord() {
    }

    public Calendar getDelayDeviceReturnTime() {
        return delayDeviceReturnTime;
    }

    public void setDelayDeviceReturnTime(Calendar delayDeviceReturnTime) {
        this.delayDeviceReturnTime = delayDeviceReturnTime;
    }

    public Device getDevice() {
        return device;
    }

    public Calendar getDeviceActualReturnTime() {
        return deviceActualReturnTime;
    }

    public void setDeviceActualReturnTime(Calendar deviceActualReturnDate) {
        this.deviceActualReturnTime = deviceActualReturnDate;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Integer getEcgTestId() {
        return ecgTestId;
    }

    public void setEcgTestId(Integer ecgTestId) {
        this.ecgTestId = ecgTestId;
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

    public Integer getAppointmentRecordId() {
        return appointmentRecordId;
    }

    public void setAppointmentRecordId(Integer appointmentRecordId) {
        this.appointmentRecordId = appointmentRecordId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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

    public Calendar getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Calendar reservationTime) {
        this.reservationTime = reservationTime;
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

    public void update(AppointmentVo newRecord) {
        if(newRecord.getPatientVo().getPatientId()!=null){
            patientId = newRecord.getPatientVo().getPatientId();
        }
        if(newRecord.getDevicesVo().getDeviceId()!=null){
            deviceId = newRecord.getDevicesVo().getDeviceId();
        }
        if(newRecord.getNurseVo().getNurseId()!=null){
            nurseId = newRecord.getNurseVo().getNurseId();
        }
        if(newRecord.getInstruction()!=null){
            instruction = newRecord.getInstruction();
        }
        if(newRecord.getDelayDeviceReturnTime()!=null){
            delayDeviceReturnTime = newRecord.getDelayDeviceReturnTime();
        }
    }

    public AppointmentVo copyToVo(){
        AppointmentVo appointmentVo = new AppointmentVo();
        if(appointmentRecordId!=null){
            appointmentVo.setAppointmentRecordId(appointmentRecordId);
            if(patientId!=null){
                PatientVo patientVo = new PatientVo();
                patientVo.setPatientId(patientId);
                appointmentVo.setPatientVo(patientVo);
            }
            if(deviceId!=null){
                DevicesVo devicesVo = new DevicesVo();
                devicesVo.setDeviceId(deviceId);
                appointmentVo.setDevicesVo(devicesVo);
            }
            if(appointmentEndTime!=null){
                appointmentVo.setAppointmentEndTime(appointmentEndTime);
            }
            if(appointmentStartTime!=null){
                appointmentVo.setAppointmentStartTime(appointmentStartTime);
            }
            if(delayDeviceReturnTime!=null){
                appointmentVo.setDelayDeviceReturnTime(delayDeviceReturnTime);
            }
            if(deviceReturnDate!=null){
                appointmentVo.setDeviceReturnDate(deviceReturnDate);
            }
            if(ecgTestId!=null){
                EcgTestVo ecgTestVo = new EcgTestVo();
                ecgTestVo.setEcgTestId(ecgTestId);
                appointmentVo.setEcgTestVo(ecgTestVo);
            }
            if(pickupDate!=null){
                appointmentVo.setPickupDate(pickupDate);
            }
            if(instruction!=null){
                appointmentVo.setInstruction(instruction);
            }
            if(nurseId!=null){
                NurseVo nurseVo = new NurseVo();
                nurseVo.setNurseId(nurseId);
                appointmentVo.setNurseVo(nurseVo);
            }
            if(deviceActualReturnTime!=null){
                appointmentVo.setDeviceActualReturnTime(deviceActualReturnTime);
            }
        }

        return appointmentVo;
    }
}
