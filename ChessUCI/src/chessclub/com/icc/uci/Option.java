package chessclub.com.icc.uci;

/**
 * The class that contain a UCI engine option. There are a variety of ways to
 * get and set options, min/max, string/numeric, etc.
 *
 * @author davidlogan
 *
 */
public class Option {
	public enum OptionType {
		CHECK,
		SPIN,
		COMBO,
		BUTTON,
		STRING
	};
    /**
     * The name of the option.
     *
     * @return The name of the option.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the type of option.
     *
     * @return The type of option.
     *  <br>There are 5 different types of options the engine supports:
        <ul>
        <li><b>check</b><br>
            a checkbox that can either be true or false</li>
        <li><b>spin</b><br>
            a spin wheel that can be an integer in a certain range
        <li><b>combo</b></br>
            a combo box that can have different predefined strings as a value</li>
        <li><b>button</b><br>
            a button that can be pressed to send a command to the engine</li>
        <li><b>string</b><br>
            a text field that has a string as a value,<br>
            an empty string has the value ""</li>
       </ul>
     */
    public final OptionType getType() {
        return type;
    }

    /**
     * The type of variable.
     * @param ptype
     *
     *  <br>There are 5 different types of options the engine supports:
        <ul>
        <li><b>check</b><br>
            a checkbox that can either be true or false</li>
        <li><b>spin</b><br>
            a spin wheel that can be an integer in a certain range
        <li><b>combo</b></br>
            a combo box that can have different predefined strings as a value</li>
        <li><b>button</b><br>
            a button that can be pressed to send a command to the engine</li>
        <li><b>string</b><br>
            a text field that has a string as a value,<br>
            an empty string has the value ""</li>
       </ul>
     */
    public final void setString(final String name, final String defaultValue) {
    	this.name = name;
    	this.type = OptionType.STRING;
    	this.dfltval = defaultValue;
    }
    public final void setCheck(final String name, final boolean defaultValue) {
    	this.name = name;
    	this.type = OptionType.CHECK;
    	this.dfltval = Boolean.toString(defaultValue);
    }
    
    public final void setSpin(final String name, final int defaultValue, final int minValue, final int maxValue) {
    	this.name = name;
    	this.type = OptionType.SPIN;
    	this.dfltval = Integer.toString(defaultValue);
    	this.max = maxValue;
    	this.min = minValue;
    }
    public final void setButton(final String name) {
    	this.name = name;
    	this.type = OptionType.BUTTON;
    }
    public final void setCombo(final String name, final String defaultValue, final String[] values) {
    	this.name = name;
    	this.type = OptionType.COMBO;
    	this.dfltval = defaultValue;
    	this.var = values.clone();
    }
    
    /**
     * Returns the default value.
     * @return The default value.
     */
    public final Integer i_getDefaultValue() {
    	switch(type) {
		case BUTTON:
		case CHECK:
		case COMBO:
		case STRING:
		default:
			return null;
		case SPIN:
			return Integer.parseInt(dfltval);
    	
    	}
    }
    public final Boolean b_getDefaultValue() {
    	switch(type) {
		case BUTTON:
		case COMBO:
		case STRING:
		case SPIN:
		default:
			return null;
		case CHECK:
			return Boolean.parseBoolean(dfltval);
    	
    	}
    }
    public final String s_getDefaultValue() {
    	switch(type) {
		case BUTTON:
		case SPIN:
		case CHECK:
		default:
			return null;
		case COMBO:
		case STRING:
			return dfltval;
    	
    	}
    }
    
    /**
     * Get the minimum value.
     * @return The minimum value.
     */
    public final Integer getMin() {
        return min;
    }

    /**
     * Get the maximum value.
     * @return The maximum value.
     */
    public final Integer getMax() {
        return max;
    }

    /**
     * Get the value of the variable.
     * @return The value of the variable.
     */
    public final String[] getVar() {
        return var;
    }

    private String name;
    private OptionType type;
    private String dfltval;
    private Integer min;
    private Integer max;
    private String[] var; // This is for combo boxes where there can be multiple values
}
