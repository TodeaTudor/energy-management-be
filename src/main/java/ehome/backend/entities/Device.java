package ehome.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Device {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Long maximumEnergyConsumption;

    @Column(nullable = false)
    private long averageEnergyConsumption;

    private String sensorName;

    private String sensorDescription;

    private Long sensorMaximumValue;

    @OneToMany(mappedBy = "device")
    private Set<Measurement> measurements;

    @ManyToOne
    private Client client;
}
