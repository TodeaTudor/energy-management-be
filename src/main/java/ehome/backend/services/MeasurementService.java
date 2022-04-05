package ehome.backend.services;

import ehome.backend.dtos.QueueMeasurementDTO;
import ehome.backend.dtos.WarningMessageDTO;
import ehome.backend.entities.Measurement;
import ehome.backend.repositories.DeviceRepository;
import ehome.backend.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationService notificationService;
    private HashMap<Long, Measurement> previousMeasurements = new HashMap<>();

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, DeviceRepository deviceRepository, NotificationService notificationService) {
        this.measurementRepository = measurementRepository;
        this.deviceRepository = deviceRepository;
        this.notificationService = notificationService;
    }

    private Measurement convertQueueMeasurementToMeasurement(QueueMeasurementDTO queueMeasurementDTO) {
        return Measurement.builder()
                .consumption(queueMeasurementDTO.getValue())
                .timeStamp(queueMeasurementDTO.getTimestampAsDate())
                .device(deviceRepository.findById(queueMeasurementDTO.getDeviceId()).orElse(null))
                .build();
    }


    public void saveMeasurement(QueueMeasurementDTO queueMeasurementDTO) {
        Measurement measurement = convertQueueMeasurementToMeasurement(queueMeasurementDTO);
        if (measurement.getDevice() != null) {
            if (previousMeasurements.get(measurement.getDevice().getId()) != null) {
                float energyPeak = measurement.getConsumption() - previousMeasurements.get(measurement.getDevice().getId()).getConsumption();
                float powerPeak = (float) (energyPeak / (3600000.0 / (measurement.getTimeStamp().getTime() - previousMeasurements.get(measurement.getDevice().getId()).getTimeStamp().getTime())));
                if (energyPeak > measurement.getDevice().getMaximumEnergyConsumption()) {
                    String clientUsername = measurement.getDevice().getClient().getUsername();
                    notificationService.notify(new WarningMessageDTO(
                                    measurement.getDevice().getDescription(),
                                    powerPeak,
                                    measurement.getTimeStamp()),
                            clientUsername);
                }else {
                    measurementRepository.save(measurement);
                }
                previousMeasurements.put(measurement.getDevice().getId(), measurement);
            } else {
                previousMeasurements.put(measurement.getDevice().getId(), measurement);
                measurementRepository.save(measurement);
            }

        }
    }

}
