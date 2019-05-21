//Create keyspace
CREATE KEYSPACE IF NOT EXISTS TrafficKeySpace
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 1
};


//Create table

CREATE TABLE TrafficKeySpace.Window_Tweet_Traffic (
	routeId text,
	totalCount bigint,
	timeStamp timestamp,
	recordDate text,
	PRIMARY KEY (routeId, timeStamp)
);

CREATE TABLE TrafficKeySpace.TweetsUsuario_Trafic (
	name text,
	mensagem text,
	idioma text,
	totalCount bigint,
	timeStamp timestamp,
	recordDate text,
	PRIMARY KEY (name, mensagem, idioma, recordDate)
);

CREATE TABLE TrafficKeySpace.TweetsFollwers_Trafic (
	name text,
	totalCount bigint,
	timeStamp timestamp,
	recordDate text,
	PRIMARY KEY (name, recordDate)
);



//Select from table


SELECT * FROM TrafficKeySpace.Window_Tweet_Traffic;
SELECT * FROM TrafficKeySpace.TweetsFollwers_Trafic;
SELECT * FROM TrafficKeySpace.TweetsUsuario_Trafic;
