--liquibase formatted sql
--changeset mkolesnikov:alter-table-users runOnChange:false failOnError:true

ALTER TABLE
    formator.users
ADD
    password TEXT;