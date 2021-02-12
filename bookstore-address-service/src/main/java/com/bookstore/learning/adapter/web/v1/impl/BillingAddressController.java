package com.bookstore.learning.adapter.web.v1.impl;

import com.bookstore.learning.adapter.web.v1.IBillingAddressController;
import com.bookstore.learning.adapter.web.v1.request.AddressRequest;
import com.bookstore.learning.adapter.web.v1.request.CreateBillingAddressRequest;
import com.bookstore.learning.adapter.web.v1.request.UpdateBillingAddressRequest;
import com.bookstore.learning.adapter.web.v1.response.BillingAddressResponse;
import com.bookstore.learning.application.port.in.billing.IBillingAddressCommandService;
import com.bookstore.learning.application.port.in.billing.IBillingAddressQueryService;
import com.bookstore.learning.domain.BillingAddress;
import com.bookstore.learning.infrastructure.constant.AddressServiceConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    public ResponseEntity<?> createBillingAddress(@Valid CreateBillingAddressRequest createBillingAddressRequest, Jwt principal) {
        String loggedInUserEmail = principal.getClaimAsString(AddressServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
        BillingAddress billingAddress = buildBillingAddress(createBillingAddressRequest, loggedInUserEmail);
        String billingAddressId = billingAddressCommandService.createBillingAddress(billingAddress);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(billingAddressId).toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<?> updateBillingAddress(@Valid UpdateBillingAddressRequest updateBillingAddressRequest, Jwt principal) {
        String loggedInUserEmail = principal.getClaimAsString(AddressServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
        BillingAddress billingAddress = buildBillingAddress(updateBillingAddressRequest, loggedInUserEmail);
        billingAddressCommandService.updateBillingAddress(billingAddress);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteBillingAddress(@NotBlank(message = "Billing address id should not be blank")
                                                              String id) {
        billingAddressCommandService.deleteBillingAddress(id);
        return ResponseEntity.noContent().build();
    }

    private BillingAddress buildBillingAddress(AddressRequest addressRequest, String loggedInUserEmail) {
        return BillingAddress.builder()
                .id(addressRequest instanceof UpdateBillingAddressRequest ? ((UpdateBillingAddressRequest) addressRequest).getId() : null)
                .userEmail(loggedInUserEmail)
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
    public ResponseEntity<BillingAddressResponse> getBillingAddressById(@NotBlank(message = "Billing address id should not be blank")
                                                                                    String id) {
        BillingAddress billingAddress = billingAddressQueryService.getBillingAddressById(id);
        return ResponseEntity.ok().body(buildBillingAddressResponse(billingAddress));
    }

    @Override
    public ResponseEntity<List<BillingAddressResponse>> getAllBillingAddressOfUser(Jwt principal) {
        List<BillingAddressResponse> billingAddressResponses = new ArrayList<>();
        String loggedInUserEmail = principal.getClaimAsString(AddressServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
        List<BillingAddress> billingAddress = billingAddressQueryService.getAllBillingAddressOfUser(loggedInUserEmail);
        billingAddress.stream().forEach(bAddress -> {
            billingAddressResponses.add(buildBillingAddressResponse(bAddress));
        });
        return ResponseEntity.ok().body(billingAddressResponses);
    }

    private BillingAddressResponse buildBillingAddressResponse(BillingAddress billingAddress) {
        return BillingAddressResponse.builder()
                .id(billingAddress.getId())
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
