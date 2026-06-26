package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Builder
public class Product {

    @Id
    //@SequenceGenerator(name = "my_seq", initialValue = 1000, allocationSize = 5)
    //@GeneratedValue( generator = "my_seq",         strategy = GenerationType.SEQUENCE)
    @GeneratedValue(    strategy = GenerationType.IDENTITY)
    private Long productID;

    private String productName;

    @Column(unique = true, nullable = false)
    private String productCode;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Long ownerID;

    private String currentVersion;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public enum Category {
        SaaS, OnPremise, Mobile, API, Embedded
    }

    public enum ProductStatus {
        Active, Sunset, EOL
    }

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

    public Product(Long productID, String productName, String productCode, Category category, Long ownerID, String currentVersion, ProductStatus status) {
        this.productID = productID;
        this.productName = productName;
        this.productCode = productCode;
        this.category = category;
        this.ownerID = ownerID;
        this.currentVersion = currentVersion;
        this.status = status;
    }

    public Product() {
    }
}
