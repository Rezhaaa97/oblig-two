package no.oslomet.obligtwo.service;

import no.oslomet.obligtwo.model.Book;

import java.util.List;

public interface BookService {

   List<Book> findAll();
   Book findById(long id );
   void deleteById(long id);
   Book save(Book book );
   List<Book> search(String title);


}
