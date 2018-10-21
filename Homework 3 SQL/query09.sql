--Full name of each employee and full name of their supervisor
SELECT e1.fname, e1.minit, e1.lname, e2.fname, e2.minit, e2.lname
FROM mccann.employee e1, mccann.employee e2
WHERE e1.superssn = e2.ssn; 