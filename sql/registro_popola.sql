USE registro;

INSERT INTO utente (id_utente, email, password, cognome, nome) VALUES
(1, 'mario.rossi@unimi.it', '1234', 'Rossi', 'Mario'),       -- docente
(2, 'giulia.verdi@unimi.it', '5678', 'Verdi', 'Giulia'),     -- studente 1
(3, 'luca.bianchi@unimi.it', '91011', 'Bianchi', 'Luca'),    -- studente 2
(4, 'anna.neri@unimi.it', 'abcd', 'Neri', 'Anna'),           -- studente 3
(5, 'francesco.ferri@unimi.it', 'qwerty', 'Ferri', 'Francesco'), -- studente 4
(6, 'sara.rossi@unimi.it', 'pass123', 'Rossi', 'Sara'),      -- docente 2
(7, 'fabio.conti@unimi.it', 'secret', 'Conti', 'Fabio');     -- studente 5

INSERT INTO docente (id_docente) VALUES
(1),  -- Mario Rossi
(6);  -- Sara Rossi

INSERT INTO studente (id_studente, matricola, corso_laurea) VALUES
(2, 'S12345', 'Informatica'),
(3, 'S67890', 'Informatica'),
(4, 'S54321', 'Fisica'),
(5, 'S99999', 'Matematica'),
(7, 'S11223', 'Informatica');

INSERT INTO corso (id_corso, nome, cfu, id_docente) VALUES
(1, 'Tecnologie Web', 6, 1),
(2, 'Algoritmi e Strutture Dati', 9, 1),
(3, 'Fisica Generale', 12, 6);

INSERT INTO iscrizione_corso (id_studente, id_corso, anno) VALUES
(2, 1, 2024),
(3, 1, 2024),
(5, 1, 2024),
(7, 1, 2024),
(2, 2, 2024),
(4, 3, 2024);

INSERT INTO appello (id_appello, id_corso, data) VALUES
(1, 1, '2025-06-15'),
(2, 1, '2025-07-01'),
(3, 2, '2025-06-20'),
(4, 3, '2025-06-10');

INSERT INTO verbale (id_verbale, codice_verbale, id_appello) VALUES
(1, 101, 1),
(2, 102, 2),
(3, 201, 3),
(4, 301, 4);

INSERT INTO valutazione (id_studente, id_appello, voto, stato_valutazione, id_verbale) VALUES
(2, 1, '28', 'verbalizzato', 1),
(3, 1, 'rimandato', 'verbalizzato', 1),
(5, 1, 'assente', 'verbalizzato', 1),
(7, 1, '30', 'verbalizzato', 1),

(2, 2, '30L', 'verbalizzato', 2),
(3, 2, 'riprovato', 'verbalizzato', 2),

(2, 3, '24', 'verbalizzato', 3),

(4, 4, '19', 'verbalizzato', 4);