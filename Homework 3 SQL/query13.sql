--Names of departments that employ all sex
--Want: d.dname
--alpha: e.dno, e.sex from employee
--beta: sex
SELECT d.dname
FROM mccann.department d, (
SELECT DISTINCT dno
FROM mccann.employee e
GROUP BY dno
HAVING COUNT(DISTINCT e.sex) =
		(SELECT COUNT(DISTINCT sex)
		FROM mccann.employee
		)) t1
WHERE d.dnumber = t1.dno;