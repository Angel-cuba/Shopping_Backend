package com.example.backend.WishesList;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "https://starlit-bienenstitch-282c7d.netlify.app"})
@RestController
@RequestMapping("/api/v1/wishes")
public class WishesController {

    private final WishesService wishesService;

    public WishesController(WishesService wishesService) {
        this.wishesService = wishesService;
    }

    @GetMapping
    public Iterable<Wishes> list() {
        return wishesService.listAll();
    }

    @GetMapping("/{id}")
    public Wishes get(@PathVariable UUID id) {
        return wishesService.get(id);
    }

    @GetMapping("/user/{userId}")
    public List<Wishes> getWishesByUserId(@PathVariable UUID userId) {
        return wishesService.getWishesByUserId(userId);
    }

    @PostMapping
    public Wishes save(@RequestBody Wishes wishes) {
       return wishesService.createWishes(wishes);
    }

    @PutMapping
    public Wishes update(@RequestBody Wishes wishes) {
        return wishesService.updateWishes(wishes);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        wishesService.deleteWishListById(id);
    }
}
