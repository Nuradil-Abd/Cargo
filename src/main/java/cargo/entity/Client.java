package cargo.entity;

import cargo.enums.Country;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "client")
    private List<Cargo> cargos = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
    private Country country;
    private String activityType;
    private String address;
}
