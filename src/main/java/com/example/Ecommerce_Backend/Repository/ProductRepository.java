package com.example.Ecommerce_Backend.Repository;

import com.example.Ecommerce_Backend.Model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

    // Find products by category ID
    List<Products> findByCategoryId(Long categoryId);

    // Find by product name (case insensitive)
    List<Products> findByNameContainingIgnoreCase(String name);

    // Filter products by price range
    List<Products> findByPriceBetween(Double minPrice, Double maxPrice);

    // Combined search: name + category + price range
    @Query("SELECT p FROM Products p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Products> searchProducts(
            @Param("name") String name,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
