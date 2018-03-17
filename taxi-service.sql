--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.4
-- Dumped by pg_dump version 9.6.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: ride_status; Type: TYPE; Schema: public; Owner: ilyaborovik
--

CREATE TYPE ride_status AS ENUM (
    'ordered',
    'paid',
    'started',
    'finished',
    'cancelled'
);


ALTER TYPE ride_status OWNER TO ilyaborovik;

--
-- Name: service_type; Type: TYPE; Schema: public; Owner: ilyaborovik
--

CREATE TYPE service_type AS ENUM (
    'economy',
    'comfort',
    'business'
);


ALTER TYPE service_type OWNER TO ilyaborovik;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: cars; Type: TABLE; Schema: public; Owner: ilyaborovik
--

CREATE TABLE cars (
    id integer NOT NULL,
    serial_number character varying(20) NOT NULL,
    model character varying(128) NOT NULL,
    color character varying(20) NOT NULL,
    driver_id integer NOT NULL,
    has_child_seat boolean DEFAULT false NOT NULL,
    is_blocked boolean DEFAULT false NOT NULL
);


ALTER TABLE cars OWNER TO ilyaborovik;

--
-- Name: cars_id_seq; Type: SEQUENCE; Schema: public; Owner: ilyaborovik
--

CREATE SEQUENCE cars_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cars_id_seq OWNER TO ilyaborovik;

--
-- Name: cars_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ilyaborovik
--

ALTER SEQUENCE cars_id_seq OWNED BY cars.id;


--
-- Name: cities; Type: TABLE; Schema: public; Owner: ilyaborovik
--

CREATE TABLE cities (
    id integer NOT NULL,
    name character varying(64) NOT NULL,
    region character varying(64) NOT NULL,
    is_unsupported boolean DEFAULT false NOT NULL
);


ALTER TABLE cities OWNER TO ilyaborovik;

--
-- Name: cities_id_seq; Type: SEQUENCE; Schema: public; Owner: ilyaborovik
--

CREATE SEQUENCE cities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cities_id_seq OWNER TO ilyaborovik;

--
-- Name: cities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ilyaborovik
--

ALTER SEQUENCE cities_id_seq OWNED BY cities.id;


--
-- Name: drivers; Type: TABLE; Schema: public; Owner: ilyaborovik
--

CREATE TABLE drivers (
    id integer NOT NULL,
    first_name character varying(64) NOT NULL,
    last_name character varying(64) NOT NULL,
    age integer NOT NULL,
    is_blocked boolean DEFAULT false NOT NULL
);


ALTER TABLE drivers OWNER TO ilyaborovik;

--
-- Name: drivers_id_seq; Type: SEQUENCE; Schema: public; Owner: ilyaborovik
--

CREATE SEQUENCE drivers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE drivers_id_seq OWNER TO ilyaborovik;

--
-- Name: drivers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ilyaborovik
--

ALTER SEQUENCE drivers_id_seq OWNED BY drivers.id;


--
-- Name: rides; Type: TABLE; Schema: public; Owner: ilyaborovik
--

CREATE TABLE rides (
    id integer NOT NULL,
    user_id integer NOT NULL,
    car_id integer,
    service_id integer NOT NULL,
    order_time timestamp without time zone DEFAULT now() NOT NULL,
    location_from character varying(256) NOT NULL,
    location_to character varying(256) NOT NULL,
    time_start timestamp without time zone,
    time_end timestamp without time zone,
    price integer NOT NULL,
    rating integer,
    order_comments character varying(512),
    status ride_status DEFAULT 'ordered'::ride_status NOT NULL
);


ALTER TABLE rides OWNER TO ilyaborovik;

--
-- Name: rides_id_seq; Type: SEQUENCE; Schema: public; Owner: ilyaborovik
--

CREATE SEQUENCE rides_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE rides_id_seq OWNER TO ilyaborovik;

--
-- Name: rides_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ilyaborovik
--

ALTER SEQUENCE rides_id_seq OWNED BY rides.id;


--
-- Name: ru.innopolis.services; Type: TABLE; Schema: public; Owner: ilyaborovik
--

CREATE TABLE ru.innopolis.services (
    id integer NOT NULL,
    city_id integer NOT NULL,
    service_type service_type DEFAULT 'economy'::service_type NOT NULL,
    base_rate integer DEFAULT 49 NOT NULL,
    is_removed boolean DEFAULT false
);


ALTER TABLE ru.innopolis.services OWNER TO ilyaborovik;

--
-- Name: services_id_seq; Type: SEQUENCE; Schema: public; Owner: ilyaborovik
--

CREATE SEQUENCE services_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE services_id_seq OWNER TO ilyaborovik;

--
-- Name: services_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ilyaborovik
--

ALTER SEQUENCE services_id_seq OWNED BY ru.innopolis.services.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: ilyaborovik
--

CREATE TABLE users (
    id integer NOT NULL,
    login character varying(64) NOT NULL,
    first_name character varying(64) NOT NULL,
    last_name character varying(64) NOT NULL,
    password character varying(64) NOT NULL,
    phone_number character varying(20),
    city_id integer,
    registration_date timestamp without time zone DEFAULT now() NOT NULL,
    is_blocked boolean NOT NULL
);


ALTER TABLE users OWNER TO ilyaborovik;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: ilyaborovik
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO ilyaborovik;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ilyaborovik
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: cars id; Type: DEFAULT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY cars ALTER COLUMN id SET DEFAULT nextval('cars_id_seq'::regclass);


--
-- Name: cities id; Type: DEFAULT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY cities ALTER COLUMN id SET DEFAULT nextval('cities_id_seq'::regclass);


--
-- Name: drivers id; Type: DEFAULT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY drivers ALTER COLUMN id SET DEFAULT nextval('drivers_id_seq'::regclass);


--
-- Name: rides id; Type: DEFAULT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY rides ALTER COLUMN id SET DEFAULT nextval('rides_id_seq'::regclass);


--
-- Name: ru.innopolis.services id; Type: DEFAULT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY ru.innopolis.services ALTER COLUMN id SET DEFAULT nextval('services_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Data for Name: cars; Type: TABLE DATA; Schema: public; Owner: ilyaborovik
--

COPY cars (id, serial_number, model, color, driver_id, has_child_seat, is_blocked) FROM stdin;
1	C070MK16	Hyundai Solaris	white	1	t	f
2	E210PE116	Renault Logan	black	2	f	f
4	K515KP16	Lada Granta	grey	4	t	f
5	X777XXX	Ford Galaxy	white	5	f	f
3	M123MM116	Lada Granta	white	3	f	t
\.


--
-- Name: cars_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ilyaborovik
--

SELECT pg_catalog.setval('cars_id_seq', 5, true);


--
-- Data for Name: cities; Type: TABLE DATA; Schema: public; Owner: ilyaborovik
--

COPY cities (id, name, region, is_unsupported) FROM stdin;
2	Kazan	Republic of Tatarstan	f
3	Moscow	Moscow Region	t
1	Innopolis	Republic of Tatarstan	f
\.


--
-- Name: cities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ilyaborovik
--

SELECT pg_catalog.setval('cities_id_seq', 1, false);


--
-- Data for Name: drivers; Type: TABLE DATA; Schema: public; Owner: ilyaborovik
--

COPY drivers (id, first_name, last_name, age, is_blocked) FROM stdin;
1	Ivan	Popov	41	f
2	Abdul	Ahmetzyanov	27	f
3	Sergey	Abramov	28	f
4	Victor	Ostapenko	34	f
5	Rinat	Fazullin	25	f
\.


--
-- Name: drivers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ilyaborovik
--

SELECT pg_catalog.setval('drivers_id_seq', 5, true);


--
-- Data for Name: rides; Type: TABLE DATA; Schema: public; Owner: ilyaborovik
--

COPY rides (id, user_id, car_id, service_id, order_time, location_from, location_to, time_start, time_end, price, rating, order_comments, status) FROM stdin;
1	1	2	3	2018-03-06 23:49:47.767635	Koltzo	MEGA	2018-03-07 18:10:32.491	2018-03-07 17:10:34.608	94	\N	where is my discount?	finished
2	1	5	2	2018-03-07 18:07:46.930633	IU	SC	\N	\N	119	0		cancelled
3	1	3	1	2018-03-07 18:09:02.865996	IU	Tech	\N	\N	99	0		cancelled
4	1	5	1	2018-03-08 15:46:57.13431	SC	Campus	\N	\N	104	0	make it fast, please	cancelled
5	1	4	1	2018-03-08 15:50:38.288352	SC	Campus	\N	\N	99	0	make it fast, please	cancelled
6	1	1	5	2018-03-08 22:59:06.56404	Center	Tandem	\N	\N	264	\N	last test ride	finished
7	1	4	4	2018-03-08 23:01:58.142863	Tandem	Center	\N	\N	119	\N		finished
8	3	1	1	2018-03-08 23:56:46.755786	Campus	Bahetle	\N	\N	84	\N		finished
\.


--
-- Name: rides_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ilyaborovik
--

SELECT pg_catalog.setval('rides_id_seq', 8, true);


--
-- Data for Name: ru.innopolis.services; Type: TABLE DATA; Schema: public; Owner: ilyaborovik
--

COPY ru.innopolis.services (id, city_id, service_type, base_rate, is_removed) FROM stdin;
3	2	economy	49	f
4	2	comfort	99	f
5	2	business	249	f
1	1	comfort	59	f
2	1	business	99	f
\.


--
-- Name: services_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ilyaborovik
--

SELECT pg_catalog.setval('services_id_seq', 1, false);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: ilyaborovik
--

COPY users (id, login, first_name, last_name, password, phone_number, city_id, registration_date, is_blocked) FROM stdin;
1	admin	admin	admin	$2a$10$nJceAoWf6M8N/8cBv/T30On1W/my2aPoQj.8JWNoqJ.gqQju4m432	+7 (919) 123-4567	2	2018-03-06 16:15:49.058423	f
3	ib16	Ilya	Borovik	$2a$10$6uNwzZkKpXGQoBYWeLpcZ.A9DcIDywqsvF7TZwckcvf5MdARnWfkm	\N	1	2018-03-08 23:24:14.794286	f
4	superuser	super	user	$2a$10$pVLNvvkq.oNNvRHlwBKN6eDTIw/yOObn0CIAaciWwCbGj7hoQSs9G	\N	\N	2018-03-08 23:33:13.733255	t
2	test_user	Test	User	$2a$10$pVLNvvkq.oNNvRHlwBKN6eDTIw/yOObn0CIAaciWwCbGj7hoQSs9G	\N	1	2018-03-08 12:38:35.020189	f
\.


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ilyaborovik
--

SELECT pg_catalog.setval('users_id_seq', 4, true);


--
-- Name: cars cars_pkey; Type: CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY cars
    ADD CONSTRAINT cars_pkey PRIMARY KEY (id);


--
-- Name: cars cars_serial_number_key; Type: CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY cars
    ADD CONSTRAINT cars_serial_number_key UNIQUE (serial_number);


--
-- Name: cities cities_pkey; Type: CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id);


--
-- Name: rides rides_pkey; Type: CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY rides
    ADD CONSTRAINT rides_pkey PRIMARY KEY (id);


--
-- Name: ru.innopolis.services services_pkey; Type: CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY ru.innopolis.services
    ADD CONSTRAINT services_pkey PRIMARY KEY (id);


--
-- Name: drivers taxi_driver_pkey; Type: CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY drivers
    ADD CONSTRAINT taxi_driver_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: cities_id_uindex; Type: INDEX; Schema: public; Owner: ilyaborovik
--

CREATE UNIQUE INDEX cities_id_uindex ON cities USING btree (id);


--
-- Name: cities_name_region_uindex; Type: INDEX; Schema: public; Owner: ilyaborovik
--

CREATE UNIQUE INDEX cities_name_region_uindex ON cities USING btree (name, region);


--
-- Name: rides_id_uindex; Type: INDEX; Schema: public; Owner: ilyaborovik
--

CREATE UNIQUE INDEX rides_id_uindex ON rides USING btree (id);


--
-- Name: rides_id_user_id_car_id_service_id_uindex; Type: INDEX; Schema: public; Owner: ilyaborovik
--

CREATE UNIQUE INDEX rides_id_user_id_car_id_service_id_uindex ON rides USING btree (id, user_id, car_id, service_id);


--
-- Name: services_id_city_id_is_removed_uindex; Type: INDEX; Schema: public; Owner: ilyaborovik
--

CREATE UNIQUE INDEX services_id_city_id_is_removed_uindex ON ru.innopolis.services USING btree (id, city_id, is_removed);


--
-- Name: services_id_uindex; Type: INDEX; Schema: public; Owner: ilyaborovik
--

CREATE UNIQUE INDEX services_id_uindex ON ru.innopolis.services USING btree (id);


--
-- Name: cars cars_drivers_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY cars
    ADD CONSTRAINT cars_drivers_id_fk FOREIGN KEY (driver_id) REFERENCES drivers(id) ON UPDATE CASCADE;


--
-- Name: rides rides_cars_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY rides
    ADD CONSTRAINT rides_cars_id_fk FOREIGN KEY (car_id) REFERENCES cars(id) ON UPDATE CASCADE;


--
-- Name: rides rides_services_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY rides
    ADD CONSTRAINT rides_services_id_fk FOREIGN KEY (service_id) REFERENCES ru.innopolis.services(id) ON UPDATE CASCADE;


--
-- Name: rides rides_users_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY rides
    ADD CONSTRAINT rides_users_id_fk FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE;


--
-- Name: ru.innopolis.services services_cities_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY ru.innopolis.services
    ADD CONSTRAINT services_cities_id_fk FOREIGN KEY (city_id) REFERENCES cities(id) ON UPDATE CASCADE;


--
-- Name: users users_cities_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ilyaborovik
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_cities_id_fk FOREIGN KEY (city_id) REFERENCES cities(id);


--
-- PostgreSQL database dump complete
--

