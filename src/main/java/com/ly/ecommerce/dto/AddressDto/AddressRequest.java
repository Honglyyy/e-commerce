package com.ly.ecommerce.dto.AddressDto;

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
