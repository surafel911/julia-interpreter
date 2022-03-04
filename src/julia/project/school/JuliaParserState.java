package julia.project.school;

import java.util.*;

public class JuliaParserState {
    private int _index;
    private List<LexicalUnit> _lexicalUnitList;
    private HashMap<String, IdentifierDescriptor> _symbolMap;
    private HashMap<String, Boolean> _instantiatedMap;

    public JuliaParserState(List<LexicalUnit> lexicalUnitList, HashMap<String, IdentifierDescriptor> symbolMap)
    {
        _index = 0;
        _symbolMap = symbolMap;
        _lexicalUnitList = new ArrayList<>(lexicalUnitList);
    }

    public int getIndex() { return _index; }
    public List<LexicalUnit> getLexicalUnitList() { return _lexicalUnitList; }
    public HashMap<String, IdentifierDescriptor> getSymbolMap() { return _symbolMap; }
    public HashMap<String, Boolean> getInstantiatedMap() { return _instantiatedMap; }
}
