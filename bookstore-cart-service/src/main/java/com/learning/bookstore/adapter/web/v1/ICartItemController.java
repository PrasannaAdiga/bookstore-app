package com.learning.bookstore.adapter.web.v1;

import com.learning.bookstore.adapter.web.exception.response.RestApiResponseErrorMessage;
import com.learning.bookstore.adapter.web.v1.request.CreateCartItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequestMapping("/v1/carts")
@Validated
public interface ICartItemController {
    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('Buyer','Admin')")
    @Operation(summary = "Create a new cart item", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Not Authorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<?> createCartItem(@RequestBody @Valid CreateCartItemRequest createCartItemRequest);

    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasAnyRole('Buyer','Admin')")
    @Operation(summary = "Delete an existing product", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Not Authorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<?> removeCartItem(@Parameter(description = "Provide a valid cart item ID")
                                     @PathVariable(value = "id")
                                     @NotBlank(message = "Cart item ID should not be blank")
                                             String id);

    @DeleteMapping("/items")
    @PreAuthorize("hasAnyRole('Buyer','Admin')")
    @Operation(summary = "Delete an existing product", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Not Authorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<?> removeAllCartItem(@RequestParam(value = "cartId", required = true) String cartId);

}
