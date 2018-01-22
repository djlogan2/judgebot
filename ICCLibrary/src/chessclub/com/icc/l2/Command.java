package chessclub.com.icc.l2;


/**
 * 
 * @author David Logan
 * <p>136 DG_COMMAND
 * <p>(command all-arguments)
 * <p>Echoes the command being done.  This is only sent to the user issuing
 * the command.  There would be one of these for each command processed
 * in a multi-command line.  Aliases are expanded.  Note that even if the
 * command took several arguments, this DG has only the two fields.
 *
 */
public class Command extends Level2Packet {
    // 136 DG_COMMAND
    // (command all-arguments)
    /**
     * Returns the command that was sent to ICC.
     * @return  The command that was sent to ICC.
     */
    public final String command() {
        return getParm(1);
    }

    /**
     * Returns a single string with all of the command arguments in it.
     * @return  All of the commands arguments. If there weren't any, an empty string will be returned.
     */
    public final String arguments() {
        return getParm(2);
    }
}
