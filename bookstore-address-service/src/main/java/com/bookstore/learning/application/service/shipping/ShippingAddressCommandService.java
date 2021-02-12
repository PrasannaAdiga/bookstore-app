package com.bookstore.learning.application.service.shipping;

import com.bookstore.learning.application.exception.UnauthorizedException;
import com.bookstore.learning.application.port.in.shipping.IShippingAddressCommandService;
import com.bookstore.learning.application.port.in.shipping.IShippingAddressQueryService;
import com.bookstore.learning.application.port.out.IShippingAddressDataProvider;
import com.bookstore.learning.domain.ShippingAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
public class ShippingAddressCommandService implements IShippingAddressCommandService {
    private final IShippingAddressDataProvider shippingAddressDataProvider;
    private final IShippingAddressQueryService shippingAddressQueryService;

    @Override
    public String createShippingAddress(ShippingAddress shippingAddress) {
        return shippingAddressDataProvider.createShippingAddress(shippingAddress);
    }

    @Override
    public void updateShippingAddress(ShippingAddress shippingAddress) {
        exitIfNoShippingAddressFound(shippingAddress.getId());
        exitIfUnauthorized(shippingAddress.getId(), shippingAddress.getUserEmail());
        shippingAddressDataProvider.updateShippingAddress(shippingAddress);
    }

    @Override
    public void deleteShippingAddress(String shippingAddressId) {
        exitIfNoShippingAddressFound(shippingAddressId);
        shippingAddressDataProvider.deleteShippingAddress(shippingAddressId);
    }

    private void exitIfNoShippingAddressFound(String shippingAddressId) {
        shippingAddressQueryService.getShippingAddressById(shippingAddressId);
    }

    private void exitIfUnauthorized(String shippingAddressId, String loggedInUserEmail) {
        ShippingAddress shippingAddress = shippingAddressQueryService.getShippingAddressById(shippingAddressId);
        if(!shippingAddress.getUserEmail().equalsIgnoreCase(shippingAddressId)) {
            log.error("UnauthorizedException in ShippingAddressCommandService.updateShippingAddress: User {} is not authorized to update the shipping address {}", loggedInUserEmail, shippingAddressId);
            throw new UnauthorizedException("User " + loggedInUserEmail + " is not authorized to update the shipping address " + shippingAddressId);
        }
    }

}
