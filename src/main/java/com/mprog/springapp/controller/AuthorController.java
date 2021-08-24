package com.mprog.springapp.controller;

import com.mprog.springapp.dao.UserDao;
import com.mprog.springapp.model.Author;
import com.mprog.springapp.model.Book;
import com.mprog.springapp.model.User;
import com.mprog.springapp.service.AuthorService;
import com.mprog.springapp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class AuthorController {


    @Autowired
    private AuthorService authorService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserDao userDao;


    @GetMapping("/authors")
    public String allBooks(Model model){
        User user = getUser();

        List<Author> authors = authorService.getAll(user.getId().intValue());
        model.addAttribute("authors", authors);

        return "authors";
    }

    @GetMapping("/addAuthor")
    public String add(Model model){
        model.addAttribute("author", new Author());
        return "add_author";
    }

    @PostMapping("/authors")
    public String addAuthor(@ModelAttribute("author") Author author, @RequestParam("bookImage") MultipartFile file) throws ServletException, IOException {
        User user = getUser();

        String image = imageService.getImage(file);
        author.setImage(image);
        author.setUser(user);
        authorService.save(author);
        return "redirect:/authors";
    }


    public User getUser() {
        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            username = ((UserDetails) principal).getUsername();
        }
        User user = userDao.findByUsername(username);
        return user;
    }
}
