package ehome.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueMeasurementDTO {

    @JsonProperty("measurement_value")
    public Float value;

    @JsonProperty("timestamp")
    public Long timestamp;

    @JsonProperty("device_id")
    public Long deviceId;

    public Timestamp getTimestampAsDate() {
        return new Timestamp(new Date(timestamp * 1000).getTime());
    }
}
