package controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/all")
public class ProductController {

    @GetMapping
    public String findAll() {
        final String s = "All products here";
        return s;
    }
}
