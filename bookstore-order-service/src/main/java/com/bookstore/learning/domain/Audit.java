package com.bookstore.learning.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
    protected String createdBy;
    protected Date creationDate;
    protected String lastModifiedBy;
    protected Date lastModifiedDate;
}
