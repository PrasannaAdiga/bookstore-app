package com.bookstore.learning.adapter.persistence.impl;

import com.bookstore.learning.adapter.persistence.entity.BillingAddressEntity;
import com.bookstore.learning.adapter.persistence.repository.BillingAddressRepository;
import com.bookstore.learning.application.port.out.IBillingAddressDataProvider;
import com.bookstore.learning.domain.BillingAddress;
import com.bookstore.learning.infrastructure.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class BillingAddressPersistenceAdapter implements IBillingAddressDataProvider {
    private final BillingAddressRepository billingAddressRepository;

    @Override
    public String createBillingAddress(BillingAddress billingAddress) {
        return billingAddressRepository.save(buildBillingAddressEntity(billingAddress)).getId();
    }

    @Override
    public void updateBillingAddress(BillingAddress billingAddress) {
        billingAddressRepository.save(buildBillingAddressEntity(billingAddress));
    }

    @Override
    public void deleteBillingAddress(String billingAddressId) {
        billingAddressRepository.deleteById(billingAddressId);
    }

    @Override
    public Optional<List<BillingAddress>> getAllBillingAddressOfUser(String userEmail) {
        Optional<List<BillingAddressEntity>> billingAddressEntities = billingAddressRepository.findByUserEmail(userEmail);
        if(!billingAddressEntities.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(billingAddressEntities.get().stream().map(new Function<BillingAddressEntity, BillingAddress>() {
            @Override
            public BillingAddress apply(BillingAddressEntity billingAddressEntity) {
                return buildBillingAddress(billingAddressEntity);
            }
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<BillingAddress> getBillingAddressById(String billingAddressId) {
        Optional<BillingAddressEntity> billingAddressEntity = billingAddressRepository.findById(billingAddressId);
        if(!billingAddressEntity.isPresent()) {
            return Optional.empty();
        }
        return List.of(billingAddressEntity.get()).stream().map(new Function<BillingAddressEntity, BillingAddress>() {
            @Override
            public BillingAddress apply(BillingAddressEntity billingAddressEntity) {
                return buildBillingAddress(billingAddressEntity);
            }
        }).findFirst();
    }

    private BillingAddressEntity buildBillingAddressEntity(BillingAddress billingAddress) {
        return BillingAddressEntity.builder()
                .id(billingAddress.getId())
                .userEmail(billingAddress.getUserEmail())
                .addressLine1(billingAddress.getAddressLine1())
                .addressLine2(billingAddress.getAddressLine2())
                .city(billingAddress.getCity())
                .state(billingAddress.getState())
                .postalCode(billingAddress.getPostalCode())
                .country(billingAddress.getCountry())
                .phone(billingAddress.getPhone())
                .build();
    }

    private BillingAddress buildBillingAddress(BillingAddressEntity billingAddressEntity) {
        return BillingAddress.builder()
                .id(billingAddressEntity.getId())
                .userEmail(billingAddressEntity.getUserEmail())
                .addressLine1(billingAddressEntity.getAddressLine1())
                .addressLine2(billingAddressEntity.getAddressLine2())
                .city(billingAddressEntity.getCity())
                .state(billingAddressEntity.getState())
                .postalCode(billingAddressEntity.getPostalCode())
                .country(billingAddressEntity.getCountry())
                .phone(billingAddressEntity.getPhone())
                .build();
    }
}
