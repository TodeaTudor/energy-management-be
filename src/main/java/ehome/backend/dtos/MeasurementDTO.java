package ehome.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementDTO {

    private Long id;
    private Timestamp timeStamp;
    private Float consumption;
}
