package ehome.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String description;

    @NotNull
    @Size(min = 3)
    private String location;

    @NotNull
    private Long maximumEnergyConsumption;

    @NotNull
    private long averageEnergyConsumption;
    private String sensorName;
    private String sensorDescription;
    private Long sensorMaximumValue;
    private String clientName;
}
