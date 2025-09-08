package com.cofetarie.service;

import com.cofetarie.model.Favorite;
import com.cofetarie.model.Product;
import com.cofetarie.model.User;
import com.cofetarie.repository.FavoriteRepository;
import com.cofetarie.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository repo;
    private final ProductRepository productRepo;

    public FavoriteService(FavoriteRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    public List<Favorite> getFavorites(User user) {
        return repo.findByUser(user);
    }

    public List<Favorite> addFavorite(User user, Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Produs inexistent"));

        if (!repo.existsByUserAndProduct(user, product)) {
            repo.save(new Favorite(user, product));
        }
        return repo.findByUser(user);
    }

    public List<Favorite> removeFavorite(User user, Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Produs inexistent"));

        repo.deleteByUserAndProduct(user, product);
        return repo.findByUser(user);
    }
}