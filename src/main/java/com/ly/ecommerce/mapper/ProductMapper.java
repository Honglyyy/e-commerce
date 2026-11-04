package com.ly.ecommerce.mapper;

import com.ly.ecommerce.dto.productDto.ProductRequest;
import com.ly.ecommerce.dto.productDto.ProductResponse;
import com.ly.ecommerce.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toProductDto(Product product) {
        if(product == null){
            return null;
        }
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getImageUrl(),
                product.getActive(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public Product toEntity(ProductRequest productRequest) {

        if(productRequest == null){
            return null;
        }

        Product product = new Product();
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setQuantity(productRequest.quantity());
        product.setCategory(productRequest.category());
        product.setImageUrl(productRequest.imageUrl());
        product.setActive(productRequest.active());
        return product;
    }
}
