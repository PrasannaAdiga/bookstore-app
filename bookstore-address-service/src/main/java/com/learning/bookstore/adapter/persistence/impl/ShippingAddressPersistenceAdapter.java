package com.learning.bookstore.adapter.persistence.impl;

import com.learning.bookstore.adapter.persistence.entity.ShippingAddressEntity;
import com.learning.bookstore.adapter.persistence.repository.ShippingAddressRepository;
import com.learning.bookstore.application.port.out.IShippingAddressDataProvider;
import com.learning.bookstore.domain.ShippingAddress;
import com.learning.bookstore.infrastructure.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class ShippingAddressPersistenceAdapter implements IShippingAddressDataProvider {
    private final ShippingAddressRepository shippingAddressRepository;

    @Override
    public String createShippingAddress(ShippingAddress shippingAddress) {
        return shippingAddressRepository.save(buildShippingAddressEntity(shippingAddress)).getId();
    }

    @Override
    public void updateShippingAddress(ShippingAddress shippingAddress) {
        Optional<ShippingAddressEntity> oShippingAddressEntity = shippingAddress.getId() != null ? shippingAddressRepository.findById(shippingAddress.getId()) : Optional.empty();
        ShippingAddressEntity shippingAddressEntity = buildShippingAddressEntity(shippingAddress);
        if(oShippingAddressEntity.isPresent()) {
            shippingAddressEntity.setCreatedBy(oShippingAddressEntity.get().getCreatedBy());
            shippingAddressEntity.setCreatedBy(oShippingAddressEntity.get().getCreatedBy());
        }
        shippingAddressRepository.save(shippingAddressEntity);
    }

    @Override
    public void deleteShippingAddress(String shippingAddressId) {
        shippingAddressRepository.deleteById(shippingAddressId);
    }

    @Override
    public Optional<List<ShippingAddress>> getAllShippingAddressOfUser(String userEmail) {
        Optional<List<ShippingAddressEntity>> shippingAddressEntities = shippingAddressRepository.findByUserEmail(userEmail);
        if(!shippingAddressEntities.isPresent() || shippingAddressEntities.get().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(shippingAddressEntities.get().stream().map(new Function<ShippingAddressEntity, ShippingAddress>() {
            @Override
            public ShippingAddress apply(ShippingAddressEntity shippingAddressEntity) {
                return buildShippingAddress(shippingAddressEntity);
            }
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<ShippingAddress> getShippingAddressById(String shippingAddressId) {
        Optional<ShippingAddressEntity> shippingAddressEntity = shippingAddressRepository.findById(shippingAddressId);
        if(!shippingAddressEntity.isPresent()) {
            return Optional.empty();
        }
        return List.of(shippingAddressEntity.get()).stream().map(new Function<ShippingAddressEntity, ShippingAddress>() {
            @Override
            public ShippingAddress apply(ShippingAddressEntity shippingAddressEntity1) {
                return buildShippingAddress(shippingAddressEntity1);
            }
        }).findFirst();
    }

    private ShippingAddressEntity buildShippingAddressEntity(ShippingAddress shippingAddress) {
        return ShippingAddressEntity.builder()
                .id(shippingAddress.getId())
                .userEmail(shippingAddress.getUserEmail())
                .addressLine1(shippingAddress.getAddressLine1())
                .addressLine2(shippingAddress.getAddressLine2())
                .city(shippingAddress.getCity())
                .state(shippingAddress.getState())
                .postalCode(shippingAddress.getPostalCode())
                .country(shippingAddress.getCountry())
                .phone(shippingAddress.getPhone())
                .build();
    }

    private ShippingAddress buildShippingAddress(ShippingAddressEntity shippingAddressEntity) {
        return ShippingAddress.builder()
                .id(shippingAddressEntity.getId())
                .userEmail(shippingAddressEntity.getUserEmail())
                .addressLine1(shippingAddressEntity.getAddressLine1())
                .addressLine2(shippingAddressEntity.getAddressLine2())
                .city(shippingAddressEntity.getCity())
                .state(shippingAddressEntity.getState())
                .postalCode(shippingAddressEntity.getPostalCode())
                .country(shippingAddressEntity.getCountry())
                .phone(shippingAddressEntity.getPhone())
                .build();
    }
}
