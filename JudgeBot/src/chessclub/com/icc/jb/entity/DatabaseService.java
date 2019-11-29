package chessclub.com.icc.jb.entity;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chessclub.com.icc.jb.enums.JBAdjudicateAction;
import chessclub.com.icc.jb.enums.SQLReturn;
import chessclub.com.icc.jb.ifac.IDatabaseService;

@Service("databaseService")
@Transactional
public class DatabaseService implements IDatabaseService {
    private static final Logger log = LogManager.getLogger(DatabaseService.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public Page<GameLog> listGames(Pageable page, String white, boolean exact, String black, boolean either) {
		String select = "from GameLog GL";
		String likeorequal = "like";
		if(exact)
		    likeorequal = "=";
		if(either) {
		    if(white != null && black != null) {
		        select += " where (lower(GL.white) "+likeorequal+" :white and lower(GL.black) like :black) or (lower(GL.white) like :black and lower(GL.black) "+likeorequal+" :white)";
		    } else if(white != null) {
		        select += " where lower(GL.white) "+likeorequal+" :white or lower(GL.black) "+likeorequal+" :white";
		    } else if(black != null){
                select += " where lower(GL.white) like :black or lower(GL.black) like :black";
		    }
		} else {
		    if(white != null && black != null) {
		        select += " where lower(GL.white) "+likeorequal+" :white and lower(GL.black) like :black";
		    } else if(white != null) {
                select += " where lower(GL.white) "+likeorequal+" :white";
		    } else if(black != null) {
                select += " where lower(GL.black) like :black";
		    }
		};

		TypedQuery<Long> countQ = em.createQuery("select count(GL) " + select, Long.class);
		TypedQuery<GameLog> recQ = em.createQuery("select GL " + select + " order by GL.adjdate desc", GameLog.class);

		if (white != null) {
		    if(exact) {
                countQ.setParameter("white", white.toLowerCase());
                recQ.setParameter("white", white.toLowerCase());
		    } else {
    			countQ.setParameter("white", "%"+white.toLowerCase()+"%");
    			recQ.setParameter("white", "%"+white.toLowerCase()+"%");
		    }
		}
		;
		if (black != null) {
			countQ.setParameter("black", "%"+black.toLowerCase()+"%");
			recQ.setParameter("black", "%"+black.toLowerCase()+"%");
		}

		Long count = countQ.getSingleResult();
		recQ.setMaxResults(page.getPageSize());
		recQ.setFirstResult((int)page.getOffset());
		List<GameLog> gamelist = recQ.getResultList();
		return new PageImpl<GameLog>(gamelist, page, count.intValue());
	}

	@Override
	@Transactional(readOnly = true)
	public GameLog getGameLog(int id) {
		return em.find(GameLog.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public GameLog getGameLogWithJoins(int id) {
		GameLog game = em.find(GameLog.class, id);

		if(game == null)
			return null;
		
		@SuppressWarnings("unused")
        Object obj;
		Iterator<AdjudicateRule> it = game.getAdjudicaterules().iterator();
		if (it.hasNext())
			obj = it.next();

		Iterator<EngineRule> it2 = game.getEnginerules().iterator();
		if (it2.hasNext())
			obj = it2.next();

		Iterator<GameLogVariable> it3 = game.getVariables().iterator();
		if (it3.hasNext())
			obj = it3.next();

		return game;
	}

	@Override
	@Transactional
	public void addGame(GameLog gl) {
		em.persist(gl);
        log.debug("Added new GameLog with id of "+gl.getId());
	}

	@Override
	@Transactional
	public void addMatchedMissedEngineRule(int gameid, int engine_id, boolean matched) {
        GameLog gl = em.find(GameLog.class, gameid);
        EngineRule er = em.find(EngineRule.class, engine_id, LockModeType.PESSIMISTIC_WRITE);
		Integer lastorder = em
				.createQuery(
						"select max(GTER.ruleorder) from GameLogToEngineRule GTER where GTER.enginerule=:erule and GTER.gamelog=:gamelog",
						Integer.class)
						.setParameter("erule", er)
						.setParameter("gamelog", gl)
						.getSingleResult();

		if(lastorder != null)
            lastorder++;
        else
            lastorder = new Integer(0);
		
        GameLogToEngineRule glter = new GameLogToEngineRule();
        glter.setEnginerule(er);
        glter.setGamelog(gl);
        glter.setRuleorder(lastorder);
        em.persist(glter);
        if(matched)
            er.hit();
        else
            er.missed();
	}

	@Override
	@Transactional
	public void addMatchedMissedAdjudicationRule(int gameid, int adjrule_id, boolean matched) {
        AdjudicateRule ar = em.find(AdjudicateRule.class, adjrule_id, LockModeType.PESSIMISTIC_WRITE);
        GameLog gl = em.find(GameLog.class, gameid);
		Integer lastorder = em
				.createQuery(
						"select max(GTAR.ruleorder) from GameLogToAdjudicateRule GTAR where GTAR.adjudicaterule=:arule and GTAR.gamelog=:gamelog",
						Integer.class).
						setParameter("arule", ar)
						.setParameter("gamelog", gl)
						.getSingleResult();

		if(lastorder != null)
            lastorder++;
        else
            lastorder = new Integer(0);
		
        GameLogToAdjudicateRule gltar = new GameLogToAdjudicateRule();
        gltar.setAdjudicaterule(ar);
        gltar.setGamelog(gl);
        gltar.setRuleorder(lastorder);
        em.persist(gltar);
        
        if(matched)
            ar.hit();
        else
            ar.missed();
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<AdjudicateRule> getActiveAdjudicationRules(boolean deleted) {
	    if(deleted) {
            return em.createQuery("select AR from AdjudicateRule AR order by AR.ruleorder asc", AdjudicateRule.class)
                    .getResultList();
	    } else {
    		return em.createQuery("select AR from AdjudicateRule AR where AR.deleted=0 order by AR.ruleorder asc",
    		        AdjudicateRule.class)
    		        .getResultList();
	    }
	}

    @Override
    @Transactional(readOnly=true)
    public Collection<EngineRule> getActiveEngineRules(boolean deleted) {
        if(deleted) {
            return em.createQuery("select ER from EngineRule ER order by ER.ruleorder asc",
                    EngineRule.class)
                    .getResultList();
        } else {
            return em.createQuery("select ER from EngineRule ER where ER.deleted=0 order by ER.ruleorder asc",
                    EngineRule.class)
                    .getResultList();
        }
    }

	@Override
	@Transactional
	public void setScore(int gameid, boolean ismate, int score) {
	    log.debug("setScore("+gameid+","+ismate+","+score+")");
	    GameLog gl = em.find(GameLog.class, gameid);
	    gl.setScore(score);
	    gl.setMate(ismate);
	}

	@Override
	@Transactional
	public void setTimes(int gameid, int whitemsec, int blackmsec) {
	    log.debug("setTimed("+gameid+","+whitemsec+","+blackmsec+")");
        GameLog gl = em.find(GameLog.class, gameid);
        gl.setWhiteTime(whitemsec);
        gl.setBlackTime(blackmsec);
	}

	@Override
	@Transactional
	public void setVariable(int gameid, String variable, String value) {
	    log.debug("setVariable("+gameid+","+variable+","+value+")");
	    
		boolean newglv = false;
		GameLogVariable glv;
		try {
		glv = em.createQuery("select GLV from GameLogVariable GLV where GLV.variable=:variable and GLV.value=:value",
				GameLogVariable.class)
				.setParameter("variable", variable)
				.setParameter("value", value)
				.getSingleResult();
		} catch(NoResultException e) {
			glv = new GameLogVariable();
			glv.setVariable(variable);
			glv.setValue(value);
			em.persist(glv);
			newglv = true;
		}
		
		if(!newglv) {
		    try {
    			em.createQuery("select GLTV from GameLogToVariable GLTV where GLTV.gamelog.id=:g_id and variable=:variable", GameLogToVariable.class)
    			        .setParameter("g_id", gameid)
    			        .setParameter("variable", glv)
    					.getSingleResult();
		    } catch(NoResultException e) {
		        GameLog gl = em.find(GameLog.class, gameid);
		        GameLogToVariable gtv = new GameLogToVariable();
		        gtv.setGamelog(gl);
		        gtv.setVariable(glv);
		        em.persist(gtv);
		    }
		}
	}

	@Override
    @Transactional(readOnly=true)
	public GameLog getLastGame(String player1, String player2) {
		return em.createQuery("select GL from GameLog GL where (lower(GL.white)=:p1 and lower(GL.black)=:p2) or (lower(GL.white)=:p2 and lower(GL.black)=:p1) and GL.testonly=0 and GL.engine_nohit=0 and GL.adjudication_nohit=0 order by GL.adjdate desc", GameLog.class)
			.setParameter("p1", player1)
			.setParameter("p2", player2)
			.getSingleResult();
	}

	@Override
	@Transactional(readOnly=true)
	public String getWinExempt(int id) {
		WinExempt we = em.find(WinExempt.class, id);
		if(we != null)
			return we.getIcchandle();
		else
			return null;
	}

	@Override
    @Transactional(readOnly=true)
	public String getNoLose(int id) {
		LoseExempt le = em.find(LoseExempt.class, id);
		if(le != null)
			return le.getIcchandle();
		else
			return null;
	}

	@Override
	@Transactional
	public SQLReturn addLoseExempt(int loserid, String loser, String handle) {
		LoseExempt le = em.find(LoseExempt.class, loserid);
		if(le == null) {
			le = new LoseExempt();
			le.setAddedby(handle);
			le.setCreated(new Date());
			le.setIcchandle(loser);
			le.setUniquehandle(loserid);
			em.persist(le);
			return SQLReturn.OK;
		} else if(!loser.equals(le.getIcchandle())) {
			le.setIcchandle(loser);
			le.setAddedby(handle);
			le.setCreated(new Date());
			return SQLReturn.OK;
		} else {
			return SQLReturn.DUPLICATE;
		}
	}

	@Override
	@Transactional
	public SQLReturn removeLoseExempt(int loserid) {
		LoseExempt le = em.find(LoseExempt.class, loserid);
		if(le != null)
			em.remove(le);
		return SQLReturn.OK;
	}

	@Override
	@Transactional
	public SQLReturn addWinExempt(int uniqueId, String winner, String handle) {
		WinExempt we = em.find(WinExempt.class, uniqueId);
		if(we == null) {
			we = new WinExempt();
			we.setCreated(new Date());
			we.setIcchandle(winner);
			we.setUniquehandle(uniqueId);
			em.persist(we);
			return SQLReturn.OK;
		} else if(!winner.equals(we.getIcchandle())) {
			we.setIcchandle(winner);
			we.setCreated(new Date());
			return SQLReturn.OK;
		} else {
			return SQLReturn.DUPLICATE;
		}
	}

	@Override
	@Transactional
	public SQLReturn removeWinExempt(int uniqueId) {
		WinExempt we = em.find(WinExempt.class, uniqueId);
		if(we != null) {
			em.remove(we);
			return SQLReturn.OK;
		} else {
		    return SQLReturn.NORECORDS;
		}
	}

	private Integer get(Object obj) {
	    if(obj == null)
	        return null;
	    else if(obj instanceof Integer)
	        return (Integer)obj;
	    else if(obj instanceof String)
	        return (((String)obj).length() == 0 ? null : Integer.parseInt((String)obj));
	    else
	        throw new IllegalArgumentException();
	}
    @Override
    @Transactional
    public void updateAdjudicateRules(List<Map<String, Object>> updateArray) throws Exception {
        int updated = 0;
        for(Map<String,Object> update : updateArray) {
            if("new".equals(update.get("function"))) {
                String ruletext = (String) update.get("ruletext");
                int ruleorder = get(update.get("ruleorder"));
                String action = (String) update.get("ruleaction");
                AdjudicateRule ar = new AdjudicateRule();
                ar.setAction(JBAdjudicateAction.valueOf(action));
                ar.setRule(ruletext);
                ar.setRuleorder(ruleorder);
                ar.setCreatedBy("unknown");
                em.persist(ar);
                updated++;
            } else if("delete".equals(update.get("function"))) {
                int id = get(update.get("id"));
                updated += em.createQuery("update AdjudicateRule AR set AR.deleted=1 where AR.deleted=0 and AR.id=:id")
                            .setParameter("id", id)
                            .executeUpdate();
            } else if("undelete".equals(update.get("function"))) {
                int id = get(update.get("id"));
                updated += em.createQuery("update AdjudicateRule AR set AR.deleted=0 where AR.deleted=1 and AR.id=:id")
                .setParameter("id", id)
                .executeUpdate();
            } else if("order".equals(update.get("function"))) {
                int id = get(update.get("id"));
                int ruleorder = get(update.get("ruleorder"));
                updated += em.createQuery("update AdjudicateRule AR set AR.ruleorder=:order where AR.id=:id")
                .setParameter("id", id)
                .setParameter("order", ruleorder)
                .executeUpdate();
            } else {
                em.getTransaction().rollback();
                throw new Exception("Unknown function: "+update.get("function"));
            }
        }
        if(updated > 0)
            em.createQuery("update AdjudicateRule AR set AR.rulehit=0,AR.rulemissed=0").executeUpdate();
    }

    @Override
    @Transactional
    public void updateEngineRules(List<Map<String, Object>> updateArray) throws Exception {
        int updated = 0;
        for(Map<String,Object> update : updateArray) {
            if("new".equals(update.get("function"))) {
                String ruletext = (String) update.get("ruletext");
                int ruleorder = get(update.get("ruleorder"));
                EngineRule er = new EngineRule();
                er.setRuleorder(ruleorder);
                er.setRule(ruletext);
                er.setCreatedby("unknown");
                if((Boolean)update.get("skip")) {
                    er.setSkip(true);
                } else {
                    er.setSkip(false);
                    er.setMovetime(get(update.get("movetime")));
                    er.setMate(get(update.get("mate")));
                    er.setWtime(get(update.get("wtime")));
                    er.setWinc(get(update.get("winc")));
                    er.setBtime(get(update.get("btime")));
                    er.setBinc(get(update.get("binc")));
                    er.setDepth(get(update.get("depth")));
                    er.setMovestogo(get(update.get("movestogo")));
                    er.setNodes(get(update.get("nodes")));
                };
                em.persist(er);
                updated++;
            } else if("delete".equals(update.get("function"))) {
                int id = get(update.get("id"));
                updated += em.createQuery("update EngineRule ER set ER.deleted=1 where ER.deleted=0 and ER.id=:id")
                            .setParameter("id", id)
                            .executeUpdate();
            } else if("undelete".equals(update.get("function"))) {
                int id = get(update.get("id"));
                updated += em.createQuery("update EngineRule ER set ER.deleted=0 where ER.deleted=1 and ER.id=:id")
                .setParameter("id", id)
                .executeUpdate();
            } else if("order".equals(update.get("function"))) {
                int id = get(update.get("id"));
                int ruleorder = get(update.get("ruleorder"));
                updated += em.createQuery("update EngineRule ER set ER.ruleorder=:order where ER.id=:id")
                .setParameter("id", id)
                .setParameter("order", ruleorder)
                .executeUpdate();
            } else {
                em.getTransaction().rollback();
                throw new Exception("Unknown function: "+update.get("function"));
            }
        }
        if(updated > 0)
            em.createQuery("update EngineRule ER set ER.rulehit=0,ER.rulemissed=0").executeUpdate();
    }

    @Override
    @Transactional
    public void adjudicationRuleHit(int gameid, boolean matched, JBAdjudicateAction action) {
    	GameLog gl = em.find(GameLog.class,  gameid);
    	if(matched)
    	{
    		gl.setAdjudicationNoHit(0);
    		gl.setfinalAction(action);
    	}
    	else
    		gl.setAdjudicationNoHit(1);
    }

    @Override
    @Transactional
    public void engineRuleHit(int gameid, boolean matched) {
    	GameLog gl = em.find(GameLog.class,  gameid);
    	gl.setEngineNoHit((matched?0:1));
    }

    @Override
    @Transactional
    public void setMoves(int gameid, String lasttwomoves) {
        GameLog gl = em.find(GameLog.class, gameid);
        gl.setLastTwoMoves(lasttwomoves);
    }

    @Override
    @Transactional(readOnly=true)
    public Page<WinExempt> getWinExemptList(String searchString, PageRequest pageRequest) {
        Long count;
        List<WinExempt> list;
        
        if(searchString != null) {
            count = em.createQuery("select count(WE) from WinExempt WE where WE.icchandle like :handle", Long.class)
                            .setParameter("handle", "%"+searchString.toLowerCase()+"%")
                            .getSingleResult();
            list = em.createQuery("select WE from WinExempt WE where WE.icchandle like :handle order by WE.icchandle asc", WinExempt.class)
                    .setParameter("handle", "%"+searchString.toLowerCase()+"%")
                    .setFirstResult((int)pageRequest.getOffset())
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();
        } else {
            count = em.createQuery("select count(WE) from WinExempt WE", Long.class)
                    .getSingleResult();
            list = em.createQuery("select WE from WinExempt WE order by WE.icchandle asc", WinExempt.class)
                    .setFirstResult((int)pageRequest.getOffset())
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();
        }
        return new PageImpl<WinExempt>(list, pageRequest, count.intValue());
    }

    @Override
    @Transactional(readOnly=true)
    public Page<LoseExempt> getLoseExemptList(String searchString, PageRequest pageRequest) {
        Long count;
        List<LoseExempt> list;
        
        if(searchString != null) {
            count = em.createQuery("select count(LE) from LoseExempt LE where LE.icchandle like :handle", Long.class)
                            .setParameter("handle", "%"+searchString.toLowerCase()+"%")
                            .getSingleResult();
            list = em.createQuery("select LE from LoseExempt LE where LE.icchandle like :handle order by LE.icchandle asc", LoseExempt.class)
                    .setParameter("handle", "%"+searchString.toLowerCase()+"%")
                    .setFirstResult((int)pageRequest.getOffset())
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();
        } else {
            count = em.createQuery("select count(LE) from LoseExempt LE", Long.class)
                    .getSingleResult();
            list = em.createQuery("select LE from LoseExempt LE order by LE.icchandle asc", LoseExempt.class)
                    .setFirstResult((int)pageRequest.getOffset())
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();
        }
        return new PageImpl<LoseExempt>(list, pageRequest, count.intValue());
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public DisconnectStats getDisconnectStats(String who)
    {
    	DisconnectStats ds = new DisconnectStats();
    	List list = em.createNativeQuery("select " +
    							"case when disconnected=1 then 'disconnected' else 'victim' end as type, count(*) as count, sum(won) as won, sum(lost) as lost, sum(drawn) as drown, sum(aborted) as aborted, sum(manual) as manual " +
    							"from " +
    							"(select " +
    							"case when whodisconnected=2 then 1 else 0 end as disconnected," +
								"case when whodisconnected=1 then 1 else 0 end as victim," +
								"case when (finalaction=0 and whodisconnected = 2) or (finalaction=1 and whodisconnected=1) then 1 else 0 end as won," +
								"case when (finalaction=0 and whodisconnected = 1) or (finalaction=1 and whodisconnected=2) then 1 else 0 end as lost," +
								"case when finalaction=2 then 1 else 0 end as drawn," +
								"case when finalaction=3 then 1 else 0 end as aborted," +
								"case when (finalaction=4 or finalaction is null) then 1 else 0 end as manual " +
								"from judgebot.gamelog where lower(white)=:who " +
								"union all " +
								"select " +
								"case when whodisconnected=1 then 1 else 0 end as disconnected," +
								"case when whodisconnected=2 then 1 else 0 end as victim," +
								"case when (finalaction=0 and whodisconnected = 1) or (finalaction=1 and whodisconnected=2) then 1 else 0 end as won," +
								"case when (finalaction=0 and whodisconnected = 2) or (finalaction=1 and whodisconnected=1) then 1 else 0 end as lost," +
								"case when finalaction=2 then 1 else 0 end as drawn," +
								"case when finalaction=3 then 1 else 0 end as aborted," +
								"case when (finalaction=4 or finalaction is null) then 1 else 0 end as manual " +
								"from judgebot.gamelog where lower(black)=:who" +
								") a group by disconnected,victim order by type asc")
    	.setParameter("who",  who.toLowerCase())
    	.getResultList();
    	Iterator i = list.iterator();
    	
    	while(i.hasNext()) {
    		Object[] o1 = (Object[])i.next();
    		if(o1[0].equals("disconnected"))
    		{
		    	ds.disconnected.won     = ((BigInteger)o1[2]).intValue();
		    	ds.disconnected.lost    = ((BigInteger)o1[3]).intValue();
		    	ds.disconnected.drawn   = ((BigInteger)o1[4]).intValue();
		    	ds.disconnected.aborted = ((BigInteger)o1[5]).intValue();
		    	ds.disconnected.manual  = ((BigInteger)o1[6]).intValue();
    		}
    		else
    		{
		    	ds.victim.won     = ((BigInteger)o1[2]).intValue();
		    	ds.victim.lost    = ((BigInteger)o1[3]).intValue();
		    	ds.victim.drawn   = ((BigInteger)o1[4]).intValue();
		    	ds.victim.aborted = ((BigInteger)o1[5]).intValue();
		    	ds.victim.manual  = ((BigInteger)o1[6]).intValue();
    		}
    	}
    	return ds;
    }
}
