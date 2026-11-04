package com.ly.ecommerce.service;

import com.ly.ecommerce.dto.productDto.ProductRequest;
import com.ly.ecommerce.dto.productDto.ProductResponse;
import com.ly.ecommerce.mapper.ProductMapper;
import com.ly.ecommerce.model.Product;
import com.ly.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productRepository.save(productMapper.toEntity(productRequest));
        return productMapper.toProductDto(product);
    }


    public Optional<ProductResponse> getProductById(Long id) {
        return  productRepository.findById(id).map(productMapper::toProductDto);
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id).map(updateProduct->{
            updateProduct.setName(productRequest.name());
            updateProduct.setDescription(productRequest.description());
            updateProduct.setPrice(productRequest.price());
            updateProduct.setCategory(productRequest.category());
            updateProduct.setActive(productRequest.active());
            updateProduct.setImageUrl(productRequest.imageUrl());
            updateProduct.setQuantity(productRequest.quantity());
            productRepository.save(updateProduct);
            return productMapper.toProductDto(updateProduct);
        });
    }

    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByActiveTrue()
                .stream().map(productMapper::toProductDto).toList();
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }
}
