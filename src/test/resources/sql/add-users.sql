insert into users (id, username, password, enabled) values
(1, 'john_doe@mail.com', '$2a$12$zNwV0anPsveKM4kJdPKSG.YVjF9FM.dXEVB8KfrJgYDTz0ExC1ds.', true),
(2, 'jane_doe@mail.com', '$2a$12$zNwV0anPsveKM4kJdPKSG.YVjF9FM.dXEVB8KfrJgYDTz0ExC1ds.', true),
(3, 'janie_doe@mail.com', '$2a$12$zNwV0anPsveKM4kJdPKSG.YVjF9FM.dXEVB8KfrJgYDTz0ExC1ds.', true);
insert into roles (user_id, role) values
(1, 'ADMINISTRATOR'),
(2, 'JOURNALIST'),
(3, 'USER');