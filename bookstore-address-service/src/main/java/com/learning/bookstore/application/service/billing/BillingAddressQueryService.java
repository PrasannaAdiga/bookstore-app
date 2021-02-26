package com.learning.bookstore.application.service.billing;

import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.exception.UnauthorizedException;
import com.learning.bookstore.application.port.in.billing.IBillingAddressQueryService;
import com.learning.bookstore.application.port.out.IBillingAddressDataProvider;
import com.learning.bookstore.domain.BillingAddress;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class BillingAddressQueryService implements IBillingAddressQueryService {
    private final IBillingAddressDataProvider billingAddressDataProvider;
    private final PrincipalResolver principalResolver;

    @Override
    public List<BillingAddress> getAllBillingAddressOfLoggedInUser() {
        String userEmail = principalResolver.getCurrentLoggedInUserMail();
        Optional<List<BillingAddress>> oBillingAddress = billingAddressDataProvider.getAllBillingAddressOfUser(userEmail);
        exitIfNoBillingAddressFound(oBillingAddress);
        return oBillingAddress.get();
    }

    @Override
    public BillingAddress getBillingAddressById(String billingAddressId) {
        Optional<BillingAddress> oSBillingAddress = billingAddressDataProvider.getBillingAddressById(billingAddressId);
        exitIfNoBillingAddressFoundForId(oSBillingAddress, billingAddressId);
        exitIfUnauthorized(oSBillingAddress, billingAddressId);
        return oSBillingAddress.get();
    }

    private void exitIfNoBillingAddressFound(Optional<List<BillingAddress>> oBillingAddress) {
        oBillingAddress.orElseThrow(() -> {
            log.error("ResourceNotFoundException in BillingAddressQueryService.getAllBillingAddressOfLoggedInUser: No billing address found for the user {}", principalResolver.getCurrentLoggedInUserMail());
            throw new ResourceNotFoundException("No billing address found for the user " + principalResolver.getCurrentLoggedInUserMail());
        });
    }

    private void exitIfNoBillingAddressFoundForId(Optional<BillingAddress> oBillingAddress, String billingAddressId) {
        oBillingAddress.orElseThrow(() -> {
            log.error("ResourceNotFoundException in BillingAddressQueryService.getBillingAddressById: billing Address with id {} not found", billingAddressId);
            throw new ResourceNotFoundException("Billing Address with id " + billingAddressId + " not found!");
        });
    }

    private void exitIfUnauthorized(Optional<BillingAddress> oBillingAddress, String billingAddressId) {
        if(!oBillingAddress.get().getUserEmail().equalsIgnoreCase(principalResolver.getCurrentLoggedInUserMail())) {
            log.error("UnauthorizedException in BillingAddressQueryService.getBillingAddressById: Billing Address with id {} is not belong to current logged in user {}",
                    billingAddressId, principalResolver.getCurrentLoggedInUserMail());
            throw new UnauthorizedException("Billing Address with id " + billingAddressId + " is not belong to current logged in user " + principalResolver.getCurrentLoggedInUserMail());
        }
    }

}
