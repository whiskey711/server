package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.Nurse;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NurseRepository extends JpaRepository<Nurse, Integer> {
    Nurse findByNurseEmailAndClinicIdAndDeletedFalse(String username, Integer clinicId);

    Nurse findByPasswordAndDeletedFalse(String pass);

    List<Nurse> findByClinicIdAndDeletedFalse(int cid);

    Nurse findByNurseEmailAndDeletedFalse(String email);

    Nurse findByNurseIdAndDeletedFalse(Integer nId);

}
