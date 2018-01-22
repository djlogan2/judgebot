--select * from (select username,count(*) as count from tacticaltrainer.tacticsuser group by username) a where a.count>1 order by count desc
--select distinct(date(datecompleted)) from tacticaltrainer.completedtactic
--select date_part('hour',datecompleted) as h, count(*) as count from tacticaltrainer.completedtactic group by h
WITH myconstants as (SELECT '2015-03-31'::date as rdate)
select
	(select rdate from myconstants) as dt,
	tacticaltrainer.getactive((select rdate from myconstants)) as act,
	tacticaltrainer.getactive2((select rdate from myconstants),20) as est,
	(select count(*) from tacticaltrainer.completedtactic where date(datecompleted)=(select rdate from myconstants) group by date(datecompleteD)) comp,
	(select count(*) as unique from (select distinct uid from tacticaltrainer.completedtactic where date(datecompleted)=(select rdate from myconstants)) b) uniq