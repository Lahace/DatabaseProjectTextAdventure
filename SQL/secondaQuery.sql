DROP VIEW IF EXISTS PersMaxLiv;

CREATE VIEW PersMaxLiv AS (SELECT p.nome,equipaggia, danno 
			   FROM personaggio AS p JOIN oggetto AS o 
			   ON p.equipaggia = o.nome 
			   WHERE livello=(SELECT MAX(IDLivello)
			   FROM livello));
SELECT nome
FROM PersMaxLiv
WHERE danno = (SELECT MAX(danno)
	       FROM oggetto);