package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.PatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PatientInfoRepository extends JpaRepository<PatientInfo, Integer> {
    List<PatientInfo> findByPatientLastNameAndBirthdateAndPhnAndClinicIdAndDeletedFalse(String Lastname, LocalDate birthday, String phn, Integer clinicId);

    PatientInfo findByPatientIdAndClinicIdAndDeletedFalse(Integer pid, Integer clinicId);

    List<PatientInfo> findByPatientLastNameAndClinicIdAndDeletedFalse(String Lastname, Integer clinicId);

    List<PatientInfo> findByBirthdateAndClinicIdAndDeletedFalse(LocalDate birthday, Integer clinicId);

    List<PatientInfo> findByPhnAndClinicIdAndDeletedFalse(String phn, Integer clinicId);

    List<PatientInfo> findByPatientLastNameAndBirthdateAndClinicIdAndDeletedFalse(String Lastname, LocalDate birthday, Integer clinicId);

    List<PatientInfo> findByPatientLastNameAndPhnAndClinicIdAndDeletedFalse(String Lastname, String phn, Integer clinicId);

    List<PatientInfo> findByBirthdateAndPhnAndClinicIdAndDeletedFalse(LocalDate birthday, String phn, Integer clinicId);

    List<PatientInfo> findByPatientFirstNameAndClinicIdAndDeletedFalse(String Firstname, Integer clinicId);

    List<PatientInfo> findByPatientLastNameAndPatientFirstNameAndClinicIdAndDeletedFalse(String Lastname, String Firstname, Integer clinicId);

    List<PatientInfo> findByPatientFirstNameAndPhnAndClinicIdAndDeletedFalse(String firstname, String phn, Integer clinicId);

    List<PatientInfo> findByPatientFirstNameAndBirthdateAndClinicIdAndDeletedFalse(String firstname, LocalDate birthday, Integer clinicId);

    List<PatientInfo> findByPatientFirstNameAndBirthdateAndPhnAndClinicIdAndDeletedFalse(String firstname, LocalDate birthday, String phn, Integer clinicId);

    List<PatientInfo> findByPatientFirstNameAndBirthdateAndPatientLastNameAndClinicIdAndDeletedFalse(String firstname,
                                                                                                     LocalDate birthday, String lastname, Integer clinicId);

    List<PatientInfo> findByPatientLastNameAndBirthdateAndPatientFirstNameAndClinicIdAndDeletedFalse(String LastName, LocalDate birthday,
                                                                                                     String firstname, Integer clinicId);

    List<PatientInfo> findByPatientLastNameAndBirthdateAndPhnAndPatientFirstNameAndClinicIdAndDeletedFalse(String LastName, LocalDate birthday,
                                                                                                           String phn, String firstname, Integer clinicId);
}
