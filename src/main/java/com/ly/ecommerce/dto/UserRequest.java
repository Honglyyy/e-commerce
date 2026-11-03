package com.ly.ecommerce.dto;

import lombok.Builder;

@Builder
public record UserRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        AddressRequest addressRequest
){
}
