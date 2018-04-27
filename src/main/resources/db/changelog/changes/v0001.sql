CREATE TABLE public.service_instances
(
    instance_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    plan_id character varying(50) COLLATE pg_catalog."default",
    service_definition_id character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT service_instances_pkey PRIMARY KEY (instance_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.service_instances
    OWNER to brokeradmin;

CREATE TABLE public.service_instance_parameters
(
    instance_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    parameter_value character varying(255) COLLATE pg_catalog."default",
    parameter_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT service_instance_parameters_pkey PRIMARY KEY (instance_id, parameter_name),
    CONSTRAINT fkk7m15dagseycbat7epu7xq9ks FOREIGN KEY (instance_id)
        REFERENCES public.service_instances (instance_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.service_instance_parameters
    OWNER to brokeradmin;

CREATE TABLE public.service_bindings
(
    binding_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT service_bindings_pkey PRIMARY KEY (binding_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.service_bindings
    OWNER to brokeradmin;

CREATE TABLE public.service_binding_parameters
(
    binding_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    parameter_value character varying(255) COLLATE pg_catalog."default",
    parameter_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT service_binding_parameters_pkey PRIMARY KEY (binding_id, parameter_name),
    CONSTRAINT fktv34bhchhp84ltjqnnr4wg3q FOREIGN KEY (binding_id)
        REFERENCES public.service_bindings (binding_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.service_binding_parameters
    OWNER to brokeradmin;

CREATE TABLE public.service_binding_credentials
(
    binding_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    credential_value character varying(255) COLLATE pg_catalog."default",
    credential_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT service_binding_credentials_pkey PRIMARY KEY (binding_id, credential_name),
    CONSTRAINT fk8tw5je39e7uqletjqu7t6n5oc FOREIGN KEY (binding_id)
        REFERENCES public.service_bindings (binding_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.service_binding_credentials
    OWNER to brokeradmin;
