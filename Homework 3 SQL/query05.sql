--Full name of employee with largest salary
SELECT fname, minit, lname
FROM mccann.employee
WHERE salary = (SELECT MAX(salary) FROM mccann.employee);