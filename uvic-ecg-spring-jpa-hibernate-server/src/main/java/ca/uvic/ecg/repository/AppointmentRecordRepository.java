package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.AppointmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;

public interface AppointmentRecordRepository extends JpaRepository<AppointmentRecord, Integer> {
    List<AppointmentRecord> findByClinicId(Integer clinicId);

    AppointmentRecord findByAppointmentRecordIdAndDeleted(Integer aId,boolean deleted);

    AppointmentRecord findByEcgTestIdAndDeleted(Integer ecgTestId,Boolean deleted);

    AppointmentRecord findByAppointmentStartTimeAndDeviceIdAndClinicIdAndDeleted(Calendar startTime, Integer deviceId, Integer clinicId,Boolean deleted);

    List<AppointmentRecord> findByDeviceIdAndClinicIdAndDeleted(Integer deviceId, Integer ClinicId,Boolean deleted);

    List<AppointmentRecord> findByPatientIdAndClinicIdAndDeletedFalse(Integer deviceId, Integer clinicId);

    List<AppointmentRecord> findByPickupDateAfterAndDeviceReturnDateBeforeAndDeviceIdAndDeletedFalse(
            Calendar pickUpDate, Calendar returnDate, Integer dId);

    List<AppointmentRecord> findByPickupDateBeforeAndDeviceReturnDateAfterAndDeviceIdAndClinicIdAndAppointmentRecordIdNotAndDeletedFalse(
            Calendar pickUpDate, Calendar returnDate, Integer dId,Integer cId,Integer apId);

    AppointmentRecord findByPickupDateBeforeAndDeviceReturnDateAfterAndDeviceIdAndClinicIdAndDeletedFalse(
            Calendar pickUpDate, Calendar returnDate, Integer dId,Integer cId);

    AppointmentRecord findByAppointmentRecordIdAndClinicIdAndDeletedFalse(Integer apId, Integer clinicId);

    AppointmentRecord findByPickupDateBeforeAndDeviceReturnDateAfterAndDeviceIdAndDeletedFalse(
            Calendar pickUpDate, Calendar returnDate, Integer dId);

    List<AppointmentRecord> findByDeviceActualReturnTimeIsNullAndDeviceReturnDateAfter(Calendar nowtime);

    List<AppointmentRecord> findByAppointmentStartTimeBeforeAndDeletedFalseAndDeviceActualReturnTimeIsNull(Calendar nowtime);

    List<AppointmentRecord> findByDeviceIdAndAppointmentStartTimeAfterAndAppointmentEndTimeBeforeAndDeletedFalse(Integer did, Calendar startTime, Calendar endTime);
    List<AppointmentRecord> findByAppointmentStartTimeAfterAndAppointmentEndTimeBeforeAndDeletedFalse(Calendar startTime, Calendar endtime);

    List<AppointmentRecord> findByDeviceIdAndAppointmentStartTimeBeforeAndDeviceActualReturnTimeIsNull(Integer did, Calendar nowTime);

}