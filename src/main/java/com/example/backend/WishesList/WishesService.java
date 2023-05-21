package com.example.backend.WishesList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishesService {
    @Autowired
    private WishesRepository wishesRepository;

    public List<Wishes> listAll() {
        return wishesRepository.findAll();
    }

    public Wishes get(UUID id) {
        if (!wishesRepository.existsById(id)) {
            return null;
        } else {
            return wishesRepository.findById(id).get();
        }
    }

    public List<Wishes> getWishesByUserId(UUID userId) {
        return wishesRepository.findWishedProductsByUserId(userId);
    }

    public Wishes createWishes(Wishes wishes) {
        return wishesRepository.save(wishes);
    }

    public Wishes updateWishes(Wishes wishes) {
        Wishes wishesToUpdate = wishesRepository.findById(wishes.getId()).orElse(null);
        if (wishesToUpdate == null) {
            return null;
        } else {
            wishesToUpdate.setUserWishes(wishes.getUserWishes());
            wishesToUpdate.setTotalOfItems(wishes.getTotalOfItems());
            wishesToUpdate.setUser(wishes.getUser());
            return wishesRepository.save(wishesToUpdate);
        }
    }

    public void deleteWishListById(UUID id) {
        Wishes wishesToDelete = wishesRepository.findById(id).orElse(null);
        if (wishesToDelete == null) {
            return;
        } else {
            wishesRepository.deleteById(id);
        }
    }
}
