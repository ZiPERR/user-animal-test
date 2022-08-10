CREATE TABLE IF NOT EXISTS public.animals
(
    animal_id bigint,
    birth_date date,
    nickname character varying(255) COLLATE pg_catalog."default",
    sex character varying(255) COLLATE pg_catalog."default",
    species character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT animals_pkey PRIMARY KEY (animal_id),
    CONSTRAINT uk_jxi16tm46k5cctxa896ntkec8 UNIQUE (nickname)
)