--liquibase formatted sql
--changeset mkolesnikov:create-table-comments runOnChange:false failOnError:true

CREATE TABLE formator.comments
(
    id         UUID PRIMARY KEY         NOT NULL,
    report_id  UUID                     NOT NULL,
    author_id  UUID                     NOT NULL,
    text       TEXT                     NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    FOREIGN KEY (report_id) REFERENCES formator.reports (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES formator.users (id)
);