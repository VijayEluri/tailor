package tailor.source;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import tailor.structure.Chain;
import tailor.structure.Group;
import tailor.structure.Protein;

public class TestPDBReader {
    
    private static final String DIR = "data";
    
    @Test
    public void testRead() throws IOException {
        Protein structure = PDBReader.read(new File(DIR, "test.pdb"));
        
        List<Chain> chains = structure.getChains();
        assertEquals(1, chains.size());
        
        Chain chain = chains.get(0);
        assertEquals("A", chain.getName());
        
        List<Group> groups = chain.getGroups();
        assertEquals(148, groups.size());
        
        Group group = groups.get(0);
        assertEquals(12, group.getAtoms().size());
    }

}
