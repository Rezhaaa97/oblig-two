package no.oslomet.obligtwo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shipping_id;
    private String firstName;
    private String lastName;
    private String address;
    private int postalCode;

    @OneToMany(mappedBy = "shipping", cascade = CascadeType.ALL)
    private List<Order> orders;
}
