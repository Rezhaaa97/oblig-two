package no.oslomet.obligtwo.service;

import no.oslomet.obligtwo.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();
    Author findById(long id);
    Author save(Author author);

}
