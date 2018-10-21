--SSNs of employees in dept 5 or who supervise employee in dept 5
SELECT e.ssn
FROM mccann.employee e
WHERE e.dno = 5
UNION
SELECT e.superssn
FROM mccann.employee e
WHERE e.dno = 5;