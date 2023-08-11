alter table if exists app_user
add constraint app_user_unique_username unique (username);