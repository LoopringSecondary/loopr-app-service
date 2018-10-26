CREATE TABLE users (
	id serial PRIMARY KEY,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone DEFAULT now(),
	account_token TEXT NOT NULL,
	config json NOT NULL default '{}'::jsonb
);