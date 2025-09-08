package com.cofetarie.service;

import com.cofetarie.model.Category;
import com.cofetarie.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @PostConstruct
    public void initDefaultCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> defaultCategories = List.of(
                    new Category("Înghețată", "Produse reci, delicioase"),
                    new Category("Torturi", "Torturi festive și aniversare"),
                    new Category("Torturi dietetice", "Pentru diete speciale"),
                    new Category("Batoane", "Gustări rapide"),
                    new Category("Bomboane", "Dulciuri mici și aromate"),
                    new Category("Prăjituri", "Prăjituri de casă"),
                    new Category("Prăjituri dietetice", "Fără zahăr, diet-friendly"),
                    new Category("Foietaje", "Foietaje proaspete"),
                    new Category("Patiserie", "Delicioase")
            );

            defaultCategories.forEach(categoryRepository::save);
            System.out.println("✔️ categoriile sunt default acum.");
        }
    }

}
