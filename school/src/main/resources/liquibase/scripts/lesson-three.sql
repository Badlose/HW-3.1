--liquibase formatted sql

--changeset isolomin:1
create index studets_name_idx on student (name);
create index faculties_name_and_color_idx on faculty (name, color);
