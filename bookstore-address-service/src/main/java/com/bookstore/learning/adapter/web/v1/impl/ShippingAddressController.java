package com.bookstore.learning.adapter.web.v1.impl;

import com.bookstore.learning.adapter.web.v1.IShippingAddressController;
import com.bookstore.learning.adapter.web.v1.request.AddressRequest;
import com.bookstore.learning.adapter.web.v1.request.CreateShippingAddressRequest;
import com.bookstore.learning.adapter.web.v1.request.UpdateShippingAddressRequest;
import com.bookstore.learning.adapter.web.v1.response.ShippingAddressResponse;
import com.bookstore.learning.application.port.in.shipping.IShippingAddressCommandService;
import com.bookstore.learning.application.port.in.shipping.IShippingAddressQueryService;
import com.bookstore.learning.domain.ShippingAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShippingAddressController implements IShippingAddressController {
    private final IShippingAddressCommandService shippingAddressCommandService;
    private final IShippingAddressQueryService shippingAddressQueryService;

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createShippingAddress(CreateShippingAddressRequest createShippingAddressRequest) {
        ShippingAddress shippingAddress = buildShippingAddress(createShippingAddressRequest);
        String shippingAddressId = shippingAddressCommandService.createShippingAddress(shippingAddress);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(shippingAddressId).toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<?> updateShippingAddress(UpdateShippingAddressRequest updateShippingAddressRequest) {
        ShippingAddress shippingAddress = buildShippingAddress(updateShippingAddressRequest);
        shippingAddressCommandService.updateShippingAddress(shippingAddress);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteShippingAddress(String id) {
        shippingAddressCommandService.deleteShippingAddress(id);
        return ResponseEntity.noContent().build();
    }

    private ShippingAddress buildShippingAddress(AddressRequest addressRequest) {
        return ShippingAddress.builder()
                .id(addressRequest instanceof UpdateShippingAddressRequest ? ((UpdateShippingAddressRequest) addressRequest).getId() : null)
                .userEmail(addressRequest.getUserEmail())
                .addressLine1(addressRequest.getAddressLine1())
                .addressLine2(addressRequest.getAddressLine2())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .postalCode(addressRequest.getPostalCode())
                .country(addressRequest.getCountry())
                .phone(addressRequest.getPhone())
                .build();
    }

    /*============================================ Query Section ================================================*/

    @Override
    public ResponseEntity<ShippingAddressResponse> getShippingAddressById(String id) {
        ShippingAddress shippingAddress = shippingAddressQueryService.getShippingAddressById(id);
        return ResponseEntity.ok().body(buildShippingAddressResponse(shippingAddress));
    }

    @Override
    public ResponseEntity<List<ShippingAddressResponse>> getAllShippingAddressOfUser() {
        List<ShippingAddressResponse> shippingAddressResponses = new ArrayList<>();
        List<ShippingAddress> shippingAddresses = shippingAddressQueryService.getAllShippingAddressOfLoggedInUser();
        shippingAddresses.stream().forEach(sAddress -> {
            shippingAddressResponses.add(buildShippingAddressResponse(sAddress));
        });
        return ResponseEntity.ok().body(shippingAddressResponses);
    }

    private ShippingAddressResponse buildShippingAddressResponse(ShippingAddress shippingAddress) {
        return ShippingAddressResponse.builder()
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

}
