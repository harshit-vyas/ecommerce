package com.project.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.entity.Product;
import com.project.ecommerce.serviceimpl.ProductServiceImpl;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ProductServiceImpl productService;

	@PostMapping("/addproduct")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		try {
			Product createdProduct = productService.addProduct(product);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Product added successfully with ID: " + createdProduct.getId());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/updateproduct/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Product productDetails) {
		try {
			Product updatedProduct = productService.updateProduct(productId, productDetails);
			return ResponseEntity.ok("Product updated successfully");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@DeleteMapping("/deleteproduct/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
		try {
			productService.deleteProduct(productId);
			return ResponseEntity.ok("Product deleted successfully");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/products")
	public ResponseEntity<?> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		if (products.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No products found.");
		}
		return ResponseEntity.ok(products);
	}
}
