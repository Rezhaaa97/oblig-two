package no.oslomet.obligtwo.controller;


import no.oslomet.obligtwo.model.*;
import no.oslomet.obligtwo.repository.*;
import no.oslomet.obligtwo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



@Controller
public class HomeController {



    //@Autowired
    //private AuthorRepository authorRepository;
    //@Autowired
    //private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;



    @GetMapping("index")
    @Transactional
    public String index(){
        return "index";
    }

    @GetMapping("/authorForm")
    @Transactional
    public String formAuthor(Model model){
        Author author = new Author();
        model.addAttribute("author",author);
        return "authorForm";
    }

    @GetMapping("/listAuthor")
    @Transactional
    public String listAuthor(Model model){
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors",authors);
        //Author author = new Author();
        //model.addAttribute("author",author);

        return "/listAuthor";
    }

    @PostMapping("/saveAuthor")
    @Transactional
    public String saveAuthor(@ModelAttribute("author") Author author){
        authorService.save(author);
        return "redirect:/listAuthor";
    }

    @GetMapping("/listBook")
    public String listBook(Model model){

        List<Book> books = bookService.findAll();
        model.addAttribute("books",books);
       // Book book = new Book();
        //model.addAttribute("book",book);

        return "/listBook";
    }


    @PostMapping("/saveBook")
    @Transactional
    public String saveBook(@ModelAttribute("book") Book book,
                           @RequestParam("bookAuthS") long[] bAList,@RequestParam("category") long categoryId){
        List<Author> authorList= new ArrayList<>();
        for(Long id : bAList) {
            authorList.add(this.authorService.findById(id));
        }
        Category category = this.categoryRepository.findById(categoryId).get();
        book.setAuthors(authorList);
        book.setCategory(category);
        bookService.save(book);
        return "redirect:/listBook";
    }



    /*
    @PostMapping("/saveBook")
    @Transactional
    public String saveBook(@RequestParam("id") Long bookId,
                           @RequestParam("ISBN") Long ISBN,
                           @RequestParam("title") String title,
                           @RequestParam("releaseYear") String releaseYear,
                           @RequestParam("quantity") int quantity,
                           @RequestParam("category") long catId,
                           @RequestParam("bookAuthS") long[] bAList){
        List<Author> authorList= new ArrayList<>();
        for(Long id : bAList) {
            authorList.add(this.authorRepository.findById(id).get());
        }
        Category category = this.categoryRepository.findById(catId).get();
            Book book;

            book = new Book(ISBN, title, releaseYear, quantity,authorList ,category);

            book = bookRepository.findById(bookId).get();
            book.setISBN(ISBN);
            book.setTitle(title);
            book.setReleaseYear(releaseYear);
            book.setQuantity(quantity);
            book.setCategory(category);
            book.setAuthors(authorList);

        bookRepository.save(book);
        return "redirect:/listBook";
    }
    */



    @GetMapping("/formBook")
    public String formBook(Model model){
        Book book = new Book();
        List<Author> authorsList = authorService.findAll();
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors",authorsList);
        model.addAttribute("categories",categoryList);
        return "formBook";
    }
    @GetMapping("/detailBook/{id}")
    public String detailBook(@PathVariable("id") String id, Model model ) {
        Book book = this.bookService.findById(Long.parseLong(id));
        List<Author> authors = book.getAuthors();
        model.addAttribute("authors", authors);
        model.addAttribute("book", book);
        return "bookDetail";
    }

    @GetMapping("/editBook/{id}")
    @Transactional
    public String edit(@PathVariable("id") String id, Model model ){
        Book book = this.bookService.findById(Long.parseLong(id)); //fjernet get het
        model.addAttribute("book",book);
        List<Author> authorsList = authorService.findAll();
        model.addAttribute("authors", authorsList);
        List<Category> categoriesList = categoryRepository.findAll();
        model.addAttribute("categories", categoriesList);
        return "/formBook";
    }
    @GetMapping("/deleteBook/{id}")
    @Transactional
    public String delete(@PathVariable("id") String id, Model model ){
        this.bookService.deleteById(Long.parseLong(id));
        return "redirect:/listBook";
    }

    /*
    @RequestMapping("/searchBook")
    public String search(@RequestParam(value = "search", required = false) String title, Model model) {
        List<Book> searchResults = null;
        try {
            bookService.findAll();
            searchResults = searchService.bookSearch(title);

        } catch (Exception ex) {
            // here you should handle unexpected errors
            // ...
            // throw ex;
            //return "/listBook";
        }
        model.addAttribute("search", searchResults);
        return "/listBook";

    }
    */

    /*
    @PostMapping("/searchBook")
    public String search(@RequestParam("title") String title, @ModelAttribute("search") Book book, Model model ){
        Book book1 = this.bookService.search(title);
        System.out.println("Found book: " + book1.getTitle());
        model.addAttribute("searchBook", book1);

        return "redirect:/listBook";
    }
    */

    @GetMapping("/orderBook/{id}")
    @Transactional
    public String orderBook(@PathVariable("id") String id, Model model){
        List<Order> ordersList = orderRepository.findAll();
        Order orderUsed = new Order();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        Book bookOrdered = this.bookService.findById(Long.parseLong(id));
        List<Book> listB = new ArrayList<>();

        if (bookOrdered.getQuantity() == 0){
            return "/errorQuantity";
        }
        orderUsed.setDate(df.format(localDate));


        listB.add(bookOrdered);
        orderUsed.setBooks(listB);
        bookOrdered.getOrders().add(orderUsed);
        orderRepository.save(orderUsed);

        for (Order order : ordersList) {
            if (order.getShipping() == null) {
                listB.add(bookOrdered);
                order.setBooks(listB);
                bookOrdered.getOrders().add(order);
                orderRepository.save(order);
            }
        }

        return "redirect:/listBook";
    }
    @GetMapping({"/listOrder"})
    @Transactional
    public String listOrder(Model model ) {
        List<Order> ordersList = orderRepository.findAll();
        List<Shipping> shippingList = shippingRepository.findAll();
        Order currentOrder = null;

        for (Order order : ordersList) {
            if (order.getShipping() == null) {
                currentOrder = order;
            }
        }

        if (currentOrder != null){
            List<Book> books = currentOrder.getBooks();
            model.addAttribute("books", books);
        }
        ordersList.remove(currentOrder);
        model.addAttribute("orders", ordersList);
        model.addAttribute("currentOrder", currentOrder);
        model.addAttribute("shippingList", shippingList);
        return "listOrder";
    }

    @RequestMapping("/detailOrder/{id}")
    public String detailOrder(@PathVariable("id") String id, Model model) {
        Order currentOrder = this.orderRepository.findById(Long.parseLong(id)).get();
        List<Book> booksList = currentOrder.getBooks();
        Shipping ship = currentOrder.getShipping();

        model.addAttribute("order", currentOrder);
        model.addAttribute("books", booksList);
        model.addAttribute("ship", ship);

        return "shippingDetail";
    }

    @RequestMapping("/removeBook/{orderId}/{bookId}")
    @Transactional
    public String addCategory(@PathVariable("orderId") String orderId, @PathVariable("bookId") String bookId, Model model) {
        Order currentOrder = this.orderRepository.findById(Long.parseLong(orderId)).get();
        Book bookToRemove = this.bookService.findById(Long.parseLong(bookId));
        List<Book> books = currentOrder.getBooks();
        books.remove(bookToRemove);
        currentOrder.setBooks(books);
        orderRepository.save(currentOrder);

        return "redirect:/listOrder";
    }

    @PostMapping("/shipOrder/{id}")
    @Transactional
    public String shipOrder(@RequestParam("shipping") long shippingId, @PathVariable("id") String id, Model model){
        Order currentOrder = this.orderRepository.findById(Long.parseLong(id)).get();
        List<Book> bookList = currentOrder.getBooks();
        List<Book> noBookStock = new ArrayList<>();
        for (Book book : bookList) {
            if (book.getQuantity()>0){
                book.setQuantity(book.getQuantity()-1);
            }
            else {
                noBookStock.add(book);
            }
        }
        Shipping shipping = this.shippingRepository.findById(shippingId).get();
        currentOrder.setShipping(shipping);
        model.addAttribute("books", noBookStock);
        return "listShipping";
    }


    /* ================ Shipping ================ */

    @RequestMapping("/formShipping")
    public String addShip(Model model) {
        Shipping newShip = new Shipping();
        model.addAttribute("ship", newShip);
        return "formShipping";
    }

    @PostMapping("/saveShipping")
    public String saveShip(@ModelAttribute("ship") Shipping ship ){
        shippingRepository.save(ship);
        return "redirect:/listBook";
    }

////////////////// CAtegory
    @RequestMapping("/formCategory")
    public String addCategory(Model model) {
        Category newCategory = new Category();
        model.addAttribute("category", newCategory);
        List<Category> categoriesList = categoryRepository.findAll();
        model.addAttribute("categories", categoriesList);
        return "formCategory";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute("category") Category category ){
        categoryRepository.save(category);
        return "redirect:/formCategory";
    }

    /*
    @GetMapping({"/listCategory"})
    public String listCategory(Model model ) {
        List<Category> categoriesList = categoryRepository.findAll();
        model.addAttribute("categories", categoriesList);
        return "listCategory";
    }
    */






}
