package com.cofetarie.controller;

import com.cofetarie.model.Category;
import com.cofetarie.model.Product;
import com.cofetarie.repository.ProductRepository;
import com.cofetarie.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @PutMapping("/api/products/{id}")
    public ResponseEntity<Product> updateDescription(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newDescription = body.get("description");

        Product product = productRepository.findById(id).orElseThrow();

        if (product.getCategory() == null) {
            throw new IllegalStateException("Product has no category assigned — cannot update.");
        }


        product.setDescription(newDescription);
        product.setCategory(product.getCategory());

        return ResponseEntity.ok(productRepository.save(product));
    }



    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProductToImgur(
            @RequestPart("product") Product product,
            @RequestPart("image") MultipartFile imageFile
    ) throws IOException {

        String clientId = "181fc502b55bfbe";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + clientId);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(imageFile.getBytes()) {
            @Override
            public String getFilename() {
                return imageFile.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.imgur.com/3/image", requestEntity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            String imgurUrl = (String) data.get("link");

            product.setImage(imgurUrl);
            productService.saveProduct(product);

            return ResponseEntity.ok("Uploaded to Imgur: " + imgurUrl);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Imgur upload failed");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }


}
