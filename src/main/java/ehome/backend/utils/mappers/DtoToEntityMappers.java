package ehome.backend.utils.mappers;

import ehome.backend.dtos.AdministratorDTO;
import ehome.backend.dtos.ClientDTO;
import ehome.backend.dtos.DeviceDTO;
import ehome.backend.dtos.QueueMeasurementDTO;
import ehome.backend.entities.Administrator;
import ehome.backend.entities.Client;
import ehome.backend.entities.Device;
import ehome.backend.entities.Measurement;


public class DtoToEntityMappers {


    public static Administrator toEntity(AdministratorDTO administratorDTO) {
        return Administrator.builder()
                .password(administratorDTO.getPassword())
                .username(administratorDTO.getUsername())
                .id(administratorDTO.getId())
                .build();

    }

    public static Client toEntity(ClientDTO clientDTO) {
        return Client.builder()
                .id(clientDTO.getId())
                .address(clientDTO.getAddress())
                .dateOfBirth(clientDTO.getDateOfBirth())
                .name(clientDTO.getName())
                .username(clientDTO.getUsername())
                .password(clientDTO.getPassword())
                .build();
    }

    public static Device toEntity(DeviceDTO deviceDTO) {
        return Device.builder()
                .id(deviceDTO.getId())
                .averageEnergyConsumption(deviceDTO.getAverageEnergyConsumption())
                .description(deviceDTO.getDescription())
                .location(deviceDTO.getLocation())
                .maximumEnergyConsumption(deviceDTO.getMaximumEnergyConsumption())
                .sensorDescription(deviceDTO.getSensorDescription())
                .sensorMaximumValue(deviceDTO.getSensorMaximumValue())
                .sensorName(deviceDTO.getSensorName())
                .build();
    }



}
