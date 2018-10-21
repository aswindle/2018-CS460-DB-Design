--Names of male dependents of male employees
SELECT d.dependent_name
FROM mccann.dependent d, mccann.employee e
WHERE d.sex = 'M'
AND d.essn = e.ssn
AND e.sex = 'M';