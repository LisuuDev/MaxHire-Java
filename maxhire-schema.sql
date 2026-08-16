--
-- PostgreSQL database dump
--

\restrict L8J9YbKFmzattwz5PQBvBmtSGiMty6FdBDvPQjdxf7qXsVdasscfSElf6iuRNEd

-- Dumped from database version 18.4
-- Dumped by pg_dump version 18.4

-- Started on 2026-08-16 13:23:46

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2 (class 3079 OID 16540)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- TOC entry 5064 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 221 (class 1259 OID 16630)
-- Name: offers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offers (
    id character varying(36) DEFAULT gen_random_uuid() NOT NULL,
    title character varying(40) NOT NULL,
    company character varying(40) NOT NULL,
    description text NOT NULL,
    tech jsonb,
    links jsonb,
    updated character varying(50) DEFAULT NULL::character varying,
    user_id character varying(36) DEFAULT NULL::character varying
);


ALTER TABLE public.offers OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16614)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id character varying(36) DEFAULT gen_random_uuid() NOT NULL,
    email character varying(50) NOT NULL,
    password character varying(100) NOT NULL,
    role character varying(7) DEFAULT NULL::character varying,
    name character varying(55) NOT NULL,
    surname character varying(55) NOT NULL,
    phone character varying(15) DEFAULT NULL::character varying,
    photo character varying(255) DEFAULT NULL::character varying
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 5058 (class 0 OID 16630)
-- Dependencies: 221
-- Data for Name: offers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offers (id, title, company, description, tech, links, updated, user_id) FROM stdin;
0f9b2c3a-1d4e-4f6a-8b2c-9d1e3f5a7b9c	Sklep Internetowy Fashion	StyleCode	Poszukujemy doświadczonego programisty do stworzenia nowoczesnego sklepu internetowego dla marki odzieżowej premium. Projekt wymaga pełnej responsywności oraz integracji z systemem magazynowym ERP, co jest kluczowe dla naszej logistyki.\\n\\nWymagamy również wdrożenia bezpiecznych płatności online (Przelewy24/Stripe) oraz modułu do obsługi zwrotów i newslettera. Czekamy na oferty zawierające portfolio podobnych realizacji e-commerce.	["React", "Node.js", "MongoDB", "Stripe"]	{"Github": "#", "Linkedin": "#"}	1764754698	0757f51f-4ce0-4393-8ea0-019781a98992
1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d	Aplikacja CRM dla Logistyki	TransSoft	Zlecę zaprojektowanie i wdrożenie dedykowanego systemu CRM dla firmy transportowej operującej na terenie całej Europy. System musi umożliwiać śledzenie przesyłek w czasie rzeczywistym oraz zarządzanie flotą pojazdów przez dyspozytorów.\\n\\nDodatkowo aplikacja powinna generować automatyczne faktury i raporty dla księgowości. Szukamy zespołu, który ma doświadczenie w tworzeniu skalowalnych aplikacji biznesowych.	["Angular", "TypeScript", "C#", ".NET Core", "SQL Server"]	{"Linkedin": "#"}	1763916762	1daeb50e-fcd9-40fb-8149-7813b1a5f18f
23456789-0abc-4def-0123-45678901abcd	Monitoring Sieci WiFi	NetGuard	Hotel w górach poszukuje specjalisty sieciowego do wdrożenia systemu monitoringu sieci WiFi. Goście skarżą się na zasięg, więc potrzebujemy diagnozy i instalacji dodatkowych Access Pointów firmy Ubiquiti.\\n\\nSystem musi pozwalać na zdalny podgląd statusu urządzeń i powiadamiać obsługę o awariach. Wymagana wizja lokalna oraz konfiguracja kontrolera sieci.	["Networking", "WiFi", "Ubiquiti", "SNMP"]	{"Github": "#"}	1767191812	b79fa00b-6652-4f77-90b2-8dd933f9ec80
2b3c4d5e-6f7a-4b8c-9d0e-1f2a3b4c5d6e	Projekt sieci LAN w biurze	NetConnect	Zlecę kompleksowe zaprojektowanie i wdrożenie infrastruktury sieciowej v nowym biurowcu naszej firmy. Oczekujemy konfiguracji bezpiecznej sieci LAN/WLAN dla około 50 stanowisk roboczych z podziałem na VLAN-y dla różnych działów.\\n\\nW zakres zlecenia wchodzi także dobór sprzętu sieciowego oraz konfiguracja bezpiecznego tunelu VPN dla pracowników zdalnych. Proszę o wycenę uwzględniającą testy obciążeniowe i dokumentację.	["Cisco", "Networking", "VLAN", "VPN"]	{"Github": "#"}	1765690473	2638e05d-64e0-4040-88d2-2aa10cd67501
34567890-abcd-4ef0-1234-56789012abcd	Aplikacja To-Do	TaskMaster	Szukamy freelancera do stworzenia rozbudowanej aplikacji To-Do z funkcja pracy zespołowej w czasie rzeczywistym. Użytkownicy powinni widzieć zmiany wprowadzane przez innych członków zespołu natychmiastowo.\\n\\nProjekt wymaga wykorzystania WebSocketów (Socket.io) oraz nowoczesnego stacku frontendowego. Aplikacja ma posiadać również tryb ciemny i możliwość tworzenia podzadań.	["React", "Socket.io", "Node.js"]	{"Github": "#", "Linkedin": "#"}	1763651426	0757f51f-4ce0-4393-8ea0-019781a98992
3c4d5e6f-7a8b-4c9d-0e1f-2a3b4c5d6e7f	Strona wizytówka kawiarni	CoffeeWeb	Potrzebujemy nowej, estetycznej strony internetowej dla kawiarni otwierającej się w centrum Warszawy. Strona musi być lekka, szybko się ładować i posiadać czytelne menu oraz galerię zdjęć, którą łatwo zaktualizujemy.\\n\\nKluczowa jest sekcja kontaktowa z mapą dojazdu i integracją z Google Maps. Zależy nam na systemie CMS (np. WordPress), abyśmy mogli samodzielnie dodawać aktualności o promocjach.	["HTML5", "CSS3", "JavaScript", "WordPress"]	{"Github": "#", "Linkedin": "#"}	1766206863	483c5e36-d237-4b10-9ea5-8103b55d3e05
45678901-bcde-4f01-2345-67890123abcd	Serwis ogłoszeniowy	LocalAds	Planujemy uruchomienie lokalnego portalu ogłoszeniowego i szukamy wykonawcy MVP. Serwis musi posiadać wyszukiwarkę z filtrowaniem, podział na kategorie oraz panel użytkownika do zarządzania ogłoszeniami.\\n\\nZależy nam na technologii Python/Django ze względu na szybkość tworzenia i bezpieczeństwo. Projekt przewiduje w przyszłości wdrożenie płatnych wyróżnień ogłoszeń.	["Django", "Python", "PostgreSQL", "Bootstrap"]	{"Github": "#"}	1763342587	0757f51f-4ce0-4393-8ea0-019781a98992
56789012-cdef-4012-3456-78901234abcd	Optymalizacja Wordpress	SpeedFix	Moja strona firmowa na WordPressie działa bardzo wolno, co negatywnie wpływa na pozycjonowanie. Zlecę audyt i optymalizację serwisu, aby uzyskać wynik w Google PageSpeed Insights powyżej 90 punktów.\\n\\nZakres prac obejmuje minifikację kodu, optymalizację grafik, konfigurację cache oraz przegląd zainstalowanych wtyczek. Proszę o raport przed i po wykonaniu usługi.	["WordPress", "Lighthouse", "PHP"]	{"Linkedin": "#"}	1765062043	e11ac16f-a76c-4cda-a3d2-8cebd0cbef83
67890123-def0-4123-4567-89012345abcd	Chatbot na stronę	TalkAI	Chcemy wdrożyć na naszej stronie internetowej inteligentnego chatbota, który odciąży dział obsługi klienta. Bot ma odpowiadać na najczęściej zadawane pytania, wykorzystując API OpenAI lub gotowe rozwiązania.\\n\\nInterfejs czatu musi być spójny z wyglądem naszej strony i działać płynnie na telefonach. Wymagana możliwość łatwej edycji bazy wiedzy bota przez naszych pracowników.	["JavaScript", "OpenAI API", "HTML"]	{"Github": "#", "Linkedin": "#"}	1766959351	fb2f6f95-ea70-4d08-8939-bc3b8a91de71
6f7a8b9c-0d1e-4f2a-3b4c-5d6e7f8a9b0c	System rezerwacji wizyt	MediPlan	Prywatna przychodnia lekarska poszukuje wykonawcy webowego systemu rezerwacji wizyt online. Pacjent powinien mieć możliwość wyboru lekarza, daty oraz godziny, a system automatycznie wyśle potwierdzenie SMS.\\n\\nPanel administracyjny dla recepcji musi być intuicyjny i pozwalać na szybkie zarządzanie grafikiem lekarzy. Wymagane jest zachowanie najwyższych standardów bezpieczeństwa danych (RODO).	["Vue.js", "Laravel", "MySQL", "PHP"]	{"Github": "#", "Linkedin": "#"}	1766189924	b79fa00b-6652-4f77-90b2-8dd933f9ec80
78901234-ef01-4234-5678-90123456abcd	System Magazynowy	WareHouse	Mała hurtownia budowlana zleci napisanie prostej aplikacji desktopowej do zarządzania stanem magazynowym. Program musi działać offline i synchronizować dane z serwerem tylko na żądanie.\\n\\nFunkcjonalności: dodawanie towarów, wystawianie dokumentów WZ/PZ, inwentaryzacja. Preferujemy technologię Java i JavaFX ze względu na posiadany sprzęt.	["Java", "JavaFX", "Hibernate", "MySQL"]	{"Linkedin": "#"}	1766846065	0757f51f-4ce0-4393-8ea0-019781a98992
7a8b9c0d-1e2f-4a3b-4c5d-6e7f8a9b0c1d	Analiza bezpieczeństwa API	SecureIt	Zlecę przeprowadzenie audytu bezpieczeństwa naszego REST API obsługującego aplikację finansową. Interesują nas testy penetracyjne pod kątem podatności z listy OWASP Top 10, w szczególności SQL Injection i XSS.\\n\\nEfektem prac ma być szczegółowy raport z wykrytymi lukami oraz rekomendacjami poprawek. Wymagamy podpisania umowy o zachowaniu poufności (NDA) przed przystąpieniem do zlecenia.	["Python", "OWASP", "PenTesting", "Burp Suite"]	{"Linkedin": "#"}	1767337453	bc197781-5fce-42b9-aad6-10591e639771
8b9c0d1e-2f3a-4b4c-5d6e-7f8a9b0c1d2e	Blog kulinarny	TastyCode	Szukam osoby, która postawi profesjonalnego bloga kulinarnego na bazie WordPress. Zależy mi na dedykowanym motywie graficznym, który będzie wyróżniał się na tle konkurencji i będzie dostosowany do urządzeń mobilnych.\\n\\nKluczowa jest optymalizacja pod kątem SEO oraz szybkości ładowania strony (Core Web Vitals). Projekt obejmuje również konfigurację wtyczek do newslettera i mediów społecznościowych.	["WordPress", "PHP", "SEO", "CSS"]	{"Github": "#"}	1763696764	dbae20c5-69a4-4982-a8b8-9c2272f5b72e
90123456-0123-4456-7890-12345678abcd	Portfolio dla Architekta	ArchiWeb	Szukam frontendowca do stworzenia minimalistycznego portfolio dla biura architektonicznego. Projekt graficzny jest bardzo oszczędny, ale wymaga perfekcyjnej typografii i wysokiej jakości prezentacji zdjęć.\\n\\nStrona musi być statyczna (np. Gatsby lub Next.js), aby zapewnić maksymalną szybkość. Treści będą zarządzane przez Headless CMS. Termin realizacji: 2 tygodnie.	["Gatsby", "React", "GraphQL", "Netlify"]	{"Github": "#", "Linkedin": "#"}	1767073221	2638e05d-64e0-4040-88d2-2aa10cd67501
9c0d1e2f-3a4b-4c5d-6e7f-8a9b0c1d2e3f	Bot Discord do zarządzania	GameServer	Potrzebujemy zaawansowanego bota na serwer Discord naszej społeczności graczy (ponad 5000 osób). Bot ma służyć do automatycznej moderacji czatu, przydzielania ról oraz obsługi systemu ticketów dla wsparcia.\\n\\nWymagana jest także integracja z zewnętrznym API naszej gry, aby pobierać statystyki graczy i wyświetlać je na kanale. Kod źródłowy musi być przekazany nam na własność po zakończeniu zlecenia.	["Node.js", "Discord.js", "MongoDB"]	{"Github": "#", "Linkedin": "#"}	1765757513	e11ac16f-a76c-4cda-a3d2-8cebd0cbef83
a1b2c3d4-e5f6-4789-0123-456789abcdef	Landing Page produktu	MarketingPro	Agencja reklamowa zleci wykonanie efektownego Landing Page dla nowego napoju energetycznego. Strona ma być wizytówką kampanii, więc zależy nam na nowoczesnych animacjach (GSAP) i płynnym scrollowaniu.\\n\\nProjekt musi zawierać formularz kontaktowy zintegrowany z systemem mailingowym oraz sekcję FAQ. Termin realizacji jest krótki, dlatego szukamy kogoś dyspozycyjnego od zaraz.	["HTML", "SASS", "GSAP", "JavaScript"]	{"Linkedin": "#"}	1764342398	fb2f6f95-ea70-4d08-8939-bc3b8a91de71
b2c3d4e5-f6a7-4890-1234-567890abcdef	Baza danych klientów	DataFlow	Zlecimy zaprojektowanie i wdrożenie relacyjnej bazy danych dla firmy handlowej. Obecnie pracujemy na plikach Excel, co staje się nieefektywne, dlatego potrzebujemy migracji danych do PostgreSQL.\\n\\nBaza musi uwzględniać relacje między klientami, zamówieniami a stanami magazynowymi. Dodatkowo potrzebujemy prostych skryptów w Pythonie do importu i czyszczenia danych historycznych.	["PostgreSQL", "SQL", "Python", "Pandas"]	{"Github": "#"}	1766311548	0757f51f-4ce0-4393-8ea0-019781a98992
c3d4e5f6-a7b8-4901-2345-678901abcdef	Konfiguracja serwera poczty	MailSys	Poszukujemy administratora Linux do postawienia i zabezpieczenia własnego serwera pocztowego dla małej firmy. Nie chcemy korzystać z gotowych hostingów, zależy nam na pełnej kontroli nad danymi.\\n\\nWymagana konfiguracja Postfix i Dovecot, a także pełne zabezpieczenie domeny (SPF, DKIM, DMARC), aby maile nie trafiały do spamu. Oczekujemy krótkiego instruktażu dla administratora.	["Linux", "Postfix", "Dovecot", "Bash"]	{"Github": "#", "Linkedin": "#"}	1763487975	1daeb50e-fcd9-40fb-8149-7813b1a5f18f
d4e5f6a7-b8c9-4012-3456-789012abcdef	Skrypt do automatyzacji	AutoOffice	Biuro rachunkowe zleci napisanie skryptu automatyzującego generowanie miesięcznych raportów dla klientów. Skrypt powinien pobierać dane z plików CSV, przetwarzać je i generować spersonalizowane pliki PDF.\\n\\nRozwiązanie musi być proste w obsłudze dla pracowników nietechnicznych (np. uruchamiane jedną ikoną). Preferujemy język Python ze względu na łatwość późniejszych modyfikacji.	["Python", "Pandas", "ReportLab"]	{"Github": "#"}	1764047122	2638e05d-64e0-4040-88d2-2aa10cd67501
e5f6a7b8-c9d0-4123-4567-890123abcdef	Gra przeglądarkowa 2D	IndieWeb	Poszukuję programisty JS do współpracy przy hobbystycznym projekcie gry platformowej 2D. Gra ma działać w przeglądarce i wykorzystywać bibliotekę Phaser.js lub czyste Canvas API.\\n\\nPosiadam już gotowe grafiki postaci i tła, potrzebuję kogoś, kto zajmie się logiką gry, fizyką i obsługą kolizji. Oferuję wynagrodzenie za kamienie milowe projektu.	["JavaScript", "Phaser.js", "HTML5"]	{"Linkedin": "#"}	1767168840	483c5e36-d237-4b10-9ea5-8103b55d3e05
89012345-f012-4345-6789-01234567abcd	Rozbudowa API	BackendPro	Nasz startup potrzebuje wsparcia backendowca przy rozbudowie istniejącego API napisanego w Go. Zadanie polega na dodaniu nowych endpointów dla aplikacji mobilnej oraz wdrożeniu autoryzacji opartej o JWT.\\n\\nWymagamy pisania testów jednostkowych do nowego kodu oraz aktualizacji dokumentacji w Swaggerze. Praca w środowisku Dockerowym, system kontroli wersji Git.	["Go", "Gin", "PostgreSQL", "Docker"]	{"Github": "#"}	1767825965	f65fdbef-f1dd-4556-80ca-e3944f2dc594
\.


--
-- TOC entry 5057 (class 0 OID 16614)
-- Dependencies: 220
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, password, role, name, surname, phone, photo) FROM stdin;
0757f51f-4ce0-4393-8ea0-019781a98992	anna.kowalska@gmail.com	$2b$10$vDid.CRgdny7sXzBJHb1eesmbIaaBp.iaJGz4q.u1.ggiHQTsgtYe	user	Anna	Kowalska	+48601234567	https://ui-avatars.com/api/?name=Anna+Kowalska&background=random
1daeb50e-fcd9-40fb-8149-7813b1a5f18f	piotr.nowak@wp.pl	$2b$10$CRz.KqrgC6IenPjAJtspIObpuuZDUtiS5HpvkNcMBDCurKaKmutZO	user	Piotr	Nowak	+48502345678	https://ui-avatars.com/api/?name=Piotr+Nowak&background=random
2638e05d-64e0-4040-88d2-2aa10cd67501	mario.wisniewski@gmail.com	$2b$10$pzoOdtzHGTz7UZNL./iSE.4f6cuDvSL6yEvOGOG8kBKijov41vQPe	user	Mariusz	Wiśniewski	+48790111222	https://ui-avatars.com/api/?name=Mariusz+Wisniewski&background=random
483c5e36-d237-4b10-9ea5-8103b55d3e05	jacek.ksiezyc@gmail.com	$2b$10$uavRx4rMnJmyGobeqptkpez.VJEpW4bkHUeM1azQIEraf7h79aEre	user	Jacek	Księżyc	+48666777888	https://ui-avatars.com/api/?name=Jacek+Ksiezyc&background=random
b79fa00b-6652-4f77-90b2-8dd933f9ec80	kamil.maj@wp.pl	$2b$10$8uzrDSp4luTeZiKsBkGb/e4TKRg.bdBLwN9irPbpmLEW9qs0/CKMe	user	Kamil	Maj	+48505606707	https://res.cloudinary.com/dp8o4hppt/image/upload/v1768008486/ProfilePhoto/tw7v5btvlulcqj4tlpus.jpg
bc197781-5fce-42b9-aad6-10591e639771	adam.kowalski@wp.pl	$2b$10$y27Yghrct7FhplUr3M2wO.Onm/Ngr/J.53/z9aG3wlOy9wzZjZ4Se	user	Adam	Kowalski	+48600900800	https://ui-avatars.com/api/?name=Adam+Kowalski&background=random
dbae20c5-69a4-4982-a8b8-9c2272f5b72e	krzysztof.zielony@wp.pl	$2b$10$MznG0LPcVKDFPL0ADuxLvuz3kn577NXhl5nzoYn4a4SV75OwWEKPi	user	Krzysztof	Zielony	+48501502503	https://ui-avatars.com/api/?name=Krzysztof+Zielony&background=random
e11ac16f-a76c-4cda-a3d2-8cebd0cbef83	jan.nowakowski@gmail.com	$2b$10$y27Yghrct7FhplUr3M2wO.Onm/Ngr/J.53/z9aG3wlOy9wzZjZ4Se	user	Jan	Nowakowski	+48781782783	https://ui-avatars.com/api/?name=Jan+Nowakowski&background=random
fb2f6f95-ea70-4d08-8939-bc3b8a91de71	monika.zajac@gmail.com	$2b$10$ksCVQDKYGN2gGFm/h75/melSEjukB4LI4Dvnxaa2L5XsYUx2lRX.u	user	Monika	Zając	+48512345678	https://ui-avatars.com/api/?name=Monika+Zajac&background=random
f65fdbef-f1dd-4556-80ca-e3944f2dc594	a2@wp.pl	$2a$10$teZquKaaoO72QP.J0wfFuuX0jKmsFrdHZz8tkAZSADwg2ZBB5Ad7.	user	Adam	Drugi	+48 555 222 444	\N
\.


--
-- TOC entry 4908 (class 2606 OID 16643)
-- Name: offers PK_offers; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT "PK_offers" PRIMARY KEY (id);


--
-- TOC entry 4905 (class 2606 OID 16629)
-- Name: users PK_users; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT "PK_users" PRIMARY KEY (id);


--
-- TOC entry 4906 (class 1259 OID 16649)
-- Name: IDX_offers_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "IDX_offers_user_id" ON public.offers USING btree (user_id);


--
-- TOC entry 4909 (class 2606 OID 16644)
-- Name: offers FK_offers_users; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT "FK_offers_users" FOREIGN KEY (user_id) REFERENCES public.users(id) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2026-08-16 13:23:47

--
-- PostgreSQL database dump complete
--

\unrestrict L8J9YbKFmzattwz5PQBvBmtSGiMty6FdBDvPQjdxf7qXsVdasscfSElf6iuRNEd

