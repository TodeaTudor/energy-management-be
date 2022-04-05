package ehome.backend.services;

import ehome.backend.dtos.DeviceConsumptionDTO;
import ehome.backend.dtos.DeviceDTO;
import ehome.backend.dtos.MeasurementDTO;
import ehome.backend.entities.Device;
import ehome.backend.entities.Measurement;
import ehome.backend.repositories.DeviceRepository;
import ehome.backend.repositories.MeasurementRepository;
import ehome.backend.utils.exceptions.DeviceNotFoundException;
import ehome.backend.utils.mappers.DeviceToDeviceConsumptionDTOMapper;
import ehome.backend.utils.mappers.DtoToEntityMappers;
import ehome.backend.utils.mappers.EntityToDtoMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final MeasurementRepository measurementRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, MeasurementRepository measurementRepository) {
        this.deviceRepository = deviceRepository;
        this.measurementRepository = measurementRepository;
    }

    public List<DeviceDTO> findAll() {
        return deviceRepository.findAll().stream()
                .map(EntityToDtoMappers::fromEntity)
                .collect(Collectors.toList());
    }

    public void createDevice(DeviceDTO deviceDTO) {
        Device device = DtoToEntityMappers.toEntity(deviceDTO);
        deviceRepository.save(device);

    }

    public void updateDevice(DeviceDTO deviceDTO) throws DeviceNotFoundException {
        Device updated = DtoToEntityMappers.toEntity(deviceDTO);
        Device toBeEdited = deviceRepository.findById(deviceDTO.getId()).orElse(null);

        if (toBeEdited == null) {
            throw new DeviceNotFoundException("Device does not exist");
        } else {
            updated.setMeasurements(toBeEdited.getMeasurements());
            updated.setClient(toBeEdited.getClient());
            deviceRepository.save(updated);
        }
    }

    public void deleteDevice(Long id) throws DeviceNotFoundException {
        Device toBeDeleted = deviceRepository.findById(id).orElse(null);
        if (toBeDeleted == null) {
            throw new DeviceNotFoundException("Device does not exist");
        } else {
            List<Measurement> measurements = measurementRepository.findAllByDevice(toBeDeleted);
            for (Measurement measurement : measurements) {
                measurementRepository.delete(measurement);
            }
            deviceRepository.delete(toBeDeleted);
        }
    }

    public void deleteDeviceByClient(Long id) throws DeviceNotFoundException {

        deviceRepository.deleteAllByClientId(id);
    }

    public List<DeviceDTO> findAllSensorless() {
        return deviceRepository.findAll().stream()
                .filter(x -> x.getSensorName() == null)
                .map(EntityToDtoMappers::fromEntity)
                .collect(Collectors.toList());
    }


    public List<DeviceDTO> findAllClientless() {
        return deviceRepository.findAll().stream()
                .filter(x -> x.getClient() == null)
                .map(EntityToDtoMappers::fromEntity)
                .collect(Collectors.toList());
    }

    public List<DeviceConsumptionDTO> getDevicesOfClient(String username) {
        List<DeviceDTO> devices = deviceRepository.findAll()
                .stream()
                .filter(x -> x.getClient() != null)
                .filter(x -> x.getClient().getUsername().equals(username))
                .map(EntityToDtoMappers::fromEntity)
                .collect(Collectors.toList());
        List<DeviceConsumptionDTO> toReturn = new ArrayList<>();
        for (DeviceDTO deviceDTO : devices) {
            float consumption = 0;
            if (deviceDTO.getSensorName() != null) {
                consumption = getConsumptionOfDevice(deviceDTO);
            }
            DeviceConsumptionDTO deviceConsumptionDTO = DeviceToDeviceConsumptionDTOMapper.createMapping(deviceDTO);
            deviceConsumptionDTO.setConsumption(consumption);
            toReturn.add(deviceConsumptionDTO);
        }
        return toReturn;
    }

    public Float getConsumptionOfDevice(DeviceDTO device) {
        Comparator<Measurement> comparator = Comparator.comparing(Measurement::getTimeStamp);
        Optional<Measurement> maxMeasurement = measurementRepository.findAll()
                .stream()
                .filter(x -> x.getDevice().getId() != null)
                .filter(x -> x.getDevice().getId().equals(device.getId()))
                .filter(x -> x.getTimeStamp() != null)
                .max(comparator);

        if (maxMeasurement.isPresent()) {
            return maxMeasurement.get().getConsumption();
        } else {
            return (float) 0;
        }


    }
    

    public List<MeasurementDTO> getHistoricalConsumption(Long deviceId) {
        return measurementRepository.findAll()
                .stream()
                .filter(x -> x.getDevice().getId() != null)
                .filter(x -> x.getDevice().getId() == deviceId)
                .map(EntityToDtoMappers::fromEntity)
                .collect(Collectors.toList());
    }
}
