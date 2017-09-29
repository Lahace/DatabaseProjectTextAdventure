DROP TABLE IF EXISTS Test;
DROP TABLE IF EXISTS Test2;

CREATE TABLE Test2
(
	ID Integer,
	Col1 varchar(20),
	Col2 varchar(10) NOT NULL,
	PRIMARY KEY(ID)
);

CREATE TABLE Test
(
	Entry Integer,
	Col1 varchar(20),
	Col2 varchar(10) NOT NULL,
	Test2 Integer,
	PRIMARY KEY(Entry),
	FOREIGN KEY(Test2) REFERENCES Test2 (ID)
);

INSERT INTO Test2(ID,Col1,Col2) VALUES (0,'R1Col1','R1Col2');
INSERT INTO Test2(ID,Col1,Col2) VALUES (1,'R2Col1','R2Col2');
INSERT INTO Test2(ID,Col1,Col2) VALUES (2,'R3Col1','R3Col2');
INSERT INTO Test2(ID,Col1,Col2) VALUES (3,'R4Col1','R4Col2');

INSERT INTO Test(Entry,Col1,Col2,Test2) VALUES (0,'R1Col1','R1Col2',0);
INSERT INTO Test(Entry,Col1,Col2,Test2) VALUES (1,'R2Col1','R2Col2',1);
INSERT INTO Test(Entry,Col1,Col2,Test2) VALUES (2,'R3Col1','R3Col2',1);
INSERT INTO Test(Entry,Col1,Col2,Test2) VALUES (3,'R4Col1','R4Col2',3);