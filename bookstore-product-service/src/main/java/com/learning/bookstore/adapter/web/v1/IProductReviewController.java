package com.learning.bookstore.adapter.web.v1;

import com.learning.bookstore.adapter.web.exception.response.RestApiResponseErrorMessage;
import com.learning.bookstore.application.port.in.review.request.CreateOrUpdateReviewRequest;
import com.learning.bookstore.application.port.in.review.response.ProductReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequestMapping("/v1/products")
@Validated
public interface IProductReviewController {
    @PostMapping("/reviews")
    @PreAuthorize("hasAnyRole('read','write')")
    @Operation(summary = "Create or update a product review", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<?> createOrUpdateReview(@RequestBody @Valid CreateOrUpdateReviewRequest createOrUpdateReviewRequest);

    @GetMapping("/{id}/reviews")
    @PreAuthorize("hasAnyRole('read','write')")
    @Operation(summary = "Get all reviews for a product", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<Page<ProductReviewResponse>> getAllReviewOfProduct(@Parameter(description = "Provide a valid product ID")
                                                                      @PathVariable("id")
                                                                      @NotBlank(message = "ProductId should not be blank")
                                                                              String productId,
                                                                      @Parameter(description = "Provide a page number")
                                                                      @RequestParam(value = "page", required = false, defaultValue = "0")
                                                                              Integer page,
                                                                      @Parameter(description = "Provide a page size")
                                                                      @RequestParam(value = "size", required = false, defaultValue = "10")
                                                                              Integer size,
                                                                      @Parameter(description = "Provide sort by field name")
                                                                      @RequestParam(value = "sort", required = false, defaultValue = "id")
                                                                              String sort);

}
