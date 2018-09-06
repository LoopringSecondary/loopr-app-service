CREATE TABLE devices (
	id serial PRIMARY KEY,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone DEFAULT now(),
    address varchar(1024) not null,
	bundle_identifier varchar(128) not null,
	device_token TEXT NOT NULL,
    is_enabled boolean NOT NULL DEFAULT true,
    is_release_mode boolean NOT NULL DEFAULT false
);