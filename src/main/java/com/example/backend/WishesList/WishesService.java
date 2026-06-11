package com.example.backend.WishesList;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishesService {

    private final WishesRepository wishesRepository;

    public WishesService(WishesRepository wishesRepository) {
        this.wishesRepository = wishesRepository;
    }

    public List<Wishes> listAll() {
        return wishesRepository.findAll();
    }

    public Wishes get(UUID id) {
        return wishesRepository.findById(id).orElse(null);
    }

    public List<Wishes> getWishesByUserId(UUID userId) {
        return wishesRepository.findWishedProductsByUserId(userId);
    }

    public Wishes createWishes(Wishes wishes) {
        return wishesRepository.save(wishes);
    }

    public Wishes updateWishes(Wishes wishes) {
        Wishes wishesToUpdate = wishesRepository.findById(wishes.getId()).orElse(null);
        if (wishesToUpdate == null) return null;
        wishesToUpdate.setUserWishes(wishes.getUserWishes());
        wishesToUpdate.setTotalOfItems(wishes.getTotalOfItems());
        wishesToUpdate.setUser(wishes.getUser());
        return wishesRepository.save(wishesToUpdate);
    }

    public void deleteWishListById(UUID id) {
        if (wishesRepository.existsById(id)) {
            wishesRepository.deleteById(id);
        }
    }
}
