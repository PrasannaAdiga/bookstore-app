package com.bookstore.learning.adapter.web.v1.impl;

import com.bookstore.learning.adapter.web.v1.IBillingAddressController;
import com.bookstore.learning.adapter.web.v1.request.AddressRequest;
import com.bookstore.learning.adapter.web.v1.request.CreateBillingAddressRequest;
import com.bookstore.learning.adapter.web.v1.request.UpdateBillingAddressRequest;
import com.bookstore.learning.adapter.web.v1.response.BillingAddressResponse;
import com.bookstore.learning.application.port.in.billing.IBillingAddressCommandService;
import com.bookstore.learning.application.port.in.billing.IBillingAddressQueryService;
import com.bookstore.learning.domain.BillingAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BillingAddressController implements IBillingAddressController {
    private final IBillingAddressCommandService billingAddressCommandService;
    private final IBillingAddressQueryService billingAddressQueryService;

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createBillingAddress(CreateBillingAddressRequest createBillingAddressRequest) {
        BillingAddress billingAddress = buildBillingAddress(createBillingAddressRequest);
        String billingAddressId = billingAddressCommandService.createBillingAddress(billingAddress);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(billingAddressId).toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<?> updateBillingAddress(UpdateBillingAddressRequest updateBillingAddressRequest) {
        BillingAddress billingAddress = buildBillingAddress(updateBillingAddressRequest);
        billingAddressCommandService.updateBillingAddress(billingAddress);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteBillingAddress(String id) {
        billingAddressCommandService.deleteBillingAddress(id);
        return ResponseEntity.noContent().build();
    }

    private BillingAddress buildBillingAddress(AddressRequest addressRequest) {
        return BillingAddress.builder()
                .id(addressRequest instanceof UpdateBillingAddressRequest ? ((UpdateBillingAddressRequest) addressRequest).getId() : null)
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
    public ResponseEntity<BillingAddressResponse> getBillingAddressById(String id) {
        BillingAddress billingAddress = billingAddressQueryService.getBillingAddressById(id);
        return ResponseEntity.ok().body(buildBillingAddressResponse(billingAddress));
    }

    @Override
    public ResponseEntity<List<BillingAddressResponse>> getAllBillingAddressOfUser() {
        List<BillingAddressResponse> billingAddressResponses = new ArrayList<>();
        List<BillingAddress> billingAddress = billingAddressQueryService.getAllBillingAddressOfLoggedInUser();
        billingAddress.stream().forEach(bAddress -> {
            billingAddressResponses.add(buildBillingAddressResponse(bAddress));
        });
        return ResponseEntity.ok().body(billingAddressResponses);
    }

    private BillingAddressResponse buildBillingAddressResponse(BillingAddress billingAddress) {
        return BillingAddressResponse.builder()
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

}
