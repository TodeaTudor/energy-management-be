package ehome.backend.utils.mappers;

import ehome.backend.dtos.DeviceConsumptionDTO;
import ehome.backend.dtos.DeviceDTO;
import org.springframework.stereotype.Component;

@Component
public class DeviceToDeviceConsumptionDTOMapper {

    public static DeviceConsumptionDTO createMapping(DeviceDTO deviceDTO) {
        return DeviceConsumptionDTO.builder()
                .id(deviceDTO.getId())
                .averageEnergyConsumption(deviceDTO.getAverageEnergyConsumption())
                .clientName(deviceDTO.getClientName())
                .description(deviceDTO.getDescription())
                .location(deviceDTO.getLocation())
                .maximumEnergyConsumption(deviceDTO.getMaximumEnergyConsumption())
                .sensorDescription(deviceDTO.getSensorDescription())
                .sensorMaximumValue(deviceDTO.getSensorMaximumValue())
                .sensorName(deviceDTO.getSensorName())
                .consumption(0)
                .build();
    }
}
