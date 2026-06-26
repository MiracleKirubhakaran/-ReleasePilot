package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.Product;
import com.cts.jfs.sprinboot.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductID(1L);
        product.setProductName("ReleasePilot");
        product.setProductCode("RP-001");
        product.setCategory(Product.Category.SaaS);
        product.setOwnerID(1L);
        product.setCurrentVersion("1.0.0");
        product.setStatus(Product.ProductStatus.Active);
    }

    // ===== GET ALL PRODUCTS =====
    @Test
    void testGetAllProducts_Success() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ReleasePilot", result.get(0).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    // ===== GET PRODUCT BY ID =====
    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("RP-001", result.getProductCode());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(99L));
    }

    // ===== SAVE PRODUCT =====
    @Test
    void testSaveProduct_Success() {
        when(productRepository.existsByProductCode("RP-001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.saveProduct(product);

        assertNotNull(result);
        assertEquals("ReleasePilot", result.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testSaveProduct_DuplicateCode() {
        when(productRepository.existsByProductCode("RP-001")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> productService.saveProduct(product));
        verify(productRepository, never()).save(any(Product.class));
    }

    // ===== UPDATE PRODUCT =====
    @Test
    void testUpdateProduct_Success() {
        Product updated = new Product();
        updated.setProductName("ReleasePilot v2");
        updated.setProductCode("RP-002");
        updated.setCategory(Product.Category.OnPremise);
        updated.setOwnerID(2L);
        updated.setCurrentVersion("2.0.0");
        updated.setStatus(Product.ProductStatus.Active);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updated);

        Product result = productService.updateProduct(1L, updated);

        assertNotNull(result);
        assertEquals("ReleasePilot v2", result.getProductName());
    }

    // ===== DELETE PRODUCT =====
    @Test
    void testDeleteProduct_Success() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    // ===== GET BY STATUS =====
    @Test
    void testGetProductsByStatus_Success() {
        when(productRepository.findByStatus(Product.ProductStatus.Active))
                .thenReturn(Arrays.asList(product));

        List<Product> result = productService.getProductsByStatus(Product.ProductStatus.Active);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Product.ProductStatus.Active, result.get(0).getStatus());
    }

    // ===== GET BY CATEGORY =====
    @Test
    void testGetProductsByCategory_Success() {
        when(productRepository.findByCategory(Product.Category.SaaS))
                .thenReturn(Arrays.asList(product));

        List<Product> result = productService.getProductsByCategory(Product.Category.SaaS);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Product.Category.SaaS, result.get(0).getCategory());
    }
}
