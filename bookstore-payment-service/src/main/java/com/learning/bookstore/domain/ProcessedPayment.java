package com.learning.bookstore.domain;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProcessedPayment {
    @NotBlank(message = "Payment Id should not be blank")
    private String paymentId;

    @NotBlank(message = "Payment Date should not be blank")
    private LocalDateTime paymentDate;

    private boolean captured;

    @NotBlank(message = "Receipt Url should not be blank")
    private String receiptUrl;
}
