CREATE TABLE messages (
	id serial PRIMARY KEY,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone DEFAULT now(),
	bundle_identifier varchar(128) not null,
	device_token TEXT NOT NULL,
    alert_body TEXT NOT NULL
);