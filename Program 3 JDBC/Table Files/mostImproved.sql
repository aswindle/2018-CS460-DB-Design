SELECT *
FROM (SELECT d18.schoolname, d15.percentpass AS Pass2015, d18.percentpass AS Pass2018, SUM(d18.percentpass - d15.percentpass) AS Change
FROM aswindle.data2015 d15, aswindle.data2018 d18
WHERE d15.distno = d18.distno
AND d15.schoolno = d18.schoolno
AND d15.distname = 'MIAMI DADE'
GROUP BY d18.distno, d18.schoolname, d18.percentpass, d15.percentpass
ORDER BY SUM(d18.percentpass - d15.percentpass) DESC NULLS LAST)
WHERE ROWNUM <= 10;