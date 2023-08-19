create table if not exists app_user_role(
    id uuid not null default uuid_generate_v4(),
    created_at timestamp not null default now(),
    updated_at timestamp,
    deleted_at timestamp,
    key varchar(50) not null,

    constraint app_user_role_pkey primary key (id),
    constraint app_user_role_key_key unique (key)
);


alter table if exists  app_user
add column first_name varchar(75),
add column last_name varchar(75),
add column birth_date date,
add column address varchar(200),
add column phone_number varchar(50),
add column role_id uuid,

add constraint app_user_role_id_fkey foreign key (role_id) references app_user_role;