package com.learning.bookstore.adapter.web.v1;

import com.learning.bookstore.adapter.web.exception.response.RestApiResponseErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RequestMapping("/v1/products/")
@Validated
public interface IProductImageController {
    @PostMapping(value = "images/upload", params = "imageFile")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Create or update a product image", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "BasicAuth"))
    ResponseEntity<?> uploadImage(@Parameter(description = "Provide a valid image file")
                                  @RequestParam("imageFile")
                                  @NotNull(message = "Image file is mandatory")
                                          MultipartFile file) throws IOException;

    @GetMapping(path = "images/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get an image of a product", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "BasicAuth"))
    ResponseEntity<?> getImage(@Parameter(description = "Provide a valid image id")
                               @PathVariable("id")
                               @NotBlank(message = "Image id is mandatory")
                                       String id) throws IOException;

}
