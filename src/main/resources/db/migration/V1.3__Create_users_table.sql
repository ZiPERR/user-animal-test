CREATE TABLE IF NOT EXISTS public.users
(
    user_id bigint,
    password character varying(255),
    username character varying(255),
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT uk3r2pikgsxo64e847ik4b0gba1 UNIQUE (username)
)