drop table profile_completion_bcc_contacts;
drop table profile_completion_cc_contacts;
drop table profile_completion_contacts;
drop table profile_failure_contacts;

create table profile_contact (id bigserial not null, contact_id bigint not null, profile_id bigint not null, notify_on varchar(255), send_type varchar(255), primary key (id));
create table delivery_job_contact (id bigserial not null, contact_id bigint not null, delivery_job_id bigint not null, notify_on varchar(255), send_type varchar(255), primary key (id));


alter table if exists profile_contact add constraint profile_contacts_contact_fk foreign key (contact_id) references contact;
alter table if exists profile_contact add constraint profile_contacts_profile_fk foreign key (profile_id) references profile;

alter table if exists delivery_job_contact add constraint delivery_job_contacts_contact_fk foreign key (contact_id) references contact;
alter table if exists delivery_job_contact add constraint delivery_job_contacts_delivery_job_fk foreign key (delivery_job_id) references delivery_job;