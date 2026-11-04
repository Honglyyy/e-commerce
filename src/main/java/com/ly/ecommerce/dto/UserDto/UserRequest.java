package com.ly.ecommerce.dto.UserDto;

import com.ly.ecommerce.dto.AddressDto.AddressRequest;
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
