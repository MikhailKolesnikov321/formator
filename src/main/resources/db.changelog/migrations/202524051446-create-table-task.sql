--liquibase formatted sql
--changeset mkolesnikov:create-table-task runOnChange:false failOnError:true

CREATE TABLE tasks
(
    id                         UUID PRIMARY KEY NOT NULL ,
    organization_supervisor_id UUID         NOT NULL,
    title                      VARCHAR(100) NOT NULL,
    description                TEXT         NOT NULL,
    start_at                   TIMESTAMPTZ  NOT NULL,
    end_at                     TIMESTAMPTZ  NOT NULL,
    created_at                 TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_user_id FOREIGN KEY (organization_supervisor_id) REFERENCES formator.users(id)
);
