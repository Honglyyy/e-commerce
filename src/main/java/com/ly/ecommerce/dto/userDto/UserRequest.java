package com.ly.ecommerce.dto.userDto;

import com.ly.ecommerce.dto.addressDto.AddressRequest;
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
