


INSERT INTO Livello (IDLivello, Exp) VALUES ( 1, 50), (2,150), (3,300), (4, 500), (5, 800); 
INSERT INTO Abilità (Nome, Danno, Livello, Tipo, Costo) VALUES ('Calcio', 30, 1, 'Attacco',5), ('Pugno', 40, 2, 'Attacco', 10),('Affondo', 60, 4, 'Attacco',15), ('Fuoco', 30, 1, 'Magia',5),('Ghiaccio', 40, 2, 'Magia',10), ('Tuono', 60, 4, 'Magia',15);
INSERT INTO Giocatore (Nickname) VALUES ( 'Alberto'), ('Ylenia'), ('Giacomo');
INSERT INTO Razza (NomeRazza, NomeAttacco, Danno) VALUES ('DarkMountain', 'WithIce',5), ('GlenGrant', 'OldMalt', 50), ('Sauza', 'SaltLemon', 30), ('Absolute', 'RussianHOW', 20), ('Volpina', 'DeepRed', 10);
INSERT INTO Mostro (Nome,HP,Razza) VALUES ('Serby', 30, 'DarkMountain'), ('Scotty',80, 'GlenGrant'), ('Tequy', 60, 'Sauza'), ('Voddy',50, 'Absolute'), ('Foxxy', 40, 'Volpina'); 
INSERT INTO Oggetto ( Nome, Tipo, pvRiprist, pmRiprist, danno) VALUES ('PozioneRossa', 'Pozione', null, 30, null), ('PozioneBlu', 'Pozione', 60, null, null),('PozioneVerde', 'Pozione',40,40, null), ('ChiaveEmerald', 'Chiave',null,null,null),('ChiaveSapphire', 'Chiave',null,null,null),('ChiaveAmethyst', 'Chiave',null,null,null), ('Arco', 'Arma', null, null,30),('Spada', 'Arma', null, null,20), ('Balestra', 'Arma', null, null,40);
INSERT INTO Ambientazione(Nome) VALUES ('Palude'),('Deserto'),('Landa'), ('Montagna'), ('Ghiacciaio'), ('Villaggio'),('Castello'), ('Taverna'), ('Foresta'), ('Ostello'),('Caverna');
INSERT INTO Zona (Nome, Ambientazione, Tipo, Richiede, Situato,Disponibile) VALUES ('Zona1', 'Palude', 'Tesoro', null, 'PozioneVerde',true), ('Zona2', 'Landa', 'Standard', null, null,null),('Zona3', 'Foresta', 'Tesoro', null, 'ChiaveEmerald',true),('Zona4', 'Taverna', 'Safezone', null, null,null),('Zona5', 'Villaggio', 'Tesoro', null, 'Arco',true),('Zona6', 'Montagna', 'Standard', 'ChiaveEmerald', null,null),('Zona7', 'Caverna', 'Tesoro', null, 'ChiaveSapphire',true),('Zona8', 'Deserto', 'Tesoro', null, 'PozioneVerde',true),('Zona9', 'Palude', 'Tesoro','ChiaveSapphire','Balestra',true),('Zona10', 'Ostello', 'Safezone', 'ChiaveSapphire',null,null),('Zona11', 'Ghiacciaio', 'Tesoro', null,'ChiaveAmethyst',true),('Zona12', 'Castello', 'Standard', 'ChiaveAmethyst',null,null);

INSERT INTO Razza (NomeRazza, NomeAttacco, Danno) VALUES ('Monster', 'Pain', 10);
INSERT INTO Mostro (Nome,Hp,Razza) VALUES ('Ragno', 50, 'Monster'),('Ratto', 20, 'Monster'),('Scorpione', 30, 'Monster'),('Serpente', 30, 'Monster'),('Falco', 50, 'Monster'),('Orso', 70, 'Monster'),('Alce', 60, 'Monster'),('Vipera', 40, 'Monster'),('Testuggine', 20, 'Monster'),('Gatto', 10, 'Monster');


INSERT INTO Conduce (Zona1, Zona2) VALUES ('Zona1', 'Zona2'), ('Zona1', 'Zona4'),('Zona2', 'Zona3'),('Zona2', 'Zona5'),('Zona2', 'Zona1'),('Zona3', 'Zona2'),('Zona3', 'Zona6'),('Zona4', 'Zona5'),('Zona4', 'Zona1'), ('Zona5', 'Zona4'),('Zona5', 'Zona2'),('Zona5', 'Zona6'),('Zona5', 'Zona8'), ('Zona6', 'Zona3'),('Zona6', 'Zona5'),('Zona6', 'Zona9'),('Zona7', 'Zona8'),('Zona7', 'Zona10'),('Zona8', 'Zona9'),('Zona8', 'Zona7'),('Zona8', 'Zona5'),('Zona8', 'Zona11'),('Zona9', 'Zona6'),('Zona9', 'Zona8'),('Zona10', 'Zona7'),('Zona10', 'Zona11'),('Zona11', 'Zona10'),('Zona11', 'Zona8'),('Zona11', 'Zona12'),('Zona12', 'Zona11');

INSERT INTO Appartiene (Mostro, Zona) VALUES ('Ragno','Zona1'),('Ratto','Zona5'), ('Scorpione', 'Zona8'),('Serpente','Zona5'),('Falco', 'Zona7'),('Orso', 'Zona6'),('Orso', 'Zona3'),('Alce','Zona2'),('Vipera', 'Zona9'),('Testuggine', 'Zona11'),('Gatto', 'Zona11'),('Testuggine', 'Zona8');
INSERT INTO Appartiene (Mostro, Zona) VALUES ('Serby', 'Zona2'), ('Scotty','Zona12'), ('Tequy', 'Zona8'), ('Voddy','Zona9'), ('Foxxy', 'Zona11');

INSERT INTO Possiede (Mostro, Oggetto) VALUES ('Serby', 'Spada'), ('Scotty','PozioneBlu'), ('Tequy', 'PozioneVerde'), ('Voddy','PozioneRossa'), ('Foxxy', 'PozioneVerde'), ('Scorpione', 'PozioneRossa'),('Serpente','PozioneBlu'),('Serpente','PozioneVerde');


