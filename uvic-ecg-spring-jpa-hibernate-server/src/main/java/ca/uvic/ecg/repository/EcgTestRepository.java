package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.EcgTest;
import ca.uvic.ecg.model.StatusTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;
@Repository
public interface EcgTestRepository extends JpaRepository<EcgTest, Integer> {
    List<EcgTest> findByPatientIdAndClinicIdAndDeletedFalse(Integer Pid, Integer clinicId);

    EcgTest findByEcgTestIdAndClinicIdAndDeletedFalse(Integer ETid, Integer clinicId);

    EcgTest findByDeviceIdAndDeletedFalse(Integer DeviceId);

    List<EcgTest> findByClinicIdAndDeletedFalse(Integer clinicId);

    EcgTest findByEcgTestIdAndDeletedFalse(Integer testId);

    List<EcgTest> findByStartTimeBeforeAndDeletedFalse(Calendar now);

    List<EcgTest> findByStartTimeAfterAndActualEndTimeBeforeAndClinicIdAndDeletedFalse(Calendar start, Calendar end, Integer cId);

    EcgTest findByDeviceIdAndStartTimeBeforeAndScheduledEndTimeAfterAndClinicIdAndDeletedFalse(Integer DeviceId, Calendar now,
                                                                                               Calendar now2, Integer clinicId);


    EcgTest findByStartTimeAndDeviceIdAndDeletedFalse(Calendar time, Integer deviceId);

    List<EcgTest> findByStatusAndDeletedFalse(StatusTypes.StatusType status);

    List<EcgTest> findByStatusAndDeletedFalseOrStatusAndDeletedFalse(StatusTypes.StatusType status, StatusTypes.StatusType status2);

    List<EcgTest> findByStatusAndClinicIdAndDeletedFalse(StatusTypes.StatusType status, Integer clinicId);

}
