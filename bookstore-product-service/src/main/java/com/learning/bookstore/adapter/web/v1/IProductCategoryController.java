package com.learning.bookstore.adapter.web.v1;

import com.learning.bookstore.adapter.web.exception.response.RestApiResponseErrorMessage;
import com.learning.bookstore.application.port.in.category.request.CreateProductCategoryRequest;
import com.learning.bookstore.application.port.in.category.request.UpdateProductCategoryRequest;
import com.learning.bookstore.application.port.in.category.response.ProductCategoryResponse;
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
public interface IProductCategoryController {
    @PostMapping("/categories")
    @PreAuthorize("hasRole('Admin')")
    @Operation(summary = "Create a new product category", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "302", description = "Product category already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Not Authorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<?> createProductCategory(@RequestBody @Valid CreateProductCategoryRequest createProductCategoryRequest);

    @PutMapping("/categories")
    @PreAuthorize("hasRole('Admin')")
    @Operation(summary = "Update an existing product category", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Not Authorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<?> updateProductCategory(@RequestBody @Valid UpdateProductCategoryRequest updateProductCategoryRequest);

    @DeleteMapping("categories/{id}")
    @PreAuthorize("hasRole('Admin')")
    @Operation(summary = "Delete an existing product category", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Not Authorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<?> deleteProductCategoryById(@Parameter(description = "Provide a valid product category ID to delete")
                                                @PathVariable("id")
                                                @NotBlank(message = "ProductCategoryId should not be blank")
                                                        String id);

    @GetMapping("categories/{id}")
    @PreAuthorize("hasAnyRole('Buyer','Admin')")
    @Operation(summary = "Get a product category by id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductCategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<ProductCategoryResponse> getProductCategoryById(@Parameter(description = "Provide a valid category ID")
                                                                   @PathVariable("id")
                                                                   @NotBlank(message = "ProductCategoryId should not be blank")
                                                                           String id);

    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('Buyer','Admin')")
    @Operation(summary = "Get all product categories", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductCategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<Page<ProductCategoryResponse>> getAllProductCategory(@Parameter(description = "Provide a page number")
                                                                        @RequestParam(value = "page", required = false, defaultValue = "0")
                                                                                Integer page,
                                                                        @Parameter(description = "Provide a page size")
                                                                        @RequestParam(value = "size", required = false, defaultValue = "10")
                                                                                Integer size,
                                                                        @Parameter(description = "Provide sort by field name")
                                                                        @RequestParam(value = "sort", required = false, defaultValue = "id")
                                                                                String sort);

}
