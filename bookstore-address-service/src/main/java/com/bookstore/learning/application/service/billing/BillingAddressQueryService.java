package com.bookstore.learning.application.service.billing;

import com.bookstore.learning.application.exception.ResourceNotFoundException;
import com.bookstore.learning.application.port.in.billing.IBillingAddressQueryService;
import com.bookstore.learning.application.port.out.IBillingAddressDataProvider;
import com.bookstore.learning.domain.BillingAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class BillingAddressQueryService implements IBillingAddressQueryService {
    private final IBillingAddressDataProvider billingAddressDataProvider;

    @Override
    public List<BillingAddress> getAllBillingAddressOfUser(String userEmail) {
        Optional<List<BillingAddress>> oBillingAddress = billingAddressDataProvider.getAllBillingAddressOfUser(userEmail);
        oBillingAddress.orElseThrow(() -> {
            log.error("ResourceNotFoundException in BillingAddressQueryService.getAllBillingAddressOfUser: No billing address found for the user {}", userEmail);
            throw new ResourceNotFoundException("No billing address found for the user " + userEmail);
        });
        return oBillingAddress.get();
    }

    @Override
    public BillingAddress getBillingAddressById(String billingAddressId) {
        Optional<BillingAddress> oSBillingAddress = billingAddressDataProvider.getBillingAddressById(billingAddressId);
        oSBillingAddress.orElseThrow(() -> {
            log.error("ResourceNotFoundException in BillingAddressQueryService.getBillingAddressById: Billing Address with id {} not found", billingAddressId);
            throw new ResourceNotFoundException("Billing Address with id " + billingAddressId + " not found!");
        });
        return oSBillingAddress.get();
    }
}
