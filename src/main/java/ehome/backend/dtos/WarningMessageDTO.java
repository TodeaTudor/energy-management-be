package ehome.backend.dtos;

import ehome.backend.entities.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarningMessageDTO {

    private String deviceName;
    private Float peakSize;
    private Timestamp peakDate;
}
