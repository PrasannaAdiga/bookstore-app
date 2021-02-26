package com.learning.bookstore.application.service.shipping;

import com.learning.bookstore.application.exception.UnauthorizedException;
import com.learning.bookstore.application.port.in.shipping.IShippingAddressCommandService;
import com.learning.bookstore.application.port.in.shipping.IShippingAddressQueryService;
import com.learning.bookstore.application.port.out.IShippingAddressDataProvider;
import com.learning.bookstore.domain.ShippingAddress;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
public class ShippingAddressCommandService implements IShippingAddressCommandService {
    private final IShippingAddressDataProvider shippingAddressDataProvider;
    private final IShippingAddressQueryService shippingAddressQueryService;
    private final PrincipalResolver principalResolver;

    @Override
    public String createShippingAddress(ShippingAddress shippingAddress) {
        exitIfUnauthorized(shippingAddress.getUserEmail());
        return shippingAddressDataProvider.createShippingAddress(shippingAddress);
    }

    @Override
    public void updateShippingAddress(ShippingAddress shippingAddress) {
        exitIfNoShippingAddressFoundOrUnauthorized(shippingAddress.getId());
        shippingAddressDataProvider.updateShippingAddress(shippingAddress);
    }

    @Override
    public void deleteShippingAddress(String shippingAddressId) {
        exitIfNoShippingAddressFoundOrUnauthorized(shippingAddressId);
        shippingAddressDataProvider.deleteShippingAddress(shippingAddressId);
    }

    private void exitIfUnauthorized(String userEmail) {
        if(!userEmail.equalsIgnoreCase(principalResolver.getCurrentLoggedInUserMail())) {
            log.error("UnauthorizedException in ShippingAddressCommandService.createShippingAddress: Current user with mail {} is trying to create an shipping address for the user mail {}",
                    principalResolver.getCurrentLoggedInUserMail(), userEmail);
            throw new UnauthorizedException("Current user with mail " + principalResolver.getCurrentLoggedInUserMail() + " is trying to create an shipping address for the user mail " + userEmail);
        }
    }

    private void exitIfNoShippingAddressFoundOrUnauthorized(String shippingAddressId) {
        shippingAddressQueryService.getShippingAddressById(shippingAddressId);
    }

}
