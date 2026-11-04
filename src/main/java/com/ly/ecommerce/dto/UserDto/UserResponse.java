package com.ly.ecommerce.dto.UserDto;

import com.ly.ecommerce.dto.AddressDto.AddressResponse;
import com.ly.ecommerce.model.enums.UserRole;
import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        String phone,
        UserRole role,
        AddressResponse addressResponse
) {
}
