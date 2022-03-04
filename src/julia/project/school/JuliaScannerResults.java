/*
 * Class:       CS 4308 Section n
 * Term:        Fall 2021
 * Name:        Surafel Assefa
 * Instructor:  Sharon Perry
 * Project:     Deliverable 1 Scanner
 */

package julia.project.school;

import java.util.List;
import java.util.HashMap;

/*
 * Results of Julia scanner lexical analysis.
 */
public class JuliaScannerResults
{
    private List<LexicalUnit> _lexicalUnitList;
    private HashMap<String, IdentifierDescriptor> _symbolMap;

    /*
     * Constructor of JuliaScannerResults.
     *
     * @param   lexicalUnitList             List of lexical units
     * @param   identifierDescriptorList    List of identifier descriptors
     */
    public JuliaScannerResults(List<LexicalUnit> lexicalUnitList, HashMap<String, IdentifierDescriptor> symbolMap)
    {
        _lexicalUnitList = lexicalUnitList;
        _symbolMap = symbolMap;
    }

    public List<LexicalUnit> getLexicalUnitList() { return _lexicalUnitList; }
    public HashMap<String, IdentifierDescriptor> getSymbolMap() { return _symbolMap; }
}
