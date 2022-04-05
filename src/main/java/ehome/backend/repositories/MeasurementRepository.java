package ehome.backend.repositories;

import ehome.backend.entities.Device;
import ehome.backend.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findAllByDevice(Device device);
}
