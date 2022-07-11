package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.EcgRawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

public interface EcgRawDataRepository extends JpaRepository<EcgRawData, Integer> {
    EcgRawData findByEcgRawDataIdAndDeletedFalse(Integer ErdId);

    EcgRawData findByEcgRawDataIdAndClinicIdAndDeletedFalse(Integer ErdId, Integer cId);

    List<EcgRawData> findByEcgTestIdAndClinicIdAndDeletedFalse(Integer etId, Integer clinicId);

    List<EcgRawData> findByStartTimeAndDeletedFalse(Calendar startTime);

    List<EcgRawData> findByReceivedTimeBeforeAndStatusFlagAndDeletedFalse(Calendar receivedTime, Boolean statusFlag);

    List<EcgRawData> findByReceivedTimeAfterAndEcgTestIdAndDeletedFalse(Calendar receivedTime, Integer testId);

    @Modifying(clearAutomatically = true)
    @Query("update EcgRawData rawData set rawData.ecgRawData =:newData where rawData.ecgRawDataId =:rawDataId")
    void updateRawData(@Param("newData") String file, @Param("rawDataId") Integer dId);

    @Transactional
    Long deleteByEcgRawDataId(Integer rdId);

}
