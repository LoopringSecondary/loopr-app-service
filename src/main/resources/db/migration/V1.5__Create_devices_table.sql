CREATE TABLE devices (
	id serial PRIMARY KEY,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone DEFAULT now(),
    address varchar(1024) not null,
	bundleIdentifier varchar(128) not null,
	deviceToken TEXT NOT NULL,
    isEnabled boolean NOT NULL DEFAULT true
);