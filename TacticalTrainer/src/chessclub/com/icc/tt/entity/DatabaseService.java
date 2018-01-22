package chessclub.com.icc.tt.entity;

// select * from tacticaltrainer.tacticstable tt where tt.id not in (select ct.tactic from tacticaltrainer.completedtactic ct where ct.uid=0) and tt.tomove='WHITE' and tt.rating >= 0 and tt.rating <= 9999 order by random() limit 1

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chessclub.com.icc.tt.DatabaseStatistics;
import chessclub.com.icc.tt.ifac.IDatabaseService;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;

    @Service("databaseService")
    @Transactional
    public class DatabaseService implements IDatabaseService {
        private static final Logger log = LogManager.getLogger(DatabaseService.class);

        private static ArrayList<Integer> noneActive = new ArrayList<Integer>();
        static {
            noneActive.add(-1);
        }
        
        @PersistenceContext
        private EntityManager em;

        @Override
        @Transactional
        public int saveTactic(Fen fen, String moves, Color winningColor, boolean winning /*, int winnerRating, int loserRating*/) {
            log.debug("fen="+fen+", moves="+moves/*+",winnerRating="+winnerRating+",loserRating="+loserRating*/);
            String boardOnly = fen.getFEN().split("\\s+")[0] + "%";
            Long count = (Long) em.createQuery("select count(TT) from TacticsTable TT where TT.fen like :boardonly")
                .setParameter("boardonly",  boardOnly)
                .getSingleResult();
            if(count != 0) {
                log.debug("Skipping tactic because the board position is already in the database");
                return -1;
            }
            
            TacticsTable tt = new TacticsTable();
            tt.setFen(fen);
            tt.setTomove(fen.toMove().other());
            tt.setMoves(moves);
            tt.setRating(1600); //winnerRating);
            tt.setTomove(winningColor);
            tt.setWinning(winning);
            if(winning)
            	tt.setgood(true);
            try {
                em.persist(tt);
            } catch(Exception e) {
                log.error("ERROR SAVING TACTIC: " + e.getMessage());
            }
            return tt.getId();
        }

        @Override
        @Transactional
        public TacticsUser getUser(String username, int uniqueid, int rating) {
            TacticsUser tu = em.find(TacticsUser.class, uniqueid);
            if(tu == null) {
                tu = new TacticsUser();
                tu.setRating(rating);
                tu.setUid(uniqueid);
                tu.setUsername(username);
                em.persist(tu);
            } else {
                tu.setUsername(username);
            };
            tu.getGamecount(); // Touch this so the lazy loading loads.
            return tu;
        }

        @Override
        @Transactional(readOnly=true)
        public TacticsTable getRandomTactic(Collection<Integer> ineligible_ids, TacticsUser user, Color tomove, int rating) {

            TacticsTable tt = null;
            
            int minrating = 0;
            int maxrating = 9999;
            if(user.getGamecount() < 20) {
                minrating = user.getRating() - 400;
                maxrating = user.getRating() + 400;
            }
            
            if(ineligible_ids == null || ineligible_ids.size() == 0)
                ineligible_ids = noneActive;

            try {
              tt = em.createQuery("select TT from TacticsTable TT where " +
                                      "(TT.good is null or TT.good='y') and " +
                                      "TT.rating> :minrating and TT.rating < :maxrating and " +
                                      "TT.id not in :ineligibles and " +
                                      "TT.id not in (select CT.tactic from CompletedTactic CT where CT.uid=:uniqueid) and " +
                                      "TT.tomove=:tomove and " +
                                      (user.getOnlywinning() ? "TT.winning=true and " : "") +
                                      "(TT.played > 20 or (TT.rating > (:playerrating - 400) and TT.rating < (:playerrating + 400))) " +
                                      "order by good asc nulls last, winning desc nulls last, ((4-abs(round(TT.rating/100,0) - round(:playerrating / 100,0)))/4 * " +
                                      "           (TT.played % 20)/20 * random()) desc",
                                  TacticsTable.class)
                    .setParameter("uniqueid",  user.getUid())
                    .setParameter("tomove",  tomove)
                    .setParameter("minrating",  minrating)
                    .setParameter("maxrating",  maxrating)
                    .setParameter("ineligibles",  ineligible_ids)
                    .setParameter("playerrating",  user.getRating())
                    .setMaxResults(1)
                    .getSingleResult();
            } catch(NoResultException e) {
            }
            return tt;
        }
        
        @Override
        @Transactional
        public void solvedTactic(CompletedTactic ct) {
            TacticsTable tt = em.find(TacticsTable.class, ct.gettactic());
            TacticsUser tu = em.find(TacticsUser.class,  ct.getuid());
            tt.newGame(ct.getnewTacticRating());
            tu.newGame(ct.getnewUserRating());
            em.persist(ct);
        }

        @SuppressWarnings("unchecked")
        @Override
        @Transactional(readOnly=true)
        public DatabaseStatistics getStatistics() {
            DatabaseStatistics ds = new DatabaseStatistics();
            
            int[] tacticalCounts = new int[21];
            tacticalCounts[20] = ((Long) em.createQuery("select count(TT) from TacticsTable TT where (good='y' or good is null) and TT.played >= 20").getSingleResult()).intValue();
            List<Object[]> countList = em.createNativeQuery("select count(TT),played from tacticaltrainer.tacticstable TT where (good='y' or good is null) and TT.played<20 group by played").getResultList();
            for(Object[] o : countList) {
                tacticalCounts[(Integer) o[1]] = ((BigInteger) o[0]).intValue();
            }
            ds.setNumberTactics(tacticalCounts);
            
            tacticalCounts = new int[21];
            tacticalCounts[20] = ((BigInteger)em.createNativeQuery("select count(*) from (select count(*),uid from tacticaltrainer.completedtactic group by uid) a where a.count>=20").getSingleResult()).intValue();
            countList = em.createNativeQuery("select count(*),numgames from (select count(*) as numgames,uid from tacticaltrainer.completedtactic group by uid) a where a.numgames<20 group by a.numgames").getResultList();
            for(Object[] o : countList) {
                tacticalCounts[((BigInteger)o[1]).intValue()] = ((BigInteger)o[0]).intValue();
            }
            
            ds.setNumberPlayers(tacticalCounts);
            ds.setAvgPlayerRating(((BigDecimal) em.createNativeQuery("select avg(rating) from tacticaltrainer.tacticsuser TU where TU.iccuid in (select uid from (select count(*) as count,CT.uid from tacticaltrainer.completedtactic CT group by CT.uid) a where a.count > 19)").getSingleResult()).intValue());
            ds.setAvgTacticRating(((Double) em.createQuery("select avg(TT.rating) from TacticsTable TT where (good='y' or good is null) and played>19").getSingleResult()).intValue());
            Object[] obj = (Object[])em.createNativeQuery("select rating,username from tacticaltrainer.tacticsuser TU where TU.iccuid in (select uid from (select count(*) as count,CT.uid from tacticaltrainer.completedtactic CT group by CT.uid) a where a.count > 19) order by TU.rating desc").setMaxResults(1).getSingleResult();
            ds.setHighestRated(((String)obj[1]).trim());
            ds.setHighRating((Integer)obj[0]);
            Object[] maxPlayed = (Object[]) em.createQuery("select count(*) as gamesPlayed, uid from CompletedTactic CT group by CT.uid order by gamesPlayed desc").setMaxResults(1).getSingleResult();
            TacticsUser user = em.createQuery("select TU from TacticsUser TU where iccuid=:uid",  TacticsUser.class).setParameter("uid",  (Integer)maxPlayed[1]).getSingleResult();
            ds.setMostTacticsPlayed(user.getUsername().trim());
            ds.setGamesPlayed(((Long)maxPlayed[0]).intValue());
            return ds;
        }

        @Override
        @Transactional(readOnly=true)
        public Collection<TacticsTable> getAllTactics() {
            return em.createQuery("select TT from TacticsTable TT where TT.good is null or TT.good='y'", TacticsTable.class).getResultList();
        }

        @Override
        @Transactional
        public void updateGoodAndComment(int id, boolean good, String comment)
        {
            TacticsTable tt = em.find(TacticsTable.class, id);
            tt.setgood(good);
            tt.setBadreason(comment);
            em.flush();
        }
        
        @Override
        @Transactional
        public void updateFen(int id, Fen fen) {
        	TacticsTable tt = em.find(TacticsTable.class,  id);
        	tt.setFen(fen);
        }
        
        @Override
        @Transactional(readOnly=true)
        public Collection<TacticsTable> getNullTactics() {
            return em.createQuery("select TT from TacticsTable TT where TT.good is null order by played asc, id desc", TacticsTable.class).getResultList();
        }

        @Override
        @Transactional(readOnly=true)
        public Collection<TacticsTable> getNullWinningTactics() {
            return em.createQuery("select TT from TacticsTable TT where TT.winning is null order by rating desc", TacticsTable.class).getResultList();
        }

        @Override
        @Transactional
        public void good(TacticsTable incoming_tt) {
            TacticsTable tt = em.find(TacticsTable.class, incoming_tt.getId());
            tt.setgood(incoming_tt.getgood());
            tt.setWinning(incoming_tt.getWinning());
            tt.setBadreason(incoming_tt.getBadreason());
        }

        @Override
        @Transactional
        public void winning(TacticsTable incoming_tt) {
            TacticsTable tt = em.find(TacticsTable.class, incoming_tt.getId());
            tt.setWinning(incoming_tt.getWinning());
        }

        @Override
        public String getMessage(Locale locale, String code) {
            Message m = null;
            if(locale.getCountry() != null && locale.getCountry().length() != 0)
                try {
                    m = (Message)em.createQuery("select M from Message M where M.code=:code and M.language=:language and M.country=:country")
                        .setParameter("code",  code)
                        .setParameter("language",  locale.getLanguage())
                        .setParameter("country",  locale.getCountry())
                        .getSingleResult();
                } catch(NoResultException e) {
                }
            
            if(m == null) {
                try {
                    m = (Message)em.createQuery("select M from Message M where M.code=:code and M.language=:language")
                            .setParameter("code",  code)
                            .setParameter("language",  locale.getLanguage())
                            .getSingleResult();
                } catch(NoResultException e) {
                }
            }
            if(m == null)
                return getMessage(new Locale("en", "US"), code);
            return m.getText();
        }
        
        @Override
        @Transactional(readOnly=true)
        public int getRanking(int uid) {
            BigInteger result = (BigInteger)em.createNativeQuery("select tacticaltrainer.getranking(?)")
                    .setParameter(1,  uid)
                    .getSingleResult();
            return (result == null ? 0 : result.intValue());
        }
        
        @Override
        public int getRanking(TacticsUser user) {
            return getRanking(user.getUid());
        }
        
        @Override
        @Transactional(readOnly=true)
        public GameToCheck getNextGameToCheck() {
        	GameToCheck gtc = (GameToCheck) em.createQuery("select C from GameToCheck where C.datecompleted is null").getSingleResult();
        	return gtc;
        }
        
        @Override
        @Transactional
        public void saveGameToCheck(String who, int gameid) {
        	GameToCheck gtc = new GameToCheck(who, gameid);
        	em.persist(gtc);
        }
        
        @Override
        @Transactional
        public void saveGametoCheck(String who, Fen fen) {
        	GameToCheck gtc = new GameToCheck(who, fen);
        	em.persist(gtc);
        }
        
        @Override
        @Transactional(readOnly=true)
        public Collection<String> getAllUsers() {
            return em.createQuery("select distinct(TU.username) from TacticsUser TU", String.class).getResultList();
        }
        @Override
        @Transactional
        public boolean hasBeenBothered(String who) {
        	Long count = em.createQuery("select count(TU) from TacticsUser TU where TU.username=:who", Long.class)
        	.setParameter("who",  who)
        	.getSingleResult();
        	if(count != 0)
        		return true;
        	BigInteger bi = (BigInteger) em.createNativeQuery("select count(*) from tacticaltrainer.botheruser where alreadybothered=:who")
        			.setParameter("who",  who)
        			.getSingleResult();
        	if(bi.intValue() != 0)
        		return true;
        	em.createNativeQuery("insert into tacticaltrainer.botheruser (alreadybothered) values (:who)").setParameter("who", who).executeUpdate();
        	return false;
        }
        
        @Override
        public ArrayList<TacticUserStat> rankByRating(int mingames, String who) {
        	@SuppressWarnings("unchecked")
			Collection<Object> results = em.createNativeQuery("select username,played,correct, " +
        	        "(select max(userratingafter) from tacticaltrainer.establishedtactics(uid)) as highrating,lastcompleted,rating from (" +
        			"select * from ( " +
        			"select uid,count(*) as played, " +
        			   "sum(case when firstwrongmove is null and secondsforfirstmove is not null then 1 else 0 end) as correct, " +
        			   "max(datecompleted) as lastcompleted " +
        			"from tacticaltrainer.completedtactic ct " +
        			"group by uid) a " +
        			"where a.played>=:mingames " +
        			") b " +
        			"inner join tacticaltrainer.tacticsuser tu " +
        			"on tu.iccuid=b.uid " +
        			"order by rating desc, lastcompleted desc, correct desc, played desc, uid desc")
        			.setParameter("mingames",  mingames)
        			.getResultList();
        	Iterator<Object> iresults = results.iterator();
        	ArrayList<TacticUserStat> ret = new ArrayList<TacticUserStat>();
        	int start = 1;
        	int rank = 1;
        	if(who != null) {
        		for(; iresults.hasNext() && !((String)((Object[])iresults.next())[0]).trim().equalsIgnoreCase(who); start++);
            	iresults = results.iterator();
        	}
        	if(start - 1 == results.size())
        		return null;
        	else if(start + 20 > results.size())
        		start = results.size() - 20;
        	else if(start > 10)
        		start -= 10;
        	while(iresults.hasNext()) {
        		Object[] six = (Object[]) iresults.next();
        		if(start == 0 || --start == 0) {
	        		TacticUserStat tus = new TacticUserStat();
	        		tus.rank          = rank;
	        		tus.username      = ((String)six[0]).trim();
	        		tus.played        = (six[1] == null ? 0 : ((BigInteger)six[1]).intValue());
	        		tus.correct       = (six[2] == null ? 0 : ((BigInteger)six[2]).intValue());
	        		tus.highestrating = (six[3] == null ? 0 : ((Integer)six[3]).intValue());
	        		tus.lastplayed    = ((Date)six[4]);
	        		tus.rating        = (six[5] == null ? 0 : ((Integer)six[5]).intValue());
	        		ret.add(tus);
        		};
        		if(ret.size() == 20) return ret;
        		rank++;
        	}
        	return ret;
        } 
        
        @Override
        public TacticsTable getTactic(int id) {
        	try {
	        	return em.createQuery("select TT from TacticsTable TT  where TT.id=:id", TacticsTable.class)
	        			.setParameter("id",  id)
	        			.getSingleResult();
        	} catch(Exception e) {
        		return null;
        	}
        }

		@Override
		public Integer[][] ratingDetail(String name) {
			@SuppressWarnings("unchecked")
			Collection<Object> results = em.createNativeQuery("select class,sum(correct) as correct, sum(wrong) as wrong from " +
"(" +
   "select round((tacticratingbefore / 200))*200 as class," +
            "case when firstwrongmove is null then 1 else 0 end as correct," +
            "case when firstwrongmove is not null then 1 else 0 end as wrong " +
"from tacticaltrainer.tacticsuser tu, tacticaltrainer.completedtactic tc where lower(tu.username)=:user and tu.iccuid=tc.uid" +
") a group by a.class order by a.class")
					.setParameter("user",  name.toLowerCase())
					.getResultList();
			Integer[][] ret = new Integer[results.size()][3];
			int x = 0;
			Iterator<Object> i = results.iterator();
			while(i.hasNext())
			{
				Object[] o = (Object[])i.next();
				int rating = ((Double)o[0]).intValue();
				int correct = ((BigInteger)o[1]).intValue();
				int wrong = ((BigInteger)o[2]).intValue();
				ret[x][0] = rating;
				ret[x][1] = correct;
				ret[x][2] = wrong;
				x++;
				//ret[x] = new Integer[]{rating, correct, wrong};
				//ret.put(rating, new Integer[]{correct, wrong});
			}
			return (x == 0 ? null : ret);
		}
}
