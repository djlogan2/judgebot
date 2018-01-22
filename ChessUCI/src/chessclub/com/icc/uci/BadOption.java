package chessclub.com.icc.uci;

/**
 *
 * @author davidlogan
 *
 *         This class gets returned to a UCI interface class when a bad option
 *         is sent to the UCI engine. An instance of this class will be returned
 *         via the badOption() interface call.
 */
public class BadOption {
    private String option;

    /**
     * Constructor that populates the data for the bad option.
     * @param poption The string returned from the UCI engine
     */
    public BadOption(final String poption) {
        this.option = poption;
    }

    /**
     * The option string sent to the UCI engine.
     * @return The option string sent to the UCI engine
     */
    public final String getOption() {
        return option;
    }
}
