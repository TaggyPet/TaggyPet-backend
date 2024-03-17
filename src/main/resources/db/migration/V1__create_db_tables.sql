create sequence user_seq start with 3 increment by 1;
create sequence pet_images_seq start with 1 increment by 50;
create sequence pets_seq start with 1 increment by 50;

create table deleted_users
(
    user_id          bigint not null,
    email            varchar(255),
    phone_number     varchar(255),
    first_name       text,
    date_of_creation timestamp(6),
    date_of_deletion timestamp(6),
    primary key (user_id)
);
create table feature_properties
(
    property_id    bigserial not null,
    property_value varchar(255),
    primary key (property_id)
);
create table features
(
    feature_id  bigserial not null,
    property_id bigint,
    description text,
    date        date,
    user_id     bigint,
    primary key (feature_id)
);
create table pet_images
(
    image_id        bigint not null,
    image_size      bigint,
    image_type      varchar(255),
    image_uuid_name varchar(255),
    image_data      oid,
    primary key (image_id)
);
create table pets
(
    pet_id   bigint not null,
    chip_id  varchar(255) unique,
    stamp_id varchar(255) unique,
    pet_name varchar(255),
    type     varchar(255),
    breed    varchar(255),
    sex      smallint check (sex between 0 and 1),
    image_id bigint unique,
    primary key (pet_id)
);
create table pets_features
(
    feature_id bigint not null,
    pet_id     bigint not null
);
create table social_networks
(
    property_id   bigserial not null,
    base_url      varchar(255),
    property_name varchar(255),
    primary key (property_id)
);
create table users
(
    user_id                         bigserial not null,
    email                           varchar(255) unique,
    phone_number                    varchar(255),
    first_name                      text,
    active                          boolean,
    date_of_creation                timestamp(6),
    permission_to_show_email        boolean,
    permission_to_show_phone_number boolean,
    password                        varchar(128),
    role   varchar(255) check (role in ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PRIVILEGED_ACCESS')),
    primary key (user_id)
);
create table users_pets
(
    pet_id  bigint not null,
    user_id bigint not null
);
create table users_social_networks
(
    property_id       bigint,
    social_network_id bigserial not null,
    user_id           bigint,
    short_name        varchar(255),
    primary key (social_network_id)
);
create table pet_clubs
(
    club_id       bigserial not null,
    club_name         varchar(255),
    club_prefix       varchar(255),
    primary key (club_id)
);

alter table if exists features
    add constraint feature_properties_features_fk foreign key (property_id) references feature_properties;

alter table if exists features
    add constraint users_features_fk foreign key (user_id) references users;

alter table if exists pets
    add constraint pet_images_pets_fk foreign key (image_id) references pet_images;

alter table if exists pets_features
    add constraint features_pets_features_fk foreign key (feature_id) references features;

alter table if exists pets_features
    add constraint pets_pet_features_fk foreign key (pet_id) references pets;

alter table if exists users_pets
    add constraint pets_users_pets_fk foreign key (pet_id) references pets;

alter table if exists users_pets
    add constraint users_users_pets_fk foreign key (user_id) references users;

alter table if exists users_social_networks
    add constraint social_networks_users_social_networks_fk foreign key (property_id) references social_networks;

alter table if exists users_social_networks
    add constraint users_users_social_networks_fk foreign key (user_id) references users;
