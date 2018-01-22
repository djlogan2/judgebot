package chessclub.com.icc.tt;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;

import chessclub.com.icc.tt.ifac.IDatabaseService;

public class I8nMessageSource extends AbstractMessageSource {

    @Autowired
    private IDatabaseService databaseService;

    @Override
    protected MessageFormat resolveCode(String arg0, Locale arg1) {
        if ("ne".equals(arg1.getLanguage()) && "SU".equals(arg1.getCountry())) {
            if ("rtl".equals(arg0))
                return new MessageFormat("y", arg1);
            String rmsg = databaseService.getMessage(arg1, arg0.toLowerCase());
            if (rmsg == null) {
                rmsg = "";
                String msg = databaseService.getMessage(new Locale("en", "US"), arg0.toLowerCase());
                for (int x = 0; x < msg.length(); x++) {
                    if (msg.charAt(x) == '{')
                        rmsg = '}' + rmsg;
                    else if (msg.charAt(x) == '}')
                        rmsg = '{' + rmsg;
                    else
                        rmsg = msg.charAt(x) + rmsg;
                }
            }
            return new MessageFormat(rmsg, arg1);
        }
        ;
        String msg = databaseService.getMessage(arg1, arg0.toLowerCase());
        if (msg == null)
            return null;
        return new MessageFormat(msg, arg1);
    }

}
