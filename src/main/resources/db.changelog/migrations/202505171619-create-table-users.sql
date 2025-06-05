--liquibase formatted sql
--changeset mkolesnikov:create-table-users runOnChange:false failOnError:true

CREATE TABLE IF NOT EXISTS formator.users(
    id UUID PRIMARY KEY NOT NULL,
    organization TEXT NOT NULL,
    email TEXT NOT NULL,
    full_name TEXT NOT NULL,
    role TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);