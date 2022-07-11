package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.Clinics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClinicRepository extends JpaRepository<Clinics, Integer> {
    Clinics findByClinicNameAndDeletedFalse(String cName);

    Clinics findByClinicIdAndDeletedFalse(Integer clinicId);
}
