--SSNs of employees on proposal 10, sorted in descending order
SELECT essn
FROM mccann.works_on
WHERE pno = 10
ORDER BY essn DESC;