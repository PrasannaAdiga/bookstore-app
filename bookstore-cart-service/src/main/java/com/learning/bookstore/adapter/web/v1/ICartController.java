package com.learning.bookstore.adapter.web.v1;

import com.learning.bookstore.adapter.web.exception.response.RestApiResponseErrorMessage;
import com.learning.bookstore.adapter.web.v1.response.CartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/carts")
public interface ICartController {

    @PostMapping
    @PreAuthorize("hasAnyRole('Buyer','Admin')")
    @Operation(summary = "Create a new cart", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "403", description = "Not Authorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<?> createCart(Jwt principal);

    @GetMapping
    @PreAuthorize("hasAnyRole('Buyer','Admin')")
    @Operation(summary = "Get buyer cart", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<CartResponse> getCart(Jwt principal);

}
