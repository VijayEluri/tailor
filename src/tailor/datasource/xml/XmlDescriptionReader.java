package tailor.datasource.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import tailor.condition.HBondCondition;
import tailor.description.AtomDescription;
import tailor.description.ChainDescription;
import tailor.description.Description;
import tailor.description.GroupDescription;
import tailor.description.ProteinDescription;


/**
 * @author maclean
 *
 */
public class XmlDescriptionReader {
    
    public class XmlMotifHandler extends DefaultHandler {
        
        private Description currentDescription;
        
        private ProteinDescription currentProtein;
        
        private ChainDescription currentChain;
        
        private GroupDescription currentGroup;
        
        private AtomDescription currentAtom;
        
        private Map<String, String> dataStore;
        
        private List<Description> pathMap;
        
        private ProteinDescriptionXmlHandler proteinDescriptionXmlHandler;
        
        private ChainDescriptionXmlHandler chainDescriptionXmlHandler;
        
        private GroupDescriptionXmlHandler groupDescriptionXmlHandler;
        
        private AtomDescriptionXmlHandler atomDescriptionXmlHandler;
        
        public XmlMotifHandler() {
            this.currentProtein = null;
            this.currentChain = null;
            this.currentGroup = null;
            this.currentAtom = null;
            this.dataStore = new HashMap<String, String>();
            this.pathMap = new ArrayList<Description>();
            
            this.proteinDescriptionXmlHandler = new ProteinDescriptionXmlHandler();
            this.chainDescriptionXmlHandler = new ChainDescriptionXmlHandler();
            this.groupDescriptionXmlHandler = new GroupDescriptionXmlHandler();
            this.atomDescriptionXmlHandler = new AtomDescriptionXmlHandler();
        }
        
        public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
            if (qName.equals("ProteinDescription")) {
                this.currentProtein = proteinDescriptionXmlHandler.create(attrs);
                this.currentDescription = this.currentProtein;
            } else if (qName.equals("ChainDescription")) {
                this.currentChain = chainDescriptionXmlHandler.create(attrs, currentProtein);
                this.currentDescription = this.currentChain;
            } else if (qName.equals("GroupDescription")) {
                this.currentGroup = groupDescriptionXmlHandler.create(attrs, currentChain);
                this.currentDescription = this.currentGroup;
            } else if (qName.equals("AtomDescription")) {
                this.currentAtom = atomDescriptionXmlHandler.create(attrs, currentGroup);
                this.currentDescription = this.currentAtom;
            } else if (qName.equals("HBondCondition")) {
                this.dataStore.put("haMax", attrs.getValue("haMax"));
                this.dataStore.put("dhaMin", attrs.getValue("dhaMin"));
                this.dataStore.put("haaMin", attrs.getValue("haaMin"));
                if (attrs.getIndex("isNegated") != -1) {
                    this.dataStore.put("isNegated", attrs.getValue("isNegated"));
                }
            } else if (qName.equals("Path")) {
                
                // create the path
                Description path = null;
                if (this.currentDescription == this.currentProtein) {
                    // TODO : handle paths at the structure level
                    //String chainName = attrs.getValue("chain");
                } else if (this.currentDescription == this.currentChain) {
                    String labelStr = attrs.getValue("label");
                    String atomName = attrs.getValue("atom");
                    path = this.currentChain.getPathByGroupLabel(labelStr, atomName);
                    
                } else if (this.currentDescription == this.currentGroup) {
                    // TODO : handle paths at the group level
                }
                
                // store the path
                System.err.println("adding path " + path.toPathString());
                pathMap.add(path);
            }
        }
        
        public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
            if (qName.equals("HBondCondition")) {
                
                // TODO : these data items might not exist / be complete
                double haMax = Double.parseDouble(this.dataStore.get("haMax"));
                double dhaMin = Double.parseDouble(this.dataStore.get("dhaMin"));
                double haaMin = Double.parseDouble(this.dataStore.get("haaMin"));
                List<Description> paths = this.pathMap;
                Description d  = paths.get(0);
                Description h  = paths.get(1);
                Description a  = paths.get(2);
                Description aa = paths.get(3);
                HBondCondition hbond = new HBondCondition(d, h, a, aa, haMax, dhaMin, haaMin);
                
                if (this.dataStore.containsKey("isNegated")) {
                    boolean isNegated = Boolean.parseBoolean((String) this.dataStore.get("isNegated"));
                    System.err.println("setting negated to " + isNegated);
                    hbond.setNegated(isNegated);
                }
                
                this.currentDescription.addCondition(hbond);
                
                // don't want to accumulate paths
                pathMap.clear();
            } else if (qName.equals("GroupDescription")) {
                this.currentDescription = this.currentChain;
            }
            
        }
        
        public Description getDescription() {
            return (Description) this.currentProtein;
        }
        
    }
    
    public Description readDescription(File file) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XmlMotifHandler handler = new XmlMotifHandler(); 
            saxParser.parse(file, handler);
            return handler.getDescription();
        } catch (SAXException s) {
            System.out.println(s);
            return null;
        } catch (IOException ioe) {
            System.out.println(ioe);
            return null;
        } catch (ParserConfigurationException pce) {
            System.out.println(pce);
            return null;
        }
    }
    
    public Description readDescription(InputStream stream) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XmlMotifHandler handler = new XmlMotifHandler(); 
            saxParser.parse(new InputSource(stream), handler);
            return handler.getDescription();
        } catch (SAXException s) {
            System.out.println(s);
            return null;
        } catch (IOException ioe) {
            System.out.println(ioe);
            return null;
        } catch (ParserConfigurationException pce) {
            System.out.println(pce);
            return null;
        }
    }
}