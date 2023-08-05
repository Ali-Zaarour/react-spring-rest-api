create table if not exists app_user_status(
    id uuid not null default uuid_generate_v4(),
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp,
    deleted_at timestamp,
    key varchar(50) not null,

    constraint app_user_status_pkey primary key (id),
    constraint app_user_status_key_key unique (key)
);

comment on table app_user_status is 'the user account status';

create table if not exists app_user (
    id uuid not null default uuid_generate_v4(),
    created_at timestamp not null default now(),
    updated_at timestamp,
    deleted_at timestamp,
    username varchar(200) NOT NULL,
    password varchar(115),
    status_id uuid,
    active boolean not null default false,
    last_login_at timestamp,
    is_verified boolean default false,
    verification_token varchar(100),
    reset_token varchar(100),

    constraint app_user_pkey primary key (id),
    constraint app_user_status_id_fkey foreign key (status_id) references app_user_status
);

comment on table app_user is 'The app user is the table that contains all the login info';