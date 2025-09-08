package com.cofetarie.repository;

import com.cofetarie.model.Favorite;
import com.cofetarie.model.Product;
import com.cofetarie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserAndProduct(User user, Product product);

    Optional<Favorite> findByUserAndProduct(User user, Product product);

    List<Favorite> findByUser(User user);

    void deleteByUserAndProduct(User user, Product product);
}
