package no.oslomet.obligtwo.repository;

import no.oslomet.obligtwo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
}