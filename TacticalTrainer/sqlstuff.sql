set schema 'tacticaltrainer';
-- good bad and unknown by # of moves
--select nmoves,sum(good) as good, sum(bad) as bad, sum(unknown) as unknown from (
--select array_length(string_to_array(moves,' '),1) as nmoves, count(*) as good,0 as bad, 0 as unknown from tacticstable where good='y' group by nmoves
--union all
--select array_length(string_to_array(moves,' '),1) as nmoves, 0 as good, count(*) as bad, 0 as unknown from tacticstable where good='n' group by nmoves
--union all
--select array_length(string_to_array(moves,' '),1) as nmoves, 0 as good, 0 as bad, count(*) as unknown from tacticstable where good is null group by nmoves
--) a group by nmoves order by nmoves
--select played,count(*) as ct from tacticstable where good is null and id < (select max(id) from tacticstable where good='y' or good='n') group by played order by played
--select played,count(*) as ct from tacticstable where good='n' group by played order by played
--select max(id) from tacticstable where good='y' or good='n'
--update tacticstable set badreason='oldy' where good='y' and id <= 39590
--update tacticstable set good='n' where id=39529
--select * from tacticsuser where iccuid=3613852
--select * from completedtactic where uid=3613852
--update tacticstable set good='y' where (good is null or good='n') and id in (6646)
--update tacticstable set badreason = null where played >= 161 and good='y'
--update tacticstable set good = null where good='n' and id >= 40258
--select * from tacticsuser where iccuid=3518198
--select * from completedtactic where tactic=7625

--select good,played,count(*) as count from tacticstable where good='y' group by good,played order by played,good
--select * from tacticstable where played = (select max(played) from tacticstable where (good='y' or good is null))
--select * from tacticstable where (badreason is null or badreason<>'oldy') and played=4 and good='n' order by played asc, id desc
--select tu.username, count(ct),min(date_trunc('minute', datecompleted)) as dc from tacticsuser tu, completedtactic ct where tu.username in (select alreadybothered from botheruser) and tu.iccuid = ct.uid group by tu.username order by dc desc
--select dc,count(*) from (select tu.username, count(ct),min(date(datecompleted)) as dc from tacticsuser tu, completedtactic ct where tu.username in (select alreadybothered from botheruser) and tu.iccuid = ct.uid group by tu.username order by dc desc) a group by dc order by dc desc
--select fen,rating,array_length(string_to_array(moves,' '),1) as nmoves, moves from tacticstable where (good='y' or good is null) order by nmoves desc
--delete from tacticstable where good='n' and played=0
--select tu.username,count(*) as ntact from tacticsuser tu, completedtactic ct where ct.uid=tu.iccuid group by tu.username order by ntact desc
--select * from tacticstable where good='n' and played=1 order by id desc
--update tacticstable set winning=null where badreason is not null
--select * from tacticstable where badreason is not null and badreason<>'oldfalse' order by winning
--update tacticstable set badreason='oldfalse' where winning=false
--select * from tacticstable where good='n' and getpiececount(fen)<=5
--select *, (unprocessed + bad + good) as total, bad::float/(unprocessed + bad + good) as badpct, good::float/(unprocessed + bad + good) as goodpct from (select (select count(*) from tacticstable where good is null) as unprocessed, (select count(*) from tacticstable where good='n') as bad, (select count(*) from tacticstable where good='y') as good) a
--select * from completedtactic where datecompleted >= '2014-10-31' and datecompleted < '2014-11-01' order by datecompleted
--select * from completedtactic where datecompleted >= '2014-10-31 05:44' and (datecompleted - (totalsecondstaken || ' seconds')::interval) <= '2014-10-31 05:44'
--select (select date_trunc('second',min(datecompleted)) from completedtactic) + (n || ' second')::interval from generate_series(0,(SELECT floor(EXTRACT(EPOCH FROM (max(datecompleted) - min(datecompleted)))/1000 - 1) FROM completedtactic)::integer) as n
--select generate_series(0,(SELECT floor(EXTRACT(EPOCH FROM (max(datecompleted) - min(datecompleted)))/1000 - 1) FROM completedtactic)::integer)
--select count(*) from completedtactic where datecompleted between (select min(datecompleted) from completedtactic) and (select max(datecompleted) from completedtactic)
--select select generate_series(1,30)
--select * from tacticstable where good is null order by id desc
--select count(*) from tacticstable where played>19 and (good is null or good='y')
--select (select count(*) from tacticstable where good='n') as bad,(select count(*) from tacticstable where good='y') as good,(select count(*) from tacticstable where good is null) as waiting
--select min(id),max(id) from tacticstable where good='n'
--select max(id) from tacticstable
--select * from tacticstable where good='n' and played=4 order by played desc,rating desc;
--select * from (select tu.username,tu.iccuid,tu.rating,count(*) as count from completedtactic ct, tacticsuser tu
--   where tu.iccuid=ct.uid
--   group by tu.username,tu.iccuid,tu.rating order by count desc) a where count > 19;
--select * from tacticstable order by id desc;
--select * from tacticstable where played>19 and (good is null or good='y') order by played desc
select * from tacticstable tt, completedtactic ct, tacticsuser tu
  where tt.id=ct.tactic and ct.uid=tu.iccuid and ct.tactic=17664 order by tu.rating desc
--select * from (select username,count(*) as count from tacticsuser group by username) a where a.count>1
--update tacticstable set good='n' where id=45481;
--select * from tacticstable order by id desc;
--select * from (select tu.username,min(datecompleted) as dc from tacticstable tt, completedtactic ct, tacticsuser tu
--  where tt.id=ct.tactic and ct.uid=tu.iccuid group by tu.username) a where a.dc>='2014-09-23' and a.username='Grump'

--select * from completedtactic where datecompleted >= '2014-01-19 09:37:29.088' order by datecompleted;

--select (userratingafter-userratingbefore) as userdiff,(tacticratingafter-tacticratingbefore) as tacticdiff,* from tacticstable tt, completedtactic ct, tacticsuser tu
--  where tt.id=ct.tactic and ct.uid=tu.iccuid and ct.tactic=40711 order by ct.datecompleted desc

--select * from
--(select count(*) as winning from tacticstable where winning=true and badreason is not null and badreason='oldfalse') a,
--(select count(*) as notwinn from tacticstable where winning=false and badreason is not null and badreason='oldfalse') b,
--(select count(*) as pending from tacticstable where winning is null and badreason is not null and badreason='oldfalse') c

--select * from
--(select count(*) as winning from tacticstable where winning=true) a,
--(select count(*) as notwinn from tacticstable where winning=false) b,
--(select count(*) as pending from tacticstable where winning is null) c

  --update tacticstable set good='y' where good is null and winning=true
--select * from tacticstable where good='y' order by rating desc,played desc;
--select count(*) from tacticstable where good<>'n' and played>0;
--select (select sum(played) from tacticstable) as tt, (select count(*) from completedtactic) as ct;
--select count(*) from completedtactic;
--select (select round(avg(tu.rating),0) as avgusrrating from tacticsuser tu) as avg_tactic,(select round(avg(tt.rating),0) as avgttrating from tacticstable tt where played>0) as avg_player
--select a.rating from (select iccuid,rating,count(*) from tacticsuser tu, completedtactic ct where tu.iccuid=ct.uid group by iccuid,rating) a where a.count>1 order by rating
--select * from tacticstable tt where tt.id not in (select ct.tactic from completedtactic ct where ct.uid=0) and tt.tomove='WHITE' and tt.rating >= 0 and tt.rating <= 9999 order by random() limit 1
--update tacticstable set tomove=(case when split_part(fen,' ',2)='b' THEN 'BLACK' else 'WHITE' END) where tomove is null;
--select case when split_part(fen,' ',2)='b' THEN 'BLACK' else 'WHITE' END from tacticstable where tomove is null;
--select * from tacticstable tt where split_part(tt.fen,' ',1)='1Qb2q2/2P3k1/6B1/p3p1nP/P7/2P3P1/1P3P2/3R2K1';
--select * from tacticsuser;
--select * from tacticstable where played<>0 order by played desc;
--select * from tacticstable order by id desc;
--select distinct tomove from tacticstable
--select * from tacticstable where fen like '2b1r1k1/3n1ppp/2p2n2/q2p2B1/1b1P1N1P/r2BP3/1KQ2PP1/1N1R3R%'
--update tacticstable set winning=true,good='y' where id=6646
--select day,count(*) as ct from (select ct.tactic,min(date_trunc('day', datecompleted)) as day from completedtactic ct group by tactic) a group by day order by day

-- THE NEW ONES
-- select * from tacticstable where id=18150
--select * from completedtactic ct, tacticsuser tu where ct.tactic=44172 and tu.iccuid=ct.uid order by datecompleted
--update tacticstable set good='n' where id in (18086,18089,36823,16515,25480)
--select * from tacticstable where id in (25480)
--update tacticstable set moves='c3e1 h3g2 e1g3 h2g3' where id=16687
--select * from tacticstable where fen like '2k5/pbqp4/1pn1pB2/2p3P1/5n2/1P2P3/P1PP1K2/R2N4%'
--delete from completedtactic where tactic=3324;
--delete from tacticstable where id=3324;
--select * from completedtactic where tactic in (select distinct(id) from tacticstable TT where played <> (select count(*) from completedtactic CT where CT.tactic=TT.id));
--select * from tacticstable TT where TT.played<>(select count(*) from completedtactic CT where CT.tactic=TT.id)
--update tacticstable set good='n' where id=13539 and (good is null or good='y')
--select count(*) from completedtactic;
--update completedtactic set datecompleted=now() where datecompleted is null;
--select max(id) from tacticstable

--update tacticstable set good='n' where id=16622
--update tacticstable set good='n' where id in (select id from (select *,array_length(regexp_split_to_array(moves, E'\\s+'),1) as ml from tacticstable where good is null) a where a.ml=1)
--select ml - (1-(ml % 2)) as ml2,sum(count) as count2 from (select array_length(regexp_split_to_array(moves, E'\\s+'),1) as ml, count(*) as count from tacticstable where good='y' group by ml) a group by ml2 order by ml2
--select id,length(moves) l from tacticstable where good='y' order by l desc
--select * from completedtactic
--delete from completedtactic;
--delete from tacticstable;
--update tacticstable set moves='c5b6 d7e7 d6c8' where id=1311
--select getrandomtactic(1234)
--select *,(select count(*) from completedtactic where uid=TU.iccuid) from tacticsuser TU where TU.iccuid=1736927

--select TT.*,random()*CASE TU. from TacticsTable TT,tacticsuser TU where TT.id not in (-1) and TT.id not in (select CT.tactic from CompletedTactic CT where CT.uid=1736927) and TT.tomove='WHITE'
--insert into tacticsuser (iccuid,rating,username) values (1,1234,'idjit');
--select tu.username, tu.rating, tu.iccuid, (select count(*) as played from completedtactic ct where ct.uid=tu.iccuid) as played from tacticsuser tu where tu.iccuid in (select id from (select username, max(iccuid) as id from tacticsuser group by username) a) order by played desc
--select username,rating from (select tu.username, tu.rating, tu.iccuid, (select count(*) from completedtactic ct where ct.uid=tu.iccuid) as played from tacticsuser tu where tu.iccuid in (select id from (select username, max(iccuid) as id from tacticsuser group by username) a)) b where b.played>=20
--union all
--select username,rating*played/20 as rating from (select tu.username, tu.rating, tu.iccuid, (select count(*) from completedtactic ct where ct.uid=tu.iccuid) as played from tacticsuser tu where tu.iccuid in (select id from (select username, max(iccuid) as id from tacticsuser group by username) a)) b where b.played<20
--order by rating desc
--update tacticstable set moves='g7f8q g8f8 g1g7' where id = 3652;

--delete from completedtactic where tactic=3174;
--delete from tacticstable where id=3174;
--select * from (select ml - (1-(ml % 2)) as ml2,id,fen,moves,played,rating from (select array_length(regexp_split_to_array(moves, E'\\s+'),1) as ml, id,fen,moves,played,rating from tacticstable where good='y') a) b where b.ml2 > 9 order by b.played desc

--select tu.rating from tacticsuser tu;
--select tt.rating from tacticstable tt where tt.played>0;

--
-- How many problems in each rating range
--
--select range,count(*) from (
--select * from (select rating from tacticstable where played>19 and (good='y' or good is null)) a
--join (select column1 as range from (values (200),(300),(400),(500),(600),(700),(800),(900),(1000),(1100),(1200),(1300),(1400),(1500),(1600),(1700),(1800),(1900),(2000),(2100),(2200),(2300),(2400),(2500),(2600),(2700),(2800),(2900),(3000)) as b) as c
--on (range-200<=rating and range+200 >=rating) ) as d group by range order by range asc
--========================================

--========================================
-- Who are the abusers?
--========================================
--select tu.username,abused,normal from (
--select uid,sum(abused) as abused, sum(normal) as normal from
--(
--select uid,1 as abused, 0 as normal from completedtactic ct where secondsforfirstmove is null
--union all
--select uid,0 as abused, 1 as normal from completedtactic ct where secondsforfirstmove is not null
--) a group by uid
--) b
--inner join tacticsuser tu on tu.iccuid=b.uid
--order by abused desc
