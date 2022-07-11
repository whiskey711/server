package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.VerificationCode2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCode2Repository extends JpaRepository<VerificationCode2, Integer> {
    VerificationCode2 findByContent(String Content);
}
