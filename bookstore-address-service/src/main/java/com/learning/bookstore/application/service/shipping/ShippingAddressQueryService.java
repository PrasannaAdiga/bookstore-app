package com.learning.bookstore.application.service.shipping;

import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.exception.UnauthorizedException;
import com.learning.bookstore.application.port.in.shipping.IShippingAddressQueryService;
import com.learning.bookstore.application.port.out.IShippingAddressDataProvider;
import com.learning.bookstore.domain.ShippingAddress;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class ShippingAddressQueryService implements IShippingAddressQueryService {
    private final IShippingAddressDataProvider shippingAddressDataProvider;
    private final PrincipalResolver principalResolver;

    @Override
    public List<ShippingAddress> getAllShippingAddressOfLoggedInUser() {
        Optional<List<ShippingAddress>> oShippingAddress = shippingAddressDataProvider.getAllShippingAddressOfUser(principalResolver.getCurrentLoggedInUserMail());
        exitIfNoShippingAddressFound(oShippingAddress);
        return oShippingAddress.get();
    }

    @Override
    public ShippingAddress getShippingAddressById(String shippingAddressId) {
        Optional<ShippingAddress> oShippingAddress = shippingAddressDataProvider.getShippingAddressById(shippingAddressId);
        exitIfNoShippingAddressFoundForId(oShippingAddress, shippingAddressId);
        exitIfUnauthorized(oShippingAddress, shippingAddressId);
        return oShippingAddress.get();
    }

    private void exitIfNoShippingAddressFound(Optional<List<ShippingAddress>> oShippingAddress) {
        oShippingAddress.orElseThrow(() -> {
            log.error("ResourceNotFoundException in ShippingAddressQueryService.getAllShippingAddressOfUser: No shipping address found for the user {}", principalResolver.getCurrentLoggedInUserMail());
            throw new ResourceNotFoundException("No shipping address found for the user " + principalResolver.getCurrentLoggedInUserMail());
        });
    }

    private void exitIfNoShippingAddressFoundForId(Optional<ShippingAddress> oShippingAddress, String shippingAddressId) {
        oShippingAddress.orElseThrow(() -> {
            log.error("ResourceNotFoundException in ShippingAddressQueryService.getShippingAddressById: Shipping Address with id {} not found", shippingAddressId);
            throw new ResourceNotFoundException("Shipping Address with id " + shippingAddressId + " not found!");
        });
    }

    private void exitIfUnauthorized(Optional<ShippingAddress> oShippingAddress, String shippingAddressId) {
        if(!oShippingAddress.get().getUserEmail().equalsIgnoreCase(principalResolver.getCurrentLoggedInUserMail())) {
            log.error("UnauthorizedException in ShippingAddressQueryService.getShippingAddressById: Shipping Address with id {} is not belong to current logged in user {}",
                    shippingAddressId, principalResolver.getCurrentLoggedInUserMail());
            throw new UnauthorizedException("Shipping Address with id " + shippingAddressId + " is not belong to current logged in user " + principalResolver.getCurrentLoggedInUserMail());
        }
    }

}
