package no.oslomet.obligtwo.controller;


import no.oslomet.obligtwo.model.Author;
import no.oslomet.obligtwo.model.Book;
import no.oslomet.obligtwo.repository.AuthorRepository;
import no.oslomet.obligtwo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;


    @GetMapping("index")
    public String index(){
        return "index";
    }

    @GetMapping("/listAuthor")
    public String listAuthor(Model model){
        List<Author> authors = authorRepository.findAll();
        model.addAttribute("authors",authors);
        Author author = new Author();
        model.addAttribute("author",author);

        return "/listAuthor";
    }

    @PostMapping("/saveAuthor")
    public String saveAuthor(@ModelAttribute("author") Author author){
        authorRepository.save(author);
        return "redirect:/listAuthor";
    }

    @GetMapping("/listBook")
    public String listBook(Model model){
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books",books);
        Book book = new Book();
        model.addAttribute("book",book);

        return "/listBook";
    }

    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute("book") Book book){
        bookRepository.save(book);
        return "redirect:/listBook";
    }
    @GetMapping("/authorForm")
    public String formAuthor(Model model){
        Author author = new Author();
        model.addAttribute("author",author);
        return "authorForm";
    }

    @GetMapping("/formBook")
    public String formBook(Model model){
        Book book = new Book();

        model.addAttribute("book", book);
        return "formBook";
    }

    @GetMapping("/edit/{ISBN}")
    public String edit(@PathVariable("ISBN") String ISBN, Model model ){
        Book book = this.bookRepository.findById(Long.parseLong(ISBN)).get();
        //List<Book> books = bookRepository.findAll();
        model.addAttribute("book",book);
        //model.addAttribute("books",books);

        return "/formBook";
    }
    @GetMapping("/delete/{ISBN}")
    public String delete(@PathVariable("ISBN") String ISBN, Model model ){
        this.bookRepository.deleteById(Long.parseLong(ISBN));
        return "redirect:/listBook";
    }


}
