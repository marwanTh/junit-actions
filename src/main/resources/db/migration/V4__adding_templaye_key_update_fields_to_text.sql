alter table if exists profile add column completion_template_key varchar(256);
alter table if exists profile add column failure_template_key varchar(256);

alter table if exists delivery_job_contact add column created_by varchar(256);
alter table if exists delivery_job_contact add column modified_by varchar(256);
alter table if exists delivery_job_contact add column created_at timestamp(6);
alter table if exists delivery_job_contact add column updated_at timestamp(6);

alter table if exists profile_contact add column created_by varchar(256);
alter table if exists profile_contact add column modified_by varchar(256);
alter table if exists profile_contact add column created_at timestamp(6);
alter table if exists profile_contact add column updated_at timestamp(6);


ALTER TABLE asset ALTER COLUMN path TYPE text;
ALTER TABLE connection ALTER COLUMN description TYPE text;
ALTER TABLE delivery_job ALTER COLUMN email_subject TYPE text;
ALTER TABLE profile ALTER COLUMN description TYPE text;
ALTER TABLE profile ALTER COLUMN email_subject_template TYPE text;
ALTER TABLE profile ALTER COLUMN root_path TYPE text;