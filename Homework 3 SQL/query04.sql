--Locations of research
SELECT dl.dlocation
FROM mccann.dept_locations dl, mccann.department d
WHERE dl.dnumber = d.dnumber
AND d.dname = 'Research';