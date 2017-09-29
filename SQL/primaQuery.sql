DROP VIEW IF EXISTS dannoPossiede;

CREATE VIEW dannoPossiede AS (SELECT mostro, oggetto, danno 
FROM possiede as p JOIN oggetto as o 
ON p.oggetto = o.nome);

SELECT mostro,oggetto 
FROM dannoPossiede
WHERE danno=(SELECT MAX(danno)
	    FROM dannoPossiede);