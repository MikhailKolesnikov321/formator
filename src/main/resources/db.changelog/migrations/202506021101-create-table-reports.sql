--liquibase formatted sql
--changeset mkolesnikov:create-table-reports runOnChange:false failOnError:true

CREATE TABLE IF NOT EXISTS formator.reports
(
    id                         UUID PRIMARY KEY         NOT NULL,
    student_id                 UUID                     NOT NULL,
    organization_supervisor_id UUID,
    university_supervisor_id   UUID,
    status                     VARCHAR(32)              NOT NULL CHECK (status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'REJECTED')),
    submitted_at               DATE                     NOT NULL,
    created_at                 TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (student_id) REFERENCES formator.users (id),
    FOREIGN KEY (organization_supervisor_id) REFERENCES formator.users (id),
    FOREIGN KEY (university_supervisor_id) REFERENCES formator.users (id)
);

--liquibase formatted sql
--changeset mkolesnikov:create-table-reports_task runOnChange:false failOnError:true

CREATE TABLE IF NOT EXISTS formator.report_task
(
    report_id  UUID    NOT NULL,
    task_id    UUID    NOT NULL,
    task_order INTEGER NOT NULL,
    PRIMARY KEY (report_id, task_id),
    FOREIGN KEY (report_id) REFERENCES formator.reports (id),
    FOREIGN KEY (task_id) REFERENCES formator.tasks (id)
);

--liquibase formatted sql
--changeset mkolesnikov:create-table-report_task_answer runOnChange:false failOnError:true

CREATE TABLE IF NOT EXISTS formator.report_task_answer
(
    report_id UUID NOT NULL,
    task_id   UUID NOT NULL,
    answer    TEXT,
    PRIMARY KEY (report_id, task_id),
    FOREIGN KEY (report_id) REFERENCES formator.reports (id),
    FOREIGN KEY (task_id) REFERENCES formator.tasks (id)
);
