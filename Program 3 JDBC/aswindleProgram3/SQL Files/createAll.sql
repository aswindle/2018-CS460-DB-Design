@ config

DROP TABLE data2015;
DROP TABLE data2016;
DROP TABLE data2017;
DROP TABLE data2018;

CREATE TABLE data2015
	(distno int NOT NULL,
	distname varchar2(14),
	schoolno int NOT NULL,
	schoolname varchar2(37),
	numstudents int,
	meanscore int,
	percentpass int,
	one int,
	two int,
	three int,
	four int,
	five int);
CREATE TABLE data2016
	(distno int NOT NULL,
	distname varchar2(14),
	schoolno int NOT NULL,
	schoolname varchar2(37),
	numstudents int,
	meanscore int,
	percentpass int,
	one int,
	two int,
	three int,
	four int,
	five int);
CREATE TABLE data2017
	(distno int NOT NULL,
	distname varchar2(14),
	schoolno int NOT NULL,
	schoolname varchar2(37),
	numstudents int,
	meanscore int,
	percentpass int,
	one int,
	two int,
	three int,
	four int,
	five int);
CREATE TABLE data2018
	(distno int NOT NULL,
	distname varchar2(14),
	schoolno int NOT NULL,
	schoolname varchar2(37),
	numstudents int,
	meanscore int,
	percentpass int,
	one int,
	two int,
	three int,
	four int,
	five int);
GRANT SELECT ON data2015 TO PUBLIC;
GRANT SELECT ON data2016 TO PUBLIC;
GRANT SELECT ON data2017 TO PUBLIC;
GRANT SELECT ON data2018 TO PUBLIC;
@ insert2015
@ insert2016
@ insert2017
@ insert2018