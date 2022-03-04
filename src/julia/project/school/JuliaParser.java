/*
 * Class:       CS 4308 Section n
 * Term:        Fall 2021
 * Name:        Surafel Assefa
 * Instructor:  Sharon Perry
 * Project:     Deliverable 1 Scanner
 */

package julia.project.school;

import java.util.*;

/*
 * The ethos of the parser is that an explicit tree structure is not required to parse. Since the token type and order
 * is what's important, traversing a linear allocation of tokens is the most effective to implement syntax checking.
 */

public class JuliaParser
{
    private int _index;
    private List<LexicalUnit> _lexicalUnitList;
    private HashMap<String, IdentifierDescriptor> _symbolMap;
    private HashSet<String> _idenifierInitializedSet;

    // TODO: Check if identifier exists at the same time its being used
    // TODO: Move parsing IDs to the arithmetic parsing

    /*
     * Called when parser encounters a syntax error. Prints an error message and terminates the program.
     *
     * @param   message             Error message printed to console
     * @param   lineNumber          Line number the syntax error is located
     */
    private void fatalError(String message, int lineNumber)
    {
        System.out.print(message + "\t" + "Line: " + lineNumber);

        System.exit(-1);
    }

    /*
     * Gets a lexical unit from the lexical unit list.
     *
     * @return LexicalUnit object at the index, or null if the index is out of bounds
     */
    private LexicalUnit getLexicalUnit(int index)
    {
        if (index < 0 || index >= _lexicalUnitList.size())
            return null;

        return _lexicalUnitList.get(index);
    }

    /*
     * Determines whether the block the _index is inside is terminated with an 'end' statement
     * 
     * @return True if the block _index is inside terminates with an 'end' statement, false otherwise.
     */
    private boolean isBlock()
    {
        int subBlocks = 0;

        for (int i = _index; i < _lexicalUnitList.size(); i++) {
            if (getLexicalUnit(i).getTokenEnum() == TokenEnum.END) {
                if (subBlocks > 0) {
                    subBlocks--;
                } else {
                    return true;
                }
            } else if (getLexicalUnit(i).getTokenEnum() == TokenEnum.WHILE || getLexicalUnit(i).getTokenEnum() == TokenEnum.IF) {
                subBlocks++;
            }
        }

        if (_index == _lexicalUnitList.size() - 1) {
            return false;
        }

        return true;
    }

    /*
     * Ensures that the syntax of the program grammar is correct.
     * 
     * Terminates the program if the syntax is incorrect.
     */
    private void parseProgram()
    {
        if (_lexicalUnitList.get(_index).getTokenEnum() != TokenEnum.FUNCTION) {
            fatalError("Expected \"function\" keyword at beginning of program",
                    _lexicalUnitList.get(0).getLineNum());
        }

        _index++;
        if (_lexicalUnitList.get(_index).getTokenEnum() != TokenEnum.IDENTIFIER) {
            fatalError("Expected identifier after \"function\" keyword",
                    _lexicalUnitList.get(0).getLineNum());
        }

        _index++;
        if (_lexicalUnitList.get(_index).getTokenEnum() != TokenEnum.LEFT_PAREN) {
            fatalError("Expected left parenthesis after function identifier \"" + _lexicalUnitList.get(1).getLexeme() + "\"",
                    _lexicalUnitList.get(2).getLineNum());
        }

        _index++;
        if (_lexicalUnitList.get(_index).getTokenEnum() != TokenEnum.RIGHT_PAREN) {
            fatalError("Expected right parenthesis after function identifier \"" + _lexicalUnitList.get(1).getLexeme() + "\"",
                    _lexicalUnitList.get(3).getLineNum());
        }

        _index++;

        if (!isBlock()) {
            fatalError("Expected \"end\" keyword at end of program",
                    _lexicalUnitList.get(_lexicalUnitList.size() - 1).getLineNum());
        }
    }

    /*
     * Ensures that the syntax of the arithmetic expression grammar is correct.
     *
     * Terminates the program if the syntax is incorrect.
     */
    private void parseArithmeticExpression()
    {
        if (getLexicalUnit(_index).getTokenEnum() != TokenEnum.IDENTIFIER || getLexicalUnit(_index).getTokenEnum() != TokenEnum.INTEGER_LIT) {
            fatalError("Unexpected symbol in arithmatic expression", getLexicalUnit(_index).getLineNum());
        }

        if (getLexicalUnit(_index + 2).getTokenEnum() != TokenEnum.IDENTIFIER || getLexicalUnit(_index + 2).getTokenEnum() != TokenEnum.INTEGER_LIT) {
            fatalError("Unexpected symbol in arithmatic expression", getLexicalUnit(_index).getLineNum());
        }

        _index += 3;
    }

    /*
     * Ensures that the syntax of the assignment grammar is correct.
     *
     * Terminates the program if the syntax is incorrect.
     */
    private void parseAssignmentStatement(LexicalUnit identifier)
    {
        if (getLexicalUnit(_index) == null) {
            fatalError("Expected expression after identifier \"" + identifier.getLexeme() + "\"", identifier.getLineNum());
        }

        /*
         * Assignment statement parsing would also check whether the identifier has been instatiated yet.
         *
         * First it determines whether the identifier is a function or a variable. If the idenfitier is a function,
         * parsing fails. Otherwise, parseAssignmentStatement checks if the function
         *
         */

        LexicalUnit operand = getLexicalUnit(_index);

        if (operand != null && (operand.getTokenEnum() == TokenEnum.ADD_OP || operand.getTokenEnum() == TokenEnum.SUB_OP ||
                    operand.getTokenEnum() == TokenEnum.MUL_OP || operand.getTokenEnum() == TokenEnum.DIV_OP)) {

            parseArithmeticExpression();
       } else if (getLexicalUnit(_index).getTokenEnum() == TokenEnum.INTEGER_LIT) {
            _index++;
        } else {
            fatalError("Unexpected symbol in assignment statement", identifier.getLineNum());
        }

    }

    /*
     * Ensures that the syntax of the updating statement grammar is correct.
     *
     * Terminates the program if the syntax is incorrect.
     */
    private void parseUpdatingStatement(LexicalUnit identifier)
    {
        if (!(getLexicalUnit(_index).getTokenEnum() != TokenEnum.INTEGER_LIT || getLexicalUnit(_index).getTokenEnum() != TokenEnum.IDENTIFIER)) {
            fatalError("Unexpected symbol in updating statement", identifier.getLineNum());
        }

        _index++;
    }

    /*
     * Determines how to further parse a statement that begins with an identifier grammar.
     *
     * Terminates the program if the syntax is incorrect.
     */
    private void parseIdentifierStatement()
    {
        LexicalUnit identifier = getLexicalUnit(_index);
        LexicalUnit operator = getLexicalUnit(_index + 1);

        if (operator == null) {
            fatalError("Expected operator after identifier \"" + identifier.getLexeme() + "\"", identifier.getLineNum());
        }

        _index += 2;

        if (operator.getTokenEnum() == TokenEnum.ASSIGNMENT_OP)
        {
            parseAssignmentStatement(identifier);
        }  else if (operator.getTokenEnum() == TokenEnum.ADDASSIGN_OP || operator.getTokenEnum() == TokenEnum.SUBASSIGN_OP ||
            operator.getTokenEnum() == TokenEnum.MULASSIGN_OP || operator.getTokenEnum() == TokenEnum.DIVASSIGN_OP)
        {
            parseUpdatingStatement(identifier);
        } else
        {
            fatalError("Unexpected symbol after identifier \"" + identifier.getLexeme() + "\"", identifier.getLineNum());
        }
    }

    /*
     * Ensures that the syntax of the boolean expression grammar is correct.
     *
     * Terminates the program if the syntax is incorrect.
     */
    private void parseBooleanExpression()
    {
        LexicalUnit relativeOp = getLexicalUnit(_index);

        if (!(relativeOp.getTokenEnum() == TokenEnum.EQUALTO_OP ||
                relativeOp.getTokenEnum() == TokenEnum.NOTEQUAL_OP ||
                relativeOp.getTokenEnum() == TokenEnum.LESSTHAN_OP ||
                relativeOp.getTokenEnum() == TokenEnum.LESSEQUAL_OP ||
                relativeOp.getTokenEnum() == TokenEnum.GREATERTHAN_OP ||
                relativeOp.getTokenEnum() == TokenEnum.GREATEREQUAL_OP)) {
            fatalError("Unexpected symbol in boolean expression", relativeOp.getLineNum());
        }

        _index++;
        if (!(getLexicalUnit(_index).getTokenEnum() == TokenEnum.INTEGER_LIT || getLexicalUnit(_index).getTokenEnum() == TokenEnum.IDENTIFIER)) {
            fatalError("Unexpected symbol in boolean expression", getLexicalUnit(_index + 1).getLineNum());
        }

        _index++;
        if (!(getLexicalUnit(_index).getTokenEnum() == TokenEnum.INTEGER_LIT || getLexicalUnit(_index).getTokenEnum() == TokenEnum.IDENTIFIER)) {
            fatalError("Unexpected symbol in boolean expression", getLexicalUnit(_index + 2).getLineNum());
        }

        _index++;
    }

    /*
     * Ensures that the else terminal keyword is predicated by an 'if' statement.
     *
     * Terminates the program if the syntax is incorrect.
     */
    private void parseElseStatement()
    {
        for (int i = _index; i > 4; i--) {
            if (getLexicalUnit(i).getTokenEnum() == TokenEnum.IF) {
                return;
            }
        }

        _index++;

        fatalError("Expected if statement before \"else\" symbol.", getLexicalUnit(_index).getLineNum());
    }
    
    /*
     * Ensures that the syntax of the if statement grammar is correct.
     *
     * Terminates the program if the syntax is incorrect.
     */
    private void parseIfStatement()
    {
        _index++;

        parseBooleanExpression();

        if (getLexicalUnit(_index).getTokenEnum() != TokenEnum.THEN) {
            fatalError("Expected \"then\" token after if statement boolean expression", getLexicalUnit(_index).getLineNum());
        }

        _index++;

        if (!isBlock()) {
            fatalError("Expected \"end\" after if statement block", getLexicalUnit(_index).getLineNum());
        }
    }

    /*
     * Ensures that the syntax of the while statement grammar is correct.
     *
     * Terminates the program if the syntax is incorrect.
     */
    private void parseWhileStatement()
    {
        _index++;

        parseBooleanExpression();

        if (getLexicalUnit(_index).getTokenEnum() != TokenEnum.DO) {
            fatalError("Expected \"then\" token after if statement boolean expression", getLexicalUnit(_index).getLineNum());
        }

        _index++;

        if (!isBlock()) {
            fatalError("Expected \"end\" after if statement block", getLexicalUnit(_index).getLineNum());
        }
    }

    /*
     * Ensures that the syntax of the print statement grammar is correct.
     *
     * Terminates the program if the syntax is incorrect.
     */
    private void parsePrintStatement()
    {
        _index++;
        if (getLexicalUnit(_index).getTokenEnum() != TokenEnum.LEFT_PAREN) {
            fatalError("Unexpected symbol after print statement", getLexicalUnit(_index).getLineNum());
        }

        _index++;
        if (!(getLexicalUnit(_index).getTokenEnum() == TokenEnum.INTEGER_LIT || getLexicalUnit(_index).getTokenEnum() == TokenEnum.IDENTIFIER)) {
            fatalError("Unexpected symbol in print statement", getLexicalUnit(_index).getLineNum());
        }

        _index++;
        if (getLexicalUnit(_index).getTokenEnum() != TokenEnum.RIGHT_PAREN) {
            fatalError("Unexpected symbol after print statement", getLexicalUnit(_index).getLineNum());
        }

        _index++;
    }

    /*
     * Main function where functions parsing grammar non-terminals are called to parse a statement based on the first
     * terminal encountered in the statement.
     *
     * * Terminates the program if the syntax is incorrect.
     */
    private void checkStatement()
    {

        LexicalUnit lexicalUnit = getLexicalUnit(_index);

        if (lexicalUnit.getTokenEnum() == TokenEnum.IDENTIFIER) {
            parseIdentifierStatement();
        } else if (lexicalUnit.getTokenEnum() == TokenEnum.IF) {
            parseIfStatement();
        } else if (lexicalUnit.getTokenEnum() == TokenEnum.ELSE) {

        } else if (lexicalUnit.getTokenEnum() == TokenEnum.WHILE) {
            parseWhileStatement();
        } else if (lexicalUnit.getTokenEnum() == TokenEnum.PRINT) {
            parsePrintStatement();
        } else if (lexicalUnit.getTokenEnum() == TokenEnum.END) {
            _index++;
        } else {
            fatalError("Unknown symbol \"" + lexicalUnit.getLexeme() + "\"", lexicalUnit.getLineNum());
        }
    }

    public JuliaParser(List<LexicalUnit> lexicalUnitList, HashMap<String, IdentifierDescriptor> symbolMap)
    {
        _index = 0;
        _symbolMap = symbolMap;

        _lexicalUnitList = new ArrayList<>(lexicalUnitList);

        _idenifierInitializedSet = new HashSet<String>();
    }

    /*
     * Parsing function called by the calling program.
     *
     * First determines whether the program grammar syntax of the code is correct. Then continuously calls
     * checkStatement() until there are no more tokens to process.
     */
    public void parse()
    {
        parseProgram();

        while (_index < _lexicalUnitList.size()) {
            checkStatement();
        }
    }
}
