package cargo.entity;


import cargo.enums.Country;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transporters")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transporter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateOfBirth;
    private String licenseNumber;
    private double drivingExperience;
    @Enumerated(EnumType.STRING)
    private Country country;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToOne(mappedBy = "transporter")
    private Truck truck;
    @OneToMany(mappedBy = "transporter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cargo> cargos = new ArrayList<>();

}
