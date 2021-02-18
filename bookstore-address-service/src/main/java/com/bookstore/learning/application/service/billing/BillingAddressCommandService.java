package com.bookstore.learning.application.service.billing;

import com.bookstore.learning.application.exception.UnauthorizedException;
import com.bookstore.learning.application.port.in.billing.IBillingAddressCommandService;
import com.bookstore.learning.application.port.in.billing.IBillingAddressQueryService;
import com.bookstore.learning.application.port.out.IBillingAddressDataProvider;
import com.bookstore.learning.domain.BillingAddress;
import com.bookstore.learning.infrastructure.util.PrincipalResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class BillingAddressCommandService implements IBillingAddressCommandService {
    private final IBillingAddressDataProvider billingAddressDataProvider;
    private final IBillingAddressQueryService billingAddressQueryService;
    private final PrincipalResolver principalResolver;

    @Override
    public String createBillingAddress(BillingAddress billingAddress) {
        exitIfUnauthorized(billingAddress.getUserEmail());
        return billingAddressDataProvider.createBillingAddress(billingAddress);
    }

    @Override
    public void updateBillingAddress(BillingAddress billingAddress) {
        exitIfNoBillingAddressFoundOrUnauthorized(billingAddress.getId());
        billingAddressDataProvider.updateBillingAddress(billingAddress);

    }

    @Override
    public void deleteBillingAddress(String billingAddressId) {
        exitIfNoBillingAddressFoundOrUnauthorized(billingAddressId);
        billingAddressDataProvider.deleteBillingAddress(billingAddressId);
    }

    private void exitIfUnauthorized(String userEmail) {
        if(!userEmail.equalsIgnoreCase(principalResolver.getCurrentLoggedInUserMail())) {
            log.error("UnauthorizedException in BillingAddressCommandService.createBillingAddress: Current user with mail {} is trying to create an billing address for the user mail {}",
                    principalResolver.getCurrentLoggedInUserMail(), userEmail);
            throw new UnauthorizedException("Current user with mail " + principalResolver.getCurrentLoggedInUserMail() + " is trying to create an billing address for the user mail " + userEmail);
        }
    }

    private void exitIfNoBillingAddressFoundOrUnauthorized(String billingAddressId) {
        billingAddressQueryService.getBillingAddressById(billingAddressId);
    }

}
