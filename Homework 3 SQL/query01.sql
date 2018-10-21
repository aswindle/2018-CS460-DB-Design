--Query 1:
--All attributes of employees from dept 5
set echo on
SELECT *
FROM mccann.employee e
WHERE e.dno = 5;