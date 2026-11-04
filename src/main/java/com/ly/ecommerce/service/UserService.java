package com.ly.ecommerce.service;

import com.ly.ecommerce.dto.UserDto.UserRequest;
import com.ly.ecommerce.dto.UserDto.UserResponse;
import com.ly.ecommerce.mapper.AddressMapper;
import com.ly.ecommerce.mapper.UserMapper;
import com.ly.ecommerce.model.Address;
import com.ly.ecommerce.model.User;
import com.ly.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toUserResponse);
    }

    @Transactional
    public Optional<UserResponse> updateUser(Long id, UserRequest userRequest) {
        return userRepository.findById(id).map(updatedUser -> {
            updatedUser.setFirstName(userRequest.firstName());
            updatedUser.setLastName(userRequest.lastName());
            updatedUser.setEmail(userRequest.email());
            updatedUser.setPhone(userRequest.phone());

            if (userRequest.addressRequest() != null) {
                Address address = updatedUser.getAddress();
                if (address != null) {
                    address.setStreet(userRequest.addressRequest().street());
                    address.setCity(userRequest.addressRequest().city());
                    address.setState(userRequest.addressRequest().state());
                    address.setZip(userRequest.addressRequest().zip());
                    address.setCountry(userRequest.addressRequest().country());
                } else {
                    updatedUser.setAddress(addressMapper.toAddress(userRequest.addressRequest()));
                }
            }

            userRepository.save(updatedUser);
            return userMapper.toUserResponse(updatedUser);
        });
    }
}
