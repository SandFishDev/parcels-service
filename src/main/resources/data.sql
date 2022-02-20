create table if not exists role
(
    id bigint not null auto_increment,
    description varchar(255),
    name varchar(255),
    primary key (id)
);

create table if not exists user_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
);

insert into role (id, description, name) VALUES (1, 'Admin role', 'ADMIN');
insert into role (id, description, name) VALUES (2, 'User role', 'USER');
