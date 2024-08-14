package me.emma.productinventoryservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.emma.productinventoryservice.entity.Product;
import me.emma.productinventoryservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("product")
@AllArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.addProduct(product), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        log.info("reaching product inventory");
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Product Not found!", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{productId}/restock")
    public ResponseEntity<String> updateProductInventory(@PathVariable Long productId,
                                                         @RequestParam Integer quantity,
                                                         @RequestParam Boolean isDeduction) {
        boolean flag = productService.updateProductInventory(productId, quantity, isDeduction);
        if (!flag) {
            return new ResponseEntity<>("Insufficient inventory！",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
    }

    @GetMapping("/lowstock")
    public ResponseEntity<List<Product>> getProductLowStock() {
        return new ResponseEntity<>(productService.getProductsLowStock(), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                                @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(productId, product);
        if (updatedProduct==null) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Product updated!",HttpStatus.NO_CONTENT);
    }

}
