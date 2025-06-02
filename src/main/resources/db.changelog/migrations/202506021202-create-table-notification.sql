--liquibase formatted sql
--changeset mkolesnikov:create-table-notification runOnChange:false failOnError:true

CREATE TABLE IF NOT EXISTS formator.notifications
(
    id         UUID PRIMARY KEY         NOT NULL,
    user_id    UUID                     NOT NULL,
    message    TEXT                     NOT NULL,
    is_read    BOOLEAN                  NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES formator.users (id)
);