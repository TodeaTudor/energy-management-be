package ehome.backend.utils.mappers;

import ehome.backend.dtos.AdministratorDTO;
import ehome.backend.dtos.ClientDTO;
import ehome.backend.dtos.DeviceDTO;
import ehome.backend.dtos.MeasurementDTO;
import ehome.backend.entities.Administrator;
import ehome.backend.entities.Client;
import ehome.backend.entities.Device;
import ehome.backend.entities.Measurement;

public class EntityToDtoMappers {

    public AdministratorDTO fromEntity(Administrator administrator) {
        return AdministratorDTO.builder()
                .id(administrator.getId())
                .username(administrator.getUsername())
                .password(administrator.getPassword())
                .build();
    }

    public static DeviceDTO fromEntity(Device device) {
        DeviceDTO deviceDTO = DeviceDTO.builder()
                .id(device.getId())
                .averageEnergyConsumption(device.getAverageEnergyConsumption())
                .description(device.getDescription())
                .location(device.getLocation())
                .maximumEnergyConsumption(device.getMaximumEnergyConsumption())
                .sensorDescription(device.getSensorDescription())
                .sensorMaximumValue(device.getSensorMaximumValue())
                .sensorName(device.getSensorName())
                .clientName(null)
                .build();
        if (device.getClient() != null) {
            deviceDTO.setClientName(device.getClient().getName());
        }
        return deviceDTO;
    }


    public static MeasurementDTO fromEntity(Measurement measurement) {
        return MeasurementDTO.builder()
                .id(measurement.getId())
                .consumption(measurement.getConsumption())
                .timeStamp(measurement.getTimeStamp())
                .build();
    }

    public static ClientDTO fromEntity(Client client) {

        return ClientDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .username(client.getUsername())
                .dateOfBirth(client.getDateOfBirth())
                .address(client.getAddress())
                .password(client.getPassword())
                .build();
    }
}
