--Cartesian product of emp first names and dependents names, distinct
SELECT DISTINCT e.fname, d.dependent_name
FROM mccann.employee e, mccann.dependent d;