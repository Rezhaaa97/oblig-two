package no.oslomet.obligtwo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long order_id;
    private String date;

    @ManyToOne
    @JoinColumn(name = "shipping")
    private Shipping shipping;

    @ManyToMany(mappedBy = "orders")
    private List<Book> books = new ArrayList<>();

}
