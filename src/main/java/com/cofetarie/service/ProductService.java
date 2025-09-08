package com.cofetarie.service;

import com.cofetarie.model.Category;
import com.cofetarie.model.Product;
import com.cofetarie.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = productRepository.findById(id).orElseThrow();

        product.setDescription(updatedProduct.getDescription());
        Category cat = product.getCategory();
        if (cat == null) throw new RuntimeException("Category cannot be null");
        product.setCategory(cat);

        return productRepository.save(product);
    }


    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }

}
