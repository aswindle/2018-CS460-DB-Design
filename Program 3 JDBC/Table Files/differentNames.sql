SELECT COUNT(*) AS Count
FROM (SELECT d15.schoolname, d18.schoolname
FROM aswindle.data2015 d15, aswindle.data2018 d18
WHERE d15.distno = d18.distno
AND d15.schoolno = d18.schoolno
AND d15.schoolname != d18.schoolname);