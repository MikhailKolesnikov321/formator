--liquibase formatted sql
--changeset mkolesnikov:create-formater-schema runOnChange:false failOnError:true

CREATE SCHEMA IF NOT EXISTS formator;