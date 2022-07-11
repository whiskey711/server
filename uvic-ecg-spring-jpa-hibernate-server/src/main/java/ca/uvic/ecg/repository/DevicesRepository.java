package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevicesRepository extends JpaRepository<Device, Integer> {
    Device findByDeviceIdAndClinicIdAndDeletedFalse(Integer Did, Integer clinicId);

    Device findByDeviceNameAndClinicIdAndDeletedFalse(String DName, Integer clinicId);

    Device findByDeviceMacAddressAndClinicIdAndDeletedFalse(String macAdd, Integer clinicId);

    List<Device> findByClinicIdAndDeletedFalse(Integer cId);

    List<Device> findByDeviceLocationAndDeletedFalseAndClinicId(String location, Integer clinicId);

    //@Query("select dev from Devices dev where dev.appointmentRecord.appointmentStartTime >= :startTime")
    //List<Devices> findstartTimeAfter(@Param("startTime") Date startTime);

}

