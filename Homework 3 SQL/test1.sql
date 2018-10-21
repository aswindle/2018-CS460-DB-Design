SELECT pno
FROM(SELECT wo.pno AS "Pno", COUNT(wo.essn) AS "EmpCount"
FROM mccann.works_on wo
GROUP BY wo.pno
HAVING COUNT(wo.essn) >= 2);