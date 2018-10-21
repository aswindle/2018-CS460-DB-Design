--For projects with more than 2 employees:
-- Retrieve project number, project name, number of employees on that project
SELECT p.pname, t1.* 
FROM mccann.project p, (SELECT wo.pno, COUNT(wo.essn)
						FROM mccann.works_on wo
						GROUP BY wo.pno
						HAVING COUNT(wo.essn) >= 2) t1
WHERE p.pnumber = t1.pno;