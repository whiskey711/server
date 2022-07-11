package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {
    VerificationCode findByContent(String Content);
}
