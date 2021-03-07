package com.learning.bookstore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@SuperBuilder
@NoArgsConstructor
public class Audit {
    protected String createdBy;
    protected Date creationDate;
    protected String lastModifiedBy;
    protected Date lastModifiedDate;
}
