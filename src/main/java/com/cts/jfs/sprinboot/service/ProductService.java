package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.Product;
import com.cts.jfs.sprinboot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.cts.jfs.sprinboot.exception.ResourceNotFoundException;
import com.cts.jfs.sprinboot.exception.DuplicateRecordException;
import com.cts.jfs.sprinboot.exception.InvalidOperationException;
import com.cts.jfs.sprinboot.exception.UnauthorizedException;
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    // Save new product
    public Product saveProduct(Product product) {
        if (productRepository.existsByProductCode(product.getProductCode())) {
            throw new RuntimeException("Product code already exists: " + product.getProductCode());
        }
        return productRepository.save(product);
    }

    // Update product
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id);
        existing.setProductName(updatedProduct.getProductName());
        existing.setProductCode(updatedProduct.getProductCode());
        existing.setCategory(updatedProduct.getCategory());
        existing.setOwnerID(updatedProduct.getOwnerID());
        existing.setCurrentVersion(updatedProduct.getCurrentVersion());
        existing.setStatus(updatedProduct.getStatus());
        return productRepository.save(existing);
    }

    // Delete product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Get products by status
    public List<Product> getProductsByStatus(Product.ProductStatus status) {
        return productRepository.findByStatus(status);
    }

    // Get products by category
    public List<Product> getProductsByCategory(Product.Category category) {
        return productRepository.findByCategory(category);
    }

    // Get products by owner
    public List<Product> getProductsByOwner(Long ownerId) {
        return productRepository.findByOwnerID(ownerId);
    }
}
