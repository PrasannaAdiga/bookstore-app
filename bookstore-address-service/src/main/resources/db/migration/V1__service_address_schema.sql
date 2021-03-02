create table Billing_Address (
    id varchar(255) not null,
    created_by varchar(255),
    created_date timestamp,
    last_modified_by varchar(255),
    last_modified_date timestamp,
    address_line1 varchar(255) not null,
    address_line2 varchar(255),
    city varchar(255),
    country varchar(255),
    phone varchar(255),
    postal_code varchar(255),
    state varchar(255),
    user_email varchar(255) not null,
    primary key (id)
);

create table Billing_Address_AUD (
    id varchar(255) not null,
    REV integer not null,
    REVTYPE tinyint,
    address_line1 varchar(255),
    address_line2 varchar(255),
    city varchar(255),
    country varchar(255),
    phone varchar(255),
    postal_code varchar(255),
    state varchar(255),
    user_email varchar(255),
    primary key (id, REV)
);

create table REVINFO (
    REV integer generated by default as identity,
    REVTSTMP bigint,
    primary key (REV)
);

create table Shipping_Address (
    id varchar(255) not null,
    created_by varchar(255),
    created_date timestamp,
    last_modified_by varchar(255),
    last_modified_date timestamp,
    address_line1 varchar(255) not null,
    address_line2 varchar(255),
    city varchar(255),
    country varchar(255),
    phone varchar(255),
    postal_code varchar(255),
    state varchar(255),
    user_email varchar(255) not null,
    primary key (id)
);

create table Shipping_Address_AUD (
    id varchar(255) not null,
    REV integer not null,
    REVTYPE tinyint,
    address_line1 varchar(255),
    address_line2 varchar(255),
    city varchar(255),
    country varchar(255),
    phone varchar(255),
    postal_code varchar(255),
    state varchar(255),
    user_email varchar(255),
    primary key (id, REV)
);

alter table Billing_Address_AUD
       add constraint FKhhunltaj604g6sqqf8ti94v05
       foreign key (REV)
       references REVINFO;

alter table Shipping_Address_AUD
       add constraint FKi7rkl01sf53k12fr3n6kli1gt
       foreign key (REV)
       references REVINFO;