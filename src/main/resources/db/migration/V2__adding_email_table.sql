create table email_notification (created_at timestamp(6), delivery_job_id bigint not null, id bigserial not null, updated_at timestamp(6), created_by varchar(255), status varchar(255), type varchar(255), modified_by varchar(255), message_handler_request_id varchar(255), primary key (id));
alter table if exists email_notification add constraint email_notification_delivery_job_fk foreign key (delivery_job_id) references delivery_job;
alter table if exists delivery_job add column email_subject varchar(255);
alter table if exists profile add column email_subject_template varchar(255);
