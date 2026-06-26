package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ===== SELECT QUERIES =====

    // Find by product code
    Optional<Product> findByProductCode(String productCode);

    // Find by status
    List<Product> findByStatus(Product.ProductStatus status);

    // Find by category
    List<Product> findByCategory(Product.Category category);

    // Find by owner
    List<Product> findByOwnerID(Long ownerID);

    // Find by product name containing keyword
    List<Product> findByProductNameContaining(String keyword);

    // Check if product code exists
    boolean existsByProductCode(String productCode);

    // Custom query: find active products by category
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.status = 'Active'")
    List<Product> findActiveProductsByCategory(@Param("category") Product.Category category);

    // ===== UPDATE QUERIES =====

    // Update product status
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.status = :status WHERE p.productID = :id")
    int updateProductStatus(@Param("id") Long id,
                            @Param("status") Product.ProductStatus status);

    // Update current version of a product
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.currentVersion = :version WHERE p.productID = :id")
    int updateCurrentVersion(@Param("id") Long id,
                              @Param("version") String version);

    // Transfer ownership of a product
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.ownerID = :newOwnerID WHERE p.productID = :id")
    int transferOwnership(@Param("id") Long id,
                          @Param("newOwnerID") Long newOwnerID);

    // ===== DELETE QUERIES =====

    // Delete all EOL products
    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.status = 'EOL'")
    int deleteEOLProducts();
}
