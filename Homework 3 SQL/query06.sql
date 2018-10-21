--Names of departments with employee with dependent named alice
SELECT dept.dname
FROM mccann.employee e, mccann.department dept, mccann.dependent depn
WHERE depn.dependent_name = 'Alice'
AND depn.essn = e.ssn
AND dept.dnumber = e.dno;