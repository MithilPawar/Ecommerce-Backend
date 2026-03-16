package com.example.Ecommerce_Backend.Controller;

import com.example.Ecommerce_Backend.DTO.ProductDto;
import com.example.Ecommerce_Backend.Model.Category;
import com.example.Ecommerce_Backend.Model.Products;
import com.example.Ecommerce_Backend.Service.CategoryService;
import com.example.Ecommerce_Backend.Service.ImageUploadService;
import com.example.Ecommerce_Backend.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final ImageUploadService imageUploadService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, ImageUploadService imageUploadService, CategoryService categoryService) {
        this.productService = productService;
        this.imageUploadService = imageUploadService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Products>> getAllProduct(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/users/me")
    public ResponseEntity<String> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(authentication.getAuthorities().toString());
    }

    @GetMapping("/category/{cat_id}")
    public ResponseEntity<List<Products>> getProductByCategory(@PathVariable long cat_id){
        return ResponseEntity.ok(productService.getProductByCategory(cat_id));
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping
//    public ResponseEntity<Products> createProduct(
//            @RequestPart("product") Products products,
//            @RequestPart("image") MultipartFile image){
//        System.out.println("Hit");
//        String imageUrl = imageUploadService.uploadImage(image);
//        products.setImageUrl(imageUrl);
//        return ResponseEntity.ok(productService.createProduct(products));
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Products> createProduct(
            @ModelAttribute ProductDto productDto,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        // You can fetch the Category entity from DB using categoryId
        Category category = categoryService.getCategoryById(productDto.getCategoryId());

        Products products = new Products();
        products.setName(productDto.getName());
        products.setDescription(productDto.getDescription());
        products.setPrice(productDto.getPrice());
        products.setQuantity(productDto.getQuantity());
        products.setCategory(category);

        if (image != null && !image.isEmpty()) {
            String imageUrl = imageUploadService.uploadImage(image);
            products.setImageUrl(imageUrl);
        }

        return ResponseEntity.ok(productService.createProduct(products));
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Products> updateProduct(@PathVariable long id, @RequestBody Products products){
        return ResponseEntity.ok(productService.updateProduct(id, products));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product Deleted Successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Products>> searchProducts(@RequestParam String name){
        return ResponseEntity.ok(productService.searchByName(name));
    }

    @GetMapping("/price")
    public ResponseEntity<List<Products>> filterByPrice(@RequestParam Double min, @RequestParam Double max){
        return ResponseEntity.ok(productService.filterByPrice(min, max));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Products>> searchFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ){
        return ResponseEntity.ok(productService.searchProducts(name, categoryId, minPrice, maxPrice));
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Products>> sortProducts(@RequestParam String field, @RequestParam String order) {
        return ResponseEntity.ok(productService.sortProducts(field, order));
    }
}
