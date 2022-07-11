package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.Remarks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemarksRepository extends JpaRepository<Remarks, Integer> {
    //List<Remarks> findByPatientIdAndClinicId(Integer pid);
    Remarks findByRemarkId(Integer rid);
}
