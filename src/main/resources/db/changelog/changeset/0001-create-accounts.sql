--liquibase formatted sql

--changeset fomichev:create-table

create table accounts(
    id BIGINT unique auto_increment not null,
    created timestamp not null,
    updated timestamp default current_timestamp(),
    balance decimal,
    status varchar(10) not null default 'ACTIVE'
)