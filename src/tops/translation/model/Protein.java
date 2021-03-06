package tops.translation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import translation.ChainDomainMap;
import translation.StructureFinder;

public class Protein implements Iterable<Chain> {

    private String id;

    private List<Chain> chains;

    public Protein() {
        this("");
    }

    public Protein(String id) {
        this.chains = new ArrayList<>();
        setID(id);
    }
    
    public void setID(String id) {
    	this.id = id.toLowerCase();
    }

    public String getID() {
        return this.id;
    }

    public void addChain(Chain chain) {
        this.chains.add(chain);
    }

    public Map<String, Map<String, String>> toTopsDomainStrings(
    		ChainDomainMap chainDomainMap) {
        Map<String, Map<String, String>> chainDomainStringMap = new HashMap<>();
        for (int i = 0; i < this.chains.size(); i++) {
            Chain chain = (Chain) this.chains.get(i);
            chainDomainStringMap.put(
            		chain.getCathCompatibleLabel(), 
            		chain.toTopsDomainStrings(chainDomainMap));
        }
        return chainDomainStringMap;
    }

    public String[] toTopsChainStringArray() {
        String[] chainStrings = new String[this.chains.size()];
        for (int i = 0; i < this.chains.size(); i++) {
            Chain chain = (Chain) this.chains.get(i);
            chainStrings[i] = chain.toTopsString(new Domain(0));
        }
        return chainStrings;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.chains.size(); i++) {
            Chain chain = (Chain) this.chains.get(i);
            stringBuffer.append(chain.toString());
        }
        return stringBuffer.toString();
    }

	@Override
	public Iterator<Chain> iterator() {
		return this.chains.iterator();
	}

    public List<Chain> getChains() {
        return this.chains;
    }

    public void findStructure(StructureFinder structureFinder) {
        // XXX UGH
        
    }

}
