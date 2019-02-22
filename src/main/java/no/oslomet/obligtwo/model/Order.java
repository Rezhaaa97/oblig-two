package no.oslomet.obligtwo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
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

    public Order() {
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", date='" + date + '\'' +
                ", shipping=" + shipping +
                ", books=" + books +
                '}';
    }
}
