package com.ly.ecommerce.mapper;

import com.ly.ecommerce.dto.addressDto.AddressRequest;
import com.ly.ecommerce.dto.addressDto.AddressResponse;
import com.ly.ecommerce.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public AddressResponse toAddressResponse(Address address){

        if(address == null){
            return null;
        }

        return AddressResponse.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zip(address.getZip())
                .country(address.getCountry())
                .build();
    }

    public Address toAddress(AddressRequest addressRequest){

        if(addressRequest == null){
            return null;
        }

        return Address.builder()
                .street(addressRequest.street())
                .city(addressRequest.city())
                .state(addressRequest.state())
                .zip(addressRequest.zip())
                .country(addressRequest.country())
                .build();
    }
}
