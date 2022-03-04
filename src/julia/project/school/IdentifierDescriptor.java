/*
 * Class:       CS 4308 Section n
 * Term:        Fall 2021
 * Name:        Surafel Assefa
 * Instructor:  Sharon Perry
 * Project:     Deliverable 1 Scanner
 */

package julia.project.school;

/*
 * Class containing descriptor/information of an identifier for the symbols table. Symbols table implemented as a list of IdentifierDescriptor.
 */
public class IdentifierDescriptor
{
    private String _lexeme;
    private IdentifierType _identifierType;
    private int _identifierValue;

    /*
     * Constructor of identifier descriptor.
     *
     * @param   lexeme              Lexeme of the identifier
     * @param   IdentifierType      Type of identifier
     * @param   identifierValue     Value of the identifier
     */
    public IdentifierDescriptor(String lexeme, IdentifierType identifierType, int identifierValue)
    {
        _lexeme = lexeme;
        _identifierType = identifierType;
        _identifierValue = identifierValue;
    }

    public String getLexeme() { return _lexeme; }
    public IdentifierType getIdentifierType() { return _identifierType; }
    public int getIdentifierValue() { return _identifierValue; }

    public void setIdentifierValue(int identifierValue) { _identifierValue = identifierValue; }
}
