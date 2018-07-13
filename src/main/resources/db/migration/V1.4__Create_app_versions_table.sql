CREATE TABLE app_versions (
	id serial PRIMARY KEY,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone DEFAULT now(),
	version varchar(128) not null,
    must_update boolean NOT NULL DEFAULT false,
    description TEXT NOT NULL
);