/*
 * Class:       CS 4308 Section n
 * Term:        Fall 2021
 * Name:        Surafel Assefa
 * Instructor:  Sharon Perry
 * Project:     Deliverable 1 Scanner
 */

package julia.project.school;

import javax.management.loading.MLetMBean;
import java.awt.color.ICC_Profile;
import java.util.*;

/*
 * Table of all tokens in Julia grammar.
 */
public final class TokenTable
{
    private static Hashtable<String, TokenEnum> _tokenHashTable = null;

    /*
     * Check if lexeme is an integer
     *
     * @param   lexeme  Lexeme to check integer status.
     * @return  True if the lexeme is an integer, false otherwise.
     */
    private static boolean isInteger(String s)
    {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /*
     * Returns static token hash table, creates it if it is null.
     *
     * @return  Returns token hash table.
     */
    private static Hashtable<String, TokenEnum> getTokenHashTable()
    {
        if (_tokenHashTable == null) {
            _tokenHashTable = new Hashtable<>();

            _tokenHashTable.put("(", TokenEnum.LEFT_PAREN);
            _tokenHashTable.put(")", TokenEnum.RIGHT_PAREN);
            _tokenHashTable.put("=", TokenEnum.ASSIGNMENT_OP);
            _tokenHashTable.put("<", TokenEnum.LESSTHAN_OP);
            _tokenHashTable.put("<=", TokenEnum.LESSEQUAL_OP);
            _tokenHashTable.put(">", TokenEnum.GREATERTHAN_OP);
            _tokenHashTable.put(">=", TokenEnum.GREATEREQUAL_OP);
            _tokenHashTable.put("==", TokenEnum.EQUALTO_OP);
            _tokenHashTable.put("~=", TokenEnum.NOTEQUAL_OP);
            _tokenHashTable.put("+", TokenEnum.ADD_OP);
            _tokenHashTable.put("+=", TokenEnum.ADDASSIGN_OP);
            _tokenHashTable.put("-", TokenEnum.SUB_OP);
            _tokenHashTable.put("-=", TokenEnum.SUBASSIGN_OP);
            _tokenHashTable.put("*", TokenEnum.MUL_OP);
            _tokenHashTable.put("*=", TokenEnum.MULASSIGN_OP);
            _tokenHashTable.put("/", TokenEnum.DIV_OP);
            _tokenHashTable.put("/=", TokenEnum.DIVASSIGN_OP);
            _tokenHashTable.put("function", TokenEnum.FUNCTION);
            _tokenHashTable.put("end", TokenEnum.END);
            _tokenHashTable.put("print", TokenEnum.PRINT);
            _tokenHashTable.put("if", TokenEnum.IF);
            _tokenHashTable.put("then", TokenEnum.THEN);
            _tokenHashTable.put("else", TokenEnum.ELSE);
            _tokenHashTable.put("while", TokenEnum.WHILE);
            _tokenHashTable.put("do", TokenEnum.DO);
            _tokenHashTable.put("repeat", TokenEnum.REPEAT);
            _tokenHashTable.put("until", TokenEnum.UNTIL);
        }

        return _tokenHashTable;
    }

    /*
     * Private constructor of TokenTable to create a "semi-static" class in Java.
     */
    private TokenTable()
    {
    }

    /*
     * Finds and returns a TokenEnum of a given lexeme.
     *
     * @param   lexeme  Lexeme to find token for..
     * @return  TokenEnum of given lexeme, or TokenEnum.NULL if lexeme is unidentifiable.
     */
    public static TokenEnum getTokenEnum(String lexeme)
    {
        TokenEnum token = TokenEnum.NULL;

        if (lexeme.isBlank())
            return TokenEnum.NULL;

        // Test for identifier and integer conditions

        if (lexeme.length() == 1 && Character.isAlphabetic(lexeme.charAt(0))) {
            return TokenEnum.IDENTIFIER;
        }

        if (Character.isDigit(lexeme.charAt(0)) && isInteger(lexeme)) {
            return TokenEnum.INTEGER_LIT;
        }

        // Test for other token conditions quickly with hash table
        token = getTokenHashTable().get(lexeme);
        if (token == null) {
            return TokenEnum.NULL;
        } else {
            return token;
        }
    }
}
