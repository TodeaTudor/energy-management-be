package ehome.backend.repositories;

import java.util.List;

import ehome.backend.entities.Client;
import ehome.backend.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    void deleteAllByClientId(Long id);
    List<Device> findAllByClient(Client client);
}
