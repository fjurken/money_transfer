--liquibase formatted sql

--changeset fomichev:add-data-to-table

insert into accounts (id, created, updated, balance, status)
values
    (1000000001, current_timestamp(), current_timestamp(), 1000000, 'ACTIVE' ),
    (1000000002, current_timestamp(), current_timestamp(), 900000, 'ACTIVE' ),
    (1000000003, current_timestamp(), current_timestamp(), 210000, 'ACTIVE' ),
    (1000000004, current_timestamp(), current_timestamp(), 45000, 'ACTIVE' ),
    (1000000005, current_timestamp(), current_timestamp(), 112000, 'ACTIVE' ),
    (1000000006, current_timestamp(), current_timestamp(), 505000, 'ACTIVE' );