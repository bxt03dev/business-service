package com.buixuantruong.shopapp.controller;

import com.buixuantruong.shopapp.dto.ApiResponse;
import com.buixuantruong.shopapp.dto.ProductDTO;
import com.buixuantruong.shopapp.dto.ProductImageDTO;
import com.buixuantruong.shopapp.dto.response.ProductListResponse;
import com.buixuantruong.shopapp.dto.response.ProductResponse;
import com.buixuantruong.shopapp.model.Product;
import com.buixuantruong.shopapp.model.ProductImage;
import com.buixuantruong.shopapp.service.ProductService;
import com.buixuantruong.shopapp.utils.fiels.common;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PostMapping("")
    public ApiResponse<Object> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult bindingResult
            //@RequestPart("file") MultipartFile file
            ){
        try{
            if(bindingResult.hasErrors()) {
                List<String> errorMessage = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ApiResponse.builder()
                        .message(String.join(", ", errorMessage))
                        .build();
            }

            return productService.createProduct(productDTO);
        }
        catch(Exception e){
            return ApiResponse.builder()
                    .message(e.getMessage())
                    .build();
        }

    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @PathVariable("id") Long id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            Product existingProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > common.MAXIMUM_IMAGE){
                return ResponseEntity.badRequest().body("You can only upload up to 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for(MultipartFile file : files) {
                if(file != null){
                    if(file.getSize() == 0){
                        continue;
                    }
                    if(file.getSize() > 10 * 1024 * 1024) {
                        throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File too large");
                    }
                    String contentType = file.getContentType();
                    if(contentType == null || !contentType.startsWith("image")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unsupported Media Type");
                    }
                    String filename = storeFile(file);
                    ProductImage productImage = this.productService.createProductImage(
                            existingProduct.getId(),
                            ProductImageDTO.builder()
                                    .imageUrl(filename)
                                    .build());
                    productImages.add(productImage);
                }
                return ResponseEntity.ok().body(productImages);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return null;
    }

    private String storeFile(MultipartFile file) throws IOException {
        if(!isImageFile(file) || file.getOriginalFilename() == null){
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
        }
        String fileName = StringUtils.cleanPath((Objects.requireNonNull(file.getOriginalFilename())));

        //them UUID vao z ten file de dam bao ten file la duy nhat
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

        //duong dan den thu muc muon luu file
        java.nio.file.Path uploadDir = Paths.get("uploads");

        //kiem tra va tao thu muc neu khong ton tai
        if(!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        //duong dan day du ten file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFileName);

        //sao chep ten file vao thu muc dich
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    private Boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @GetMapping("")
    public ApiResponse<Object> getProduct(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createAt").descending());
        Page<ProductResponse> products = productService.getAllProducts(pageRequest);
        int totalPages = products.getTotalPages();
        List<ProductResponse> productList = products.getContent();
        return ApiResponse.builder()
                .result(new ProductListResponse(productList, totalPages))
                .build();
    }

    @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts() throws Exception {
        Faker faker = new Faker();
        for(int i=  0; i < 1000; i++){
            String productName = faker.commerce().productName();
            if(productService.existsProduct(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .description(faker.lorem().sentence(10))
                    .thumbnail("https://picsum.photos/200/300")
                    .price((Long)faker.number().numberBetween(10L, 900000000L))
                    .categoryId((Long)faker.number().numberBetween(1L, 5L))
                    .build();
            productService.createProduct(productDTO);
        }
        return ResponseEntity.ok("Fake products generated successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<Object> getProductById(@PathVariable("id") Long id) throws Exception {
        return ApiResponse.builder()
                .result(ProductResponse.from(productService.getProductById(id)))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ApiResponse.builder()
                .result("Delete product successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) throws Exception {
        Product updatedProduct = productService.updateProduct(id, productDTO);
        return ApiResponse.builder()
                .result(ProductResponse.from(updatedProduct))
                .build();
    }
}
