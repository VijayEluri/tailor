package tailor.engine;

import java.util.List;

import org.junit.Test;

import tailor.datasource.Structure;
import tailor.description.ChainDescription;
import tailor.description.GroupDescription;
import tailor.description.RangedGroupDescription;

public class RangedChainSingleEngineTest {
    
    @Test
    public void unrangedTest() {
        // make the data
        Structure protein = StructureFactory.makeChain("GTYPEDFASED");
        
        // make the pattern
        ChainDescription chainDescription = new ChainDescription();
        GroupDescription groupDescriptionA = new GroupDescription();
        GroupDescription groupDescriptionB = new GroupDescription();
        chainDescription.addGroupDescription(groupDescriptionA);
        chainDescription.addGroupDescription(groupDescriptionB);
        
        // match
        RangedSingleChainEngine engine = new RangedSingleChainEngine();
        List<Structure> matches = engine.scan(chainDescription, protein);
        for (Structure match : matches) {
            System.out.println(match);
        }
    }
    
    @Test
    public void rangedTest() {
     // make the data
        Structure protein = StructureFactory.makeChain("GTYPEDFASED");
        
        // make the pattern
        ChainDescription chainDescription = new ChainDescription();
        RangedGroupDescription groupDescription = new RangedGroupDescription();
        chainDescription.addGroupDescription(groupDescription);
        
        // match
        RangedSingleChainEngine engine = new RangedSingleChainEngine();
        List<Structure> matches = engine.scan(chainDescription, protein);
        for (Structure match : matches) {
            System.out.println(match);
        }
    }

}
