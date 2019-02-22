package no.oslomet.obligtwo.service;

import no.oslomet.obligtwo.model.Book;

import java.util.List;

public interface BookService {

   List<Book> findAll();
   Book findById(long ISBN );
   void deleteById(long ISBN);
   Book save(Book book );
   Book search(String title);


}
