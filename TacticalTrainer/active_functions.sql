-- Function: tacticaltrainer.getactive(date)

-- DROP FUNCTION tacticaltrainer.getactive(date);

CREATE OR REPLACE FUNCTION tacticaltrainer.getactive(date)
  RETURNS bigint AS
$BODY$select count(*) from (select distinct(uid) from tacticaltrainer.completedtactic where datecompleted >= ($1 - interval '7' day) and datecompleted <= $1) a$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION tacticaltrainer.getactive(date)
  OWNER TO tacticaltrainer;
-- Function: tacticaltrainer.getactive2(date, integer)

-- DROP FUNCTION tacticaltrainer.getactive2(date, integer);

CREATE OR REPLACE FUNCTION tacticaltrainer.getactive2(date, integer)
  RETURNS bigint AS
$BODY$select count(*) from (select distinct(uid) from tacticaltrainer.completedtactic where datecompleted >= ($1 - interval '7' day) and datecompleted <= $1 and uid in (select uid from (select uid,count(*) as count from tacticaltrainer.completedtactic where datecompleted <= $1 group by uid) a where a.count>=$2)) a$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION tacticaltrainer.getactive2(date, integer)
  OWNER TO tacticaltrainer;
-- Function: tacticaltrainer.getallactive()

-- DROP FUNCTION tacticaltrainer.getallactive();

CREATE OR REPLACE FUNCTION tacticaltrainer.getallactive()
  RETURNS TABLE(dt date, active integer) AS
$BODY$
declare
	ddt timestamp; --tacticaltrainer.completedtactic%rowtype;
begin
	FOR ddt IN (SELECT DISTINCT DATE(datecompleted) AS xdt FROM tacticaltrainer.completedtactic ORDER BY xdt)
	LOOP
		dt := ddt;
		active := tacticaltrainer.getactive(cast(ddt as date));
		RETURN NEXT;
	END LOOP;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION tacticaltrainer.getallactive()
  OWNER TO tacticaltrainer;
-- Function: tacticaltrainer.getallactive(integer)

-- DROP FUNCTION tacticaltrainer.getallactive(integer);

CREATE OR REPLACE FUNCTION tacticaltrainer.getallactive(IN integer)
  RETURNS TABLE(dt date, active integer) AS
$BODY$
declare
	ddt timestamp; --tacticaltrainer.completedtactic%rowtype;
begin
	FOR ddt IN (SELECT DISTINCT DATE(datecompleted) xdt FROM tacticaltrainer.completedtactic ORDER BY xdt)
	LOOP
		dt := ddt;
		active := tacticaltrainer.getactive2(cast(ddt as date),$1);
		RETURN NEXT;
	END LOOP;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION tacticaltrainer.getallactive(integer)
  OWNER TO tacticaltrainer;
