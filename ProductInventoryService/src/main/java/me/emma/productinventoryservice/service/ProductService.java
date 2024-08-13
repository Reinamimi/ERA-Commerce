package me.emma.productinventoryservice.service;

import lombok.AllArgsConstructor;
import me.emma.productinventoryservice.entity.Product;
import me.emma.productinventoryservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public boolean updateProductInventory(Long productId, Integer quantity, Boolean isDeduction) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return false;
        }
        Integer newStock;
        Integer stock = product.getStock();
        if (isDeduction) {
            newStock = stock - quantity;
            if(newStock < 0) {
                return false;
            }
        } else {
            newStock = stock + quantity;
        }
        product.setStock(newStock);
        productRepository.save(product);
        return true;
    }

    public List<Product> getProductsLowStock() {
            int lowStockValue = 30;
            return productRepository.findByStockLessThanEqual(lowStockValue);
    }

    public Product updateProduct(Long productId, Product product) {
        Product existingProduct = productRepository.findById(productId).orElse(null);
        if(existingProduct == null) {
            return null;
        }
        else{
            existingProduct.setStock(product.getStock());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            productRepository.save(existingProduct);
        }
        return existingProduct;
    }
}
