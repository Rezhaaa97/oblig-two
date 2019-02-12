package no.oslomet.obligtwo.service;

import no.oslomet.obligtwo.model.Author;
import no.oslomet.obligtwo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;


    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author findById(long id ) {
        return authorRepository.findById(id).get();
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }


}
