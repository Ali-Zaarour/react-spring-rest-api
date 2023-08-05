insert into app_user_status (key)
values
      ('PENDING_CONFIRMATION'),
      ('ACTIVATED'),
      ('SUSPENDED'),
      ('DELETED')
on conflict do nothing;