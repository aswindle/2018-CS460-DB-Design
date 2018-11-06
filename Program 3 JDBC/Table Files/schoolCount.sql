SELECT distname, COUNT(schoolno)
FROM aswindle.data2015
WHERE distname = 'MIAMI DADE'
GROUP BY distname;