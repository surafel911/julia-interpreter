/*
 * Class:       CS 4308 Section n
 * Term:        Fall 2021
 * Name:        Surafel Assefa
 * Instructor:  Sharon Perry
 * Project:     Deliverable 1 Scanner
 */

package julia.project.school;

/*
 * Basic lexical unit.
 */
public class LexicalUnit
{
    private String _lexeme;
    private TokenEnum _token;
    private int _lineNum;

    /*
     * Constructor of Lexical unit.
     *
     * @param   lexeme  String of lexeme
     * @param   token   TokenEnum of lexeme
     */
    public LexicalUnit(String lexeme, TokenEnum token, int lineNum)
    {
        _lexeme = lexeme;
        _token = token;
        _lineNum = lineNum;
    }

    public String getLexeme()
    {
        return _lexeme;
    }
    public TokenEnum getTokenEnum()
    {
        return _token;
    }
    public int getLineNum() { return _lineNum; }
}
