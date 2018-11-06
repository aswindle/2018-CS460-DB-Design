SELECT *
FROM (SELECT distname, schoolname, SUM(four + five) AS SUM
		FROM aswindle.data2015
		WHERE distname = 'MIAMI DADE'
		GROUP BY distname, schoolname
		ORDER BY SUM(four + five) DESC NULLS LAST)
WHERE ROWNUM <= 5;