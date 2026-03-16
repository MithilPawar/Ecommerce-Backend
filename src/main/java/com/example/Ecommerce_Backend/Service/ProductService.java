package com.example.Ecommerce_Backend.Service;

import com.example.Ecommerce_Backend.Exception.CustomExceptionHandler.*;
import com.example.Ecommerce_Backend.Model.Category;
import com.example.Ecommerce_Backend.Model.Products;
import com.example.Ecommerce_Backend.Repository.CategoryRepository;
import com.example.Ecommerce_Backend.Repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    public Products createProduct(Products products) {
        return productRepository.save(products);
    }

    public Products getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    public List<Products> getProductByCategory(long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException("Category does not exist"));
        return productRepository.findByCategoryId(catId);
    }

    public Products updateProduct(long id, Products products) {
        Products existProducts = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product does not exist"));
        existProducts.setName(products.getName());
        existProducts.setDescription(products.getDescription());
        existProducts.setPrice(products.getPrice());
        existProducts.setQuantity(products.getQuantity());

        if(products.getCategory() != null){
            Category category = categoryRepository.findById(products.getCategory().getId()).orElseThrow(() -> new CategoryNotFoundException("Product Category does not exist"));
            existProducts.setCategory(category);
        }
        return productRepository.save(existProducts);
    }

    public void deleteProduct(long id) {
        Products products = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
        productRepository.deleteById(id);
    }

    //Searching product by keyword
    public List<Products> searchByName(String name){
        return productRepository.findByNameContainingIgnoreCase(name);
    }


    //Filter by price range
    public List<Products> filterByPrice(Double minPrice, Double maxPrice){
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    //combined search and filters
    public List<Products> searchProducts(String name, Long categoryId, Double minPrice, Double maxPrice){
        return productRepository.searchProducts(name, categoryId, minPrice, maxPrice);
    }

    public List<Products> sortProducts(String field, String order) {
        List<String> allowedFields = List.of("name", "price", "quantity");
        if (!allowedFields.contains(field)) {
            throw new IllegalArgumentException("Invalid sort field: " + field);
        }
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(field).ascending() : Sort.by(field).descending();
        return productRepository.findAll(sort);
    }
}
