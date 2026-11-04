package com.ly.ecommerce.mapper;

import com.ly.ecommerce.dto.userDto.UserRequest;
import com.ly.ecommerce.dto.userDto.UserResponse;
import com.ly.ecommerce.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final AddressMapper addressMapper;

    public UserResponse toUserResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId() != null ? user.getId().toString() : null)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .addressResponse(addressMapper.toAddressResponse(user.getAddress()))
                .build();
    }

    public User toUser(UserRequest userRequest) {
        if (userRequest == null) return null;
        return User.builder()
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .email(userRequest.email())
                .phone(userRequest.phone())
                .address(addressMapper.toAddress(userRequest.addressRequest()))
                .build();
    }
}
