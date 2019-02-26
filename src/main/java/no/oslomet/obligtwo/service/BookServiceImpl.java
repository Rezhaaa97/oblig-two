package no.oslomet.obligtwo.service;

import no.oslomet.obligtwo.model.Book;
import no.oslomet.obligtwo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private  BookRepository bookRepository;


    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(long ISBN ) {
        return bookRepository.findById(ISBN).get();
    }

    @Override
    public void deleteById(long ISBN ) {
        bookRepository.deleteById(ISBN);
    }

    @Override
    public Book save(Book book ) {
        return bookRepository.save(book);
    }


}
