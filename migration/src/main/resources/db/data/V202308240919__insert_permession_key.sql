insert into app_user_permission (key)
values ('CREATE'),
        ('READ'),
        ('UPDATE'),
        ('DELETE')
ON CONFLICT DO NOTHING;