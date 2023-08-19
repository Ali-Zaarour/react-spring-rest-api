insert into app_user_role (key)
values
    ('ADMIN'),
    ('USER'),
    ('MANAGER')
on conflict do nothing;