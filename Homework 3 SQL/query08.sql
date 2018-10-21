--Birthdates of managers of departments with projects in Stafford
SELECT e.bdate
FROM mccann.employee e, mccann.project p, mccann.department d
WHERE p.plocation = 'Stafford'
AND p.dnum = d.dnumber
AND e.ssn = d.mgrssn;