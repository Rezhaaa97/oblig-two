package no.oslomet.obligtwo.model;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@ToString
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long author_id;
    private String firstName;
    private String lastName;
    private String rating;



    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();


}
