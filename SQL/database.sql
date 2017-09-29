DROP TABLE IF EXISTS Apprende;
DROP TABLE IF EXISTS Inventario;
DROP TABLE IF EXISTS Personaggio;
DROP TABLE IF EXISTS Conduce;
DROP TABLE IF EXISTS Possiede;
DROP TABLE IF EXISTS Abilità;
DROP TABLE IF EXISTS Livello;
DROP TABLE IF EXISTS Appartiene;
DROP TABLE IF EXISTS Zona;
DROP TABLE IF EXISTS Ambientazione;
DROP TABLE IF EXISTS Oggetto;
DROP TABLE IF EXISTS Mostro;
DROP TABLE IF EXISTS Razza;
DROP TABLE IF EXISTS Giocatore;

/*
CREATE TYPE tipoOgg AS ENUM('Arma','Pozione','Chiave');
CREATE TYPE tipoZona AS ENUM('Standard','Safezone','Tesoro');
CREATE TYPE tipoClasse AS ENUM('Guerriero','Mago');
CREATE TYPE tipoAbilità AS ENUM ('Attacco','Magia');*/

CREATE TABLE Giocatore
(
	Nickname varchar(20) PRIMARY KEY
);

CREATE TABLE Oggetto
(
	Nome varchar(20) PRIMARY KEY,
	Tipo tipoOgg NOT NULL,
	pvRiprist Integer,
	pmRiprist Integer,
	danno Integer
);


CREATE TABLE Livello
(
	IDLivello Integer PRIMARY KEY,
	Exp Integer NOT NULL
	--Exp è l'exp necessaria per raggiungere il livello successivo
);

CREATE TABLE Ambientazione
(
	Nome varchar(20) PRIMARY KEY
);

CREATE TABLE Zona
(
	Nome varchar(20) PRIMARY KEY,
	Ambientazione varchar(20) NOT NULL,
	Tipo tipoZona NOT NULL,
	Richiede varchar(20) DEFAULT NULL,
	Situato varchar(20) DEFAULT NULL,
	Disponibile boolean DEFAULT NULL,
	FOREIGN KEY (Richiede) REFERENCES Oggetto(Nome) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (Situato) REFERENCES Oggetto(Nome) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (Ambientazione) REFERENCES Ambientazione(Nome) ON UPDATE CASCADE,
	CONSTRAINT chk_richiede CHECK (Richiede<>Situato),
	CONSTRAINT chk_tesoro CHECK ((Tipo='Tesoro' AND situato IS NOT NULL AND disponibile IS NOT NULL) OR (Tipo='Standard' AND situato IS NULL AND disponibile IS NULL) OR (Tipo='Safezone' AND situato IS NULL AND disponibile IS NULL))
);

CREATE TABLE Conduce
(
	Zona1 varchar(20) NOT NULL,
	Zona2 varchar(20) NOT NULL,
	PRIMARY KEY(Zona1,Zona2),
	FOREIGN KEY (Zona1) REFERENCES Zona(Nome) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (Zona2) REFERENCES Zona(Nome) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Razza
(
	NomeRazza varchar(20) PRIMARY KEY,
	NomeAttacco varchar(20) NOT NULL,
	Danno Integer NOT NULL
);

CREATE TABLE Mostro
(
	Nome varchar(20) PRIMARY KEY,
	Hp Integer NOT NULL DEFAULT 100,
	Razza varchar(20) NOT NULL,
	FOREIGN KEY (Razza) REFERENCES Razza(NomeRazza) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Possiede
(
	Mostro varchar(20),
	Oggetto varchar(20),
	PRIMARY KEY(Mostro,Oggetto),
	FOREIGN KEY(Mostro) REFERENCES Mostro(Nome) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(Oggetto) REFERENCES Oggetto(Nome) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Personaggio
(
	Nome varchar(20) PRIMARY KEY,
	Livello Integer NOT NULL DEFAULT 1,
	HP Integer NOT NULL DEFAULT 100,
	MP Integer NOT NULL DEFAULT 100,
	Exp Integer NOT NULL DEFAULT 0,
	Equipaggia varchar(20) DEFAULT NULL,
	Forza Integer,
	Intelligenza Integer,
	Classe tipoClasse NOT NULL,
	Giocatore varchar(20) NOT NULL,
	Zona varchar(20) NOT NULL,
	GiocoVinto boolean DEFAULT false,
	FOREIGN KEY (Livello) REFERENCES Livello(IDLivello) ON UPDATE CASCADE,
	FOREIGN KEY (Equipaggia) REFERENCES Oggetto(Nome) ON UPDATE CASCADE,
	FOREIGN KEY (Giocatore) REFERENCES Giocatore(Nickname) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (Zona) REFERENCES Zona(Nome) ON UPDATE CASCADE,
	CONSTRAINT chk_Class CHECK ((Classe='Guerriero' AND Intelligenza = null) OR (Classe='Mago' AND Forza = null))

	--NB PV E HP, PM E MP e Exp e Exp sono sinonimi
);

CREATE TABLE Inventario
(
	Personaggio varchar(20),
	Oggetto varchar(20),
	Quantità Integer NOT NULL DEFAULT 1,
	PRIMARY KEY(Personaggio,Oggetto),
	FOREIGN KEY (Personaggio) REFERENCES Personaggio(Nome) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (Oggetto) REFERENCES Oggetto(Nome) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Abilità
(
	Nome varchar(20) PRIMARY KEY,
	Danno Integer NOT NULL,
	Livello Integer NOT NULL DEFAULT 0,
	Tipo tipoAbilità NOT NULL,
	Costo Integer NOT NULL,
	FOREIGN KEY (Livello) REFERENCES Livello(IDLivello)
);

CREATE TABLE Apprende
(
	Personaggio varchar(20),
	Abilità varchar(20),
	PRIMARY KEY(Personaggio,Abilità),
	FOREIGN KEY (Personaggio) REFERENCES Personaggio(Nome) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (Abilità) REFERENCES Abilità(Nome)  ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Appartiene
(
	Mostro varchar(20),
	Zona varchar(20),
	PRIMARY KEY (Mostro,Zona),
	FOREIGN KEY (Mostro) REFERENCES Mostro(Nome) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (Zona) REFERENCES Zona(Nome) ON UPDATE CASCADE ON DELETE CASCADE

);
