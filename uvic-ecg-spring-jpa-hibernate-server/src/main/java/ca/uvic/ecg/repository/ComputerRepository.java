package ca.uvic.ecg.repository;

import ca.uvic.ecg.model.Computer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComputerRepository extends JpaRepository<Computer, Integer> {

    Computer findByComputerIdAndClinicId(Integer cId,Integer clinicId);

    Computer findByMacAddressesLike(String macAddress);
}
