CREATE TABLE IF NOT EXISTS public.user_animals
(
    animal_id bigint,
    user_id bigint NOT NULL,
    CONSTRAINT user_animals_pkey PRIMARY KEY (user_id),
    CONSTRAINT fkknw4b0ilrf1tdpnskxl6aghl4 FOREIGN KEY (user_id)
        REFERENCES public.animals (animal_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkurn0orn74j2oyk80ocf63x0e FOREIGN KEY (animal_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)