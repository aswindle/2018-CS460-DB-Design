--Salaries of employees in dept 5 not on ProductY
SELECT e.salary
FROM mccann.employee e, mccann.works_on wo, mccann.project p
WHERE e.dno = 5
AND e.ssn = wo.essn
AND wo.pno = p.pnumber
AND p.pname != 'ProductY';