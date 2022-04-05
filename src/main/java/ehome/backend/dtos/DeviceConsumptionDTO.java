package ehome.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceConsumptionDTO {

    private Long id;
    private String description;
    @NotNull
    private String location;
    @NotNull
    private Long maximumEnergyConsumption;
    @NotNull
    private long averageEnergyConsumption;
    private String sensorName;
    private String sensorDescription;
    private Long sensorMaximumValue;
    private String clientName;
    private float consumption;
}
