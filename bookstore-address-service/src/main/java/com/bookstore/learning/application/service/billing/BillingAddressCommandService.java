package com.bookstore.learning.application.service.billing;

import com.bookstore.learning.application.exception.UnauthorizedException;
import com.bookstore.learning.application.port.in.billing.IBillingAddressCommandService;
import com.bookstore.learning.application.port.in.billing.IBillingAddressQueryService;
import com.bookstore.learning.application.port.out.IBillingAddressDataProvider;
import com.bookstore.learning.domain.BillingAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class BillingAddressCommandService implements IBillingAddressCommandService {
    private final IBillingAddressDataProvider billingAddressDataProvider;
    private final IBillingAddressQueryService billingAddressQueryService;

    @Override
    public String createBillingAddress(BillingAddress billingAddress) {
        return billingAddressDataProvider.createBillingAddress(billingAddress);
    }

    @Override
    public void updateBillingAddress(BillingAddress billingAddress) {
        exitIfNoBillingAddressFound(billingAddress.getId());
        exitIfUnauthorized(billingAddress.getId(), billingAddress.getUserEmail());
        billingAddressDataProvider.updateBillingAddress(billingAddress);

    }

    @Override
    public void deleteBillingAddress(String billingAddressId) {
        exitIfNoBillingAddressFound(billingAddressId);
        billingAddressDataProvider.deleteBillingAddress(billingAddressId);
    }

    private void exitIfNoBillingAddressFound(String billingAddressId) {
        billingAddressQueryService.getBillingAddressById(billingAddressId);
    }

    private void exitIfUnauthorized(String billingAddressId, String loggedInUserEmail) {
        BillingAddress billingAddress = billingAddressQueryService.getBillingAddressById(billingAddressId);
        if(!billingAddress.getUserEmail().equalsIgnoreCase(billingAddressId)) {
            log.error("UnauthorizedException in BillingAddressCommandService.updateBillingAddress: User {} is not authorized to update the billing address {}", loggedInUserEmail, billingAddressId);
            throw new UnauthorizedException("User " + loggedInUserEmail + " is not authorized to update the billing address " + billingAddressId);
        }
    }
}
