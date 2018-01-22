package chessclub.com.icc.jb.web;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import chessclub.com.icc.jb.entity.AdjudicateRule;
import chessclub.com.icc.jb.entity.EngineRule;
import chessclub.com.icc.jb.entity.GameLog;
import chessclub.com.icc.jb.entity.LoseExempt;
import chessclub.com.icc.jb.entity.WinExempt;
import chessclub.com.icc.jb.enums.JBAdjudicateAction;
import chessclub.com.icc.jb.icc.AdjudicateBase;
import chessclub.com.icc.jb.ifac.IDatabaseService;
import chessclub.com.icc.parser.ICCParser;
import chessclub.com.icc.parser.ParseException;
import chessclub.com.icc.parser.TokenMgrError;

@RequestMapping("/")
@Controller
public class JudgeBotController {
	@SuppressWarnings("unused")
    private static final Logger log = LogManager.getLogger(JudgeBotController.class);

    @Autowired
    private IDatabaseService databaseService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel, Locale locale) {
        return "startingpoint";
    }
    
    @RequestMapping(value = "/gamelogs", method = RequestMethod.GET)
    public String listgames(Model uiModel, Locale locale) {
    	Page<GameLog> page = databaseService.listGames(new PageRequest(0, 25), null, false, null, true);
    	uiModel.addAttribute("page", page);
    	uiModel.addAttribute("dateformatter", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale));
        return "gamelogs/gamelogs";
    }

    @RequestMapping(value = "/gamelist", method = RequestMethod.POST)
    public String listgames(@RequestParam(value="page") int spage,
                            @RequestParam("pagesize") int pagesize,
    						@RequestParam(value="white", required=false) String white,
    						@RequestParam(value="black", required=false) String black,
    						@RequestParam(value="either", required=false) Boolean either,
    						Model uiModel, Locale locale) {
    	System.out.println("startpage="+spage+",pagesize="+pagesize+",white="+white+",black="+black+",either="+either);
    	if(white != null && white.length() == 0) white = null;
    	if(black != null && black.length() == 0) black = null;
    	
    	if(either == null)
    	    either = new Boolean(true);
    	
    	Page<GameLog> page = databaseService.listGames(new PageRequest(spage, pagesize), white, false, black, either);
    	
    	if(white != null)
    		uiModel.addAttribute("white", white);
    	if(black != null && !either)
    		uiModel.addAttribute("black", black);
    	uiModel.addAttribute("either", either);
    	
    	uiModel.addAttribute("page", page);
    	uiModel.addAttribute("dateformatter", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale));
        return "gamelogs/gamelist";
    }

    @RequestMapping(value = "/game/{id}", method = RequestMethod.GET)
    public String oneGame(@PathVariable("id") int id, Model uiModel, Locale locale) {
    	GameLog game = databaseService.getGameLog(id);
    	uiModel.addAttribute("game", game);
    	uiModel.addAttribute("formatter", DecimalFormat.getInstance(locale));
        return "gamelogs/onegame";
    }

    @RequestMapping(value = "/gamerules/{id}", method = RequestMethod.GET)
    public String gameRules(@PathVariable("id") int id, Model uiModel, Locale locale) {
    	GameLog game = databaseService.getGameLogWithJoins(id);
    	uiModel.addAttribute("game", game);
    	uiModel.addAttribute("formatter", DecimalFormat.getInstance(locale));
        return "gamelogs/rulewindow";
    }

    @RequestMapping(value = "/adjrules", method = RequestMethod.GET)
    public String adjudicateRules(@RequestParam("deleted") boolean deleted, Model uiModel, Locale locale) {
        Collection<AdjudicateRule> activeRules = this.databaseService.getActiveAdjudicationRules(deleted);
        uiModel.addAttribute("rules", activeRules);

        ArrayList<String> variables = new ArrayList<String>();
        variables.addAll(Arrays.asList(AdjudicateBase.getIntVariables()));
        variables.addAll(Arrays.asList(AdjudicateBase.getStringVariables()));
        Collections.sort(variables);
        uiModel.addAttribute("variables", variables);
        
        ArrayList<String> actions = new ArrayList<String>(JBAdjudicateAction.values().length);
        for(JBAdjudicateAction jba : JBAdjudicateAction.values()) {
            actions.add(jba.toString());
        }
        uiModel.addAttribute("actions", actions);
        
        return "adjrules";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/adjrules", method = RequestMethod.POST)
    public String POSTadjudicateRules(@RequestParam("updatearray") String updates,
                                        @RequestParam(value="deleted", defaultValue="false") boolean deleted,
                                        HttpServletRequest request,
                                        Model uiModel,
                                        Locale locale) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<HashMap<String, Object>> convertedUpdates = new ArrayList<HashMap<String,Object>>();
        convertedUpdates = mapper.readValue(updates, convertedUpdates.getClass());
        for(HashMap<String, Object> element : convertedUpdates) {
            for(String key : element.keySet()) {
                if(element.get(key) instanceof String)
                    element.put(key, HtmlUtils.htmlUnescape((String)element.get(key)));
            }
        }
        databaseService.updateAdjudicateRules(convertedUpdates);
      return adjudicateRules(deleted, uiModel, locale);
    }

    @RequestMapping(value = "/json/syntaxcheckrule", method = RequestMethod.POST, produces = "application/text")
    @ResponseBody
    public ResponseEntity<String> syntaxCheckRule(@RequestParam("rule") String rule,
                                                    Model uiModel, Locale locale) {
        String ret = "?";
        AdjudicateBase ab = new AdjudicateBase(null, null, null, null, new String[]{"white", "black"}, true);
            try {
                (new ICCParser(new StringReader(rule))).booleanRule(ab);
                ret = "OK";
            } catch (TokenMgrError e) {
                ret = e.getMessage();
            } catch (ParseException e) {
                ret = e.getMessage();
            } catch (Exception e) {
                ret = e.getMessage();
            }
        return new ResponseEntity<String>(ret, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/enginerules", method = RequestMethod.GET)
    public String engineRules(@RequestParam("deleted") boolean deleted, Model uiModel, Locale locale) {
        Collection<EngineRule> activeRules = this.databaseService.getActiveEngineRules(deleted);
        uiModel.addAttribute("rules", activeRules);

        ArrayList<String> variables = new ArrayList<String>();
        variables.addAll(Arrays.asList(AdjudicateBase.getIntVariables()));
        variables.addAll(Arrays.asList(AdjudicateBase.getStringVariables()));
        Collections.sort(variables);
        uiModel.addAttribute("variables", variables);
        
        return "enginerules";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/enginerules", method = RequestMethod.POST)
    public String POSTengineRules(@RequestParam("updatearray") String updates,
                                        @RequestParam(value="deleted", defaultValue="false") boolean deleted,
                                        HttpServletRequest request,
                                        Model uiModel,
                                        Locale locale) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<HashMap<String, Object>> convertedUpdates = new ArrayList<HashMap<String,Object>>();
        convertedUpdates = mapper.readValue(updates, convertedUpdates.getClass());
        for(HashMap<String, Object> element : convertedUpdates) {
            for(String key : element.keySet()) {
                if(element.get(key) instanceof String)
                    element.put(key, HtmlUtils.htmlUnescape((String)element.get(key)));
            }
        }
        databaseService.updateEngineRules(convertedUpdates);
      return engineRules(deleted, uiModel, locale);
    }

    @RequestMapping(value = "/exempt", method = RequestMethod.GET)
    public String winExempt(Model uiModel, Locale locale) {
        Page<WinExempt> winExemptList = this.databaseService.getWinExemptList(null, new PageRequest(0,25));
        Page<LoseExempt> loseExemptList = this.databaseService.getLoseExemptList(null, new PageRequest(0,25));
        uiModel.addAttribute("winpage", winExemptList);
        uiModel.addAttribute("losepage", loseExemptList);
        return "exempt";
    }

    @RequestMapping(value = "/winexempt", method = RequestMethod.POST)
    public String POSTwinexemptlist(@RequestParam("page") int page,
                                    @RequestParam("pagesize") int pagesize,
                                    @RequestParam(value="search", required=false) String search,
                                        Model uiModel,
                                        Locale locale) throws Exception {
        Page<WinExempt> winExemptList = this.databaseService.getWinExemptList(search, new PageRequest(page,pagesize));
        uiModel.addAttribute("winpage", winExemptList);
        if(search != null)
            uiModel.addAttribute("winsearch", search);
        return "winexempt";
    }
    @RequestMapping(value = "/loseexempt", method = RequestMethod.POST)
    public String POSTloseexemptlist(@RequestParam("page") int page,
                                    @RequestParam("pagesize") int pagesize,
                                    @RequestParam(value="search", required=false) String search,
                                        Model uiModel,
                                        Locale locale) throws Exception {
        Page<LoseExempt> loseExemptList = this.databaseService.getLoseExemptList(search, new PageRequest(0,25));
        uiModel.addAttribute("losepage", loseExemptList);
        if(search != null)
            uiModel.addAttribute("losesearch", search);
        return "loseexempt";
    }
}
