--liquibase formatted sql
--changeset mkolesnikov:create-table-task runOnChange:false failOnError:true

CREATE TABLE IF NOT EXISTS formator.tasks (
    id                         UUID PRIMARY KEY NOT NULL ,
    title                      VARCHAR(100) NOT NULL,
    description                TEXT         NOT NULL,
    start_at                   DATE NOT NULL,
    end_at                     DATE NOT NULL,
    created_at                 TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

--liquibase formatted sql
--changeset mkolesnikov:create-table-task-user runOnChange:false failOnError:true

CREATE TABLE IF NOT EXISTS formator.task_user (
    user_id UUID NOT NULL,
    task_id UUID NOT NULL,
    PRIMARY KEY (user_id, task_id),
    FOREIGN KEY (task_id) REFERENCES formator.tasks (id),
    FOREIGN KEY (user_id) REFERENCES formator.users (id)
);
