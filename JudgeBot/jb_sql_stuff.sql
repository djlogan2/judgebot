--select max(fk_gamelog_id) from judgebot.gamelog_to_adjrule
--select  from  where fk_gamelog_id=28745145
--select * from judgebot.adjudicaterules
--
--    Average number of adjudication actions per day
--
select action,avg(ct) from (
select dt,action,count(*) as ct from (
select gl.id,date(gl.adjdate) dt,max(gta.fk_adjudicaterule_id),ar.action from judgebot.gamelog gl,judgebot.gamelog_to_adjrule gta,judgebot.adjudicaterules ar where testonly=0 and engine_nohit=0 and adjudication_nohit=0 and gta.fk_gamelog_id=gl.id and gta.fk_adjudicaterule_id=ar.id group by gl.id,dt,ar.action order by dt desc
) a group by dt,action order by dt desc
) b group by action