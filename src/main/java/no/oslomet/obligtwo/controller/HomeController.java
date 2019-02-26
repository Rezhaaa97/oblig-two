package no.oslomet.obligtwo.controller;


import no.oslomet.obligtwo.model.*;
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

    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ShippingService shippingService;


    @GetMapping("index")
    @Transactional
    public String index(){
        return "index";
    }

    @GetMapping("/listAuthor")
    @Transactional
    public String listAuthor(Model model){
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors",authors);
        Author author = new Author();
        model.addAttribute("author",author);
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
        BookSearch search = new BookSearch();
        model.addAttribute("bookSearch",search);
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
        Category category = this.categoryService.findById(categoryId);
        book.setAuthors(authorList);
        book.setCategory(category);
        bookService.save(book);
        return "redirect:/listBook";
    }

    @GetMapping("/formBook")
    public String formBook(Model model){
        Book book = new Book();
        List<Author> authorsList = authorService.findAll();
        List<Category> categoryList = categoryService.findAll();
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
        List<Category> categoriesList = categoryService.findAll();
        model.addAttribute("categories", categoriesList);
        return "/formBook";
    }
    @GetMapping("/deleteBook/{id}")
    @Transactional
    public String delete(@PathVariable("id") String id, Model model ){
        this.bookService.deleteById(Long.parseLong(id));
        return "redirect:/listBook";
    }


    @RequestMapping("/searchBook")
    public String searchBook(@ModelAttribute BookSearch bookSearch, Model model) {
        List<Book> booksList = bookService.findAll();
        List<Book> search = new ArrayList<>();
        String research = bookSearch.getSearchText();
        for (Book book : booksList) {
            if (book.getTitle().contains(research)
                    || book.getCategory().getName().contains(research)) {
                search.add(book);
            }
        }
        model.addAttribute("books", search);
        BookSearch newSearch = new BookSearch();
        model.addAttribute("bookSearch", newSearch);
        return "listBook";
    }


    @GetMapping("/orderBook/{id}")
    @Transactional
    public String orderBook(@PathVariable("id") String id){
        boolean noneShipOrdered = false;
        List<Order> ordersList = orderService.findAll();
        Order orderUsed = new Order();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        Book bookOrdered = this.bookService.findById(Long.parseLong(id));
        List<Book> listB = new ArrayList<>();

        if (bookOrdered.getQuantity() == 0){
            return "/quantityError";
        }

        if (ordersList.isEmpty()){
            orderUsed.setDate(df.format(localDate));


            listB.add(bookOrdered);
            orderUsed.setBooks(listB);
            bookOrdered.getOrders().add(orderUsed);
            orderService.save(orderUsed);
        }else{

            for (Order order : ordersList) {
                if (order.getShipping() == null) {
                    noneShipOrdered = true;
                    List<Book> lB = order.getBooks();
                    lB.add(bookOrdered);
                    order.setBooks(listB);
                    bookOrdered.getOrders().add(order);
                    orderService.save(order);
                }
            }
            if (!noneShipOrdered){
                Order orderUsed2 = new Order();
                orderUsed2.setDate(df.format(localDate));
                List<Book> lB = new ArrayList<>();
                lB.add(bookOrdered);
                orderUsed2.setBooks(lB);
                bookOrdered.getOrders().add(orderUsed2);

                orderService.save(orderUsed2);
            }
        }



        return "redirect:/listBook";
    }
    @GetMapping({"/listOrder"})
    @Transactional
    public String listOrder(Model model ) {
        List<Order> ordersList = orderService.findAll();
        List<Shipping> shippingList = shippingService.findAll();
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
        Order currentOrder = this.orderService.findById(Long.parseLong(id));
        List<Book> booksList = currentOrder.getBooks();
        Shipping ship = currentOrder.getShipping();

        model.addAttribute("order", currentOrder);
        model.addAttribute("books", booksList);
        model.addAttribute("ship", ship);

        return "shippingDetail";
    }


    @PostMapping("/shipOrder/{id}")
    @Transactional
    public String shipOrder(@RequestParam("shipping") long shippingId, @PathVariable("id") String id, Model model){
        Order currentOrder = this.orderService.findById(Long.parseLong(id));
        List<Book> bookList = currentOrder.getBooks();
        List<Book> emptyStock = new ArrayList<>();
        for (Book book : bookList) {
            if (book.getQuantity()>0){
                book.setQuantity(book.getQuantity()-1);
            }
            else {
                emptyStock.add(book);

            }
        }
        Shipping shipping = this.shippingService.findById(shippingId);
        currentOrder.setShipping(shipping);
        model.addAttribute("books", emptyStock);
        return "listShipping";
    }



    @RequestMapping("/formShipping")
    public String addShip(Model model) {
        Shipping newShip = new Shipping();
        model.addAttribute("ship", newShip);
        return "formShipping";
    }

    @PostMapping("/saveShipping")
    public String saveShip(@ModelAttribute("ship") Shipping ship ){
        shippingService.save(ship);
        return "redirect:/listBook";
    }

    @RequestMapping("/formCategory")
    public String addCategory(Model model) {
        Category newCategory = new Category();
        model.addAttribute("category", newCategory);
        List<Category> categoriesList = categoryService.findAll();
        model.addAttribute("categories", categoriesList);
        return "formCategory";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute("category") Category category ){
        categoryService.save(category);
        return "redirect:/formCategory";
    }


}
