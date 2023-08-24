/*remove role_id column + constraint fkey from app_user table*/
alter table if exists app_user
drop column role_id,
drop constraint if exists app_user_role_id_fkey ;

/*create table app_user_permission*/
create table  if not exists app_user_permission(
    id uuid not null default uuid_generate_v4(),
    created_at timestamp not null default now(),
    updated_at timestamp,
    deleted_at timestamp,
    key varchar(50) not null,

    constraint app_user_permission_pkey primary key (id),
    constraint app_user_permission_key_key unique (key)
);


/*create app_user_role_mapping*/
create table if not exists  app_user_role_mapping(
    id uuid not null  default  uuid_generate_v4(),
    created_at timestamp not null default now(),
    updated_at timestamp,
    deleted_at timestamp,
    app_user_id uuid,
    role_id  uuid,

    constraint app_user_role_mapping_pkey primary key (id),
    constraint app_user_role_mapping_app_user_id_fkey foreign key  (app_user_id) references  app_user (id),
    constraint app_user_role_mapping_role_id_fkey foreign key (role_id) references app_user_role (id),
    constraint app_user_role_mapping_app_user_id_role_id_key unique (app_user_id,role_id)
);

/*create table app_user_permission_mapping*/
create table  if not exists  app_user_permission_mapping(
    id uuid not null default  uuid_generate_v4(),
    created_at timestamp not null default now(),
    updated_at timestamp,
    deleted_at timestamp,
    role_mapping_id uuid,
    permission_id uuid,

    constraint  app_user_permission_mapping_pkey primary key (id),
    constraint  app_user_permission_mapping_role_mapping_id_fkey foreign key  (role_mapping_id) references  app_user_role_mapping (id),
    constraint  app_user_permission_mapping_permission_id_fkey foreign key  (permission_id) references  app_user_permission (id),
    constraint  app_user_role_mapping_role_mapping_id_permission_id_key unique (role_mapping_id,permission_id)
);