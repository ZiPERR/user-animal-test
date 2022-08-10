CREATE TABLE IF NOT EXISTS public.roles
(
    id bigint NOT NULL,
    name character varying(45) NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);