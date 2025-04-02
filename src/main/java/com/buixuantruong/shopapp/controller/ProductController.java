package com.buixuantruong.shopapp.controller;

import com.buixuantruong.shopapp.dto.ProductDTO;
import com.buixuantruong.shopapp.dto.ProductImageDTO;
import com.buixuantruong.shopapp.model.Product;
import com.buixuantruong.shopapp.model.ProductImage;
import com.buixuantruong.shopapp.service.IProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    IProductService productService;

    @GetMapping("")
    public ResponseEntity<String> getAllProducts() {
        return ResponseEntity.ok("All products");
    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(
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
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product newProduct = productService.createProduct(productDTO);

            return ResponseEntity.ok(newProduct);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @PathVariable("id") Long id,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            Product existingProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
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
        String fileName = StringUtils.cleanPath((file.getOriginalFilename()));

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
}
