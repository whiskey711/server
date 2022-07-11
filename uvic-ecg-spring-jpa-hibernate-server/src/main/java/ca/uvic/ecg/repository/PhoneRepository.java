package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.Phones;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phones, Integer> {
    Phones findByPhoneMacAndClinicIdAndDeletedFalse(String macAddress, Integer clinicId);

    Phones findByModelAndPhoneNameAndDeletedFalse(String model, String name);
}
