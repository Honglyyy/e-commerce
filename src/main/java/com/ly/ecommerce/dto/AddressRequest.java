package com.ly.ecommerce.dto;

import lombok.Builder;

@Builder
public record AddressRequest(
       String street,
        String city,
        String state,
        String zip,
        String country
) {
}
