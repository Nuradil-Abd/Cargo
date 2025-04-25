package cargo.entity;


import cargo.enums.Country;
import cargo.enums.TruckType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trucks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String truckNumber;
    @Enumerated(EnumType.STRING)
    private TruckType type;
    private String model;
    private Long year;
    private String techPassportNumber;
    private Country country;
    private boolean status;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transporter_id")
    private Transporter transporter;

}
