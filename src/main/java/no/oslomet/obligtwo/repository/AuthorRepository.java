package no.oslomet.obligtwo.repository;

import no.oslomet.obligtwo.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
