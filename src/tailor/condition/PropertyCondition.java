package tailor.condition;

import tailor.datasource.Structure;
import tailor.description.Description;


//TODO : this whole class may be unnecessary...

public class PropertyCondition implements Condition {
	
	private String propertyKey;
	private String propertyValue;
	
	public PropertyCondition(String propertyKey, String propertyValue) {
		this.propertyKey = propertyKey;
		this.propertyValue = propertyValue;
	}
	
	public boolean contains(Description d) {
		// TODO
		return false;
	}

    public Object clone() {
    	return null;	// TODO
    }
    
	public boolean equals(Condition other) {
		if (other instanceof PropertyCondition) {
			PropertyCondition o = (PropertyCondition) other;
//            System.err.println(this + " == " + o);
			if (this.propertyKey.equals(o.propertyKey) && this.propertyValue.equals(o.propertyValue)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean keyEquals(String key) {
		return this.propertyKey.equals(key);
	}

	public boolean valueEquals(String value) {
		return this.propertyValue.equals(value);
	}
	
	public String getValue() {
		return this.propertyValue;
	}
	
	public boolean satisfiedBy(Structure structure) {
		return structure.hasPropertyEqualTo(this.propertyKey, this.propertyValue);
	}
	
    public String toXml() {
    	return "";	//TODO
    }

	public String toString() {
		return String.format("%s : %s", this.propertyKey, this.propertyValue);
	}
}
