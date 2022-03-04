/*
 * Class:       CS 4308 Section n
 * Term:        Fall 2021
 * Name:        Surafel Assefa
 * Instructor:  Sharon Perry
 * Project:     Deliverable 1 Scanner
 */

package julia.project.school;

import java.io.*;
import java.util.*;
import java.util.List;

/*
 * Class implementing the scanning, lexing, and semantic of Julia source files
 */
public class JuliaScanner
{
    /*
     * Called when scanner encounters and lexing error. Prints an error message and terminates the program.
     *
     * @param   juliaScannerState   Current state of the julia scanner
     * @param   message             Error message printed to console
     */
    private void fatalError(JuliaScannerState juliaScannerState, String message)
    {
        System.out.print(message + "\t"
                + "Row: " + juliaScannerState.getCurrentRow()
                + " Col: " + juliaScannerState.getCurrentColumn());

        System.exit(-1);
    }

    /*
     * Gets next character from reader.
     *
     * @param   juliaScannerState   Current state of the julia scanner
     * @return  Next character of input stream, -1 at EoF.
     */
    private int getNextChar(JuliaScannerState juliaScannerState)
    {
        int c = -1;

        try {
            c = juliaScannerState.getPushBackReader().read();
        } catch (IOException ioException) {
            fatalError(juliaScannerState,
                    "ERROR: Failed to read character from reader.");
        }

        return c;
    }

    /*
     * Peeks at the next character. Marker guaranteed to be at the same point after the call as before.
     *
     * @param   juliaScannerState   Current state of the julia scanner
     * @return  Character after marker in input stream, -1 at EoF.
     */
    public int peekNextChar(JuliaScannerState juliaScannerState)
    {
        int c = -1;

        try {
            c = juliaScannerState.getPushBackReader().read();
            juliaScannerState.getPushBackReader().unread(c);
        } catch (Exception e) {
            fatalError(juliaScannerState,
                    "ERROR: Failed to peek character from reader.");
        }

        return c;
    }

    /*
     * Checks if the character is valid.
     *
     * @param   c Character to validate.
     * @return  True if character is not EoF (-1) or outside UTF-8 character range.
     */
    public boolean isValidChar(int c)
    {
        if (c == -1 || c >= 127)
            return false;

        return true;
    }

    /*
     * Finds the next non-whitespace character in input stream. Skips comment lines.
     *
     * @param   juliaScannerState   Current state of the julia scanner
     * @return  Character after whitespace, or first character in next line if comment is encountered.
     */
    public int skipWhitespace(JuliaScannerState juliaScannerState)
    {
        int c = 0, tmp = 0;

        do {
            c = getNextChar(juliaScannerState);

            if ((char)c == '/' && peekNextChar(juliaScannerState) == '/') {
                while (isValidChar(c) && peekNextChar(juliaScannerState) != '\n') {
                    c = getNextChar(juliaScannerState);
                }

                c = getNextChar(juliaScannerState);
            }

            if (!Character.isWhitespace(c)) {
                break;
            }

            tmp = juliaScannerState.getCurrentRow();

            if ((char)c == '\n') {
                juliaScannerState.setCurrentRow(++tmp);
                juliaScannerState.setCurrentColumn(0);
            } else {
                juliaScannerState.setCurrentColumn(++tmp);
            }
        } while (isValidChar(c));

        return c;
    }

    /*
     * Check if lexeme is an integer
     *
     * @param   lexeme  Lexeme to check integer status.
     * @return  True if the lexeme is an integer, false otherwise.
     */
    private boolean isInteger(String lexeme)
    {
        try {
            Integer.parseInt(lexeme);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /*
     * Check if lexeme is valid. Namely checks if lexeme is a valid integer or identifier.
     *
     * @param   lexeme  Lexeme to check validity.
     * @param   juliaScannerState   Current state of the julia scanner
     * @return  True if the lexeme meets convention, false otherwise.
     */
    private boolean isValidLexeme(String lexeme, JuliaScannerState juliaScannerState)
    {
        if (Character.isDigit(lexeme.charAt(0)) && !isInteger(lexeme)) {
            return false;
        }

        if (Character.isAlphabetic(lexeme.charAt(0))) {
            if (Character.isDigit(peekNextChar(juliaScannerState))) {
                return false;
            }
        }

        return true;
    }

    /*
     * Constructs the next lexeme in input stream. Adds characters to lexeme until whitespace is encountered.
     *
     * @param   juliaScannerState   Current state of the julia scanner.
     * @return  String of the next lexeme, null if EoF.
     */
    public String buildNextLexeme(JuliaScannerState juliaScannerState)
    {
        int c = 0;
        String lexeme = new String();

        if (!isValidChar(c = skipWhitespace(juliaScannerState))) {
            return null;
        }

        while (isValidChar(peekNextChar(juliaScannerState)) && peekNextChar(juliaScannerState) != ' ' && peekNextChar(juliaScannerState) != '\n') {
            lexeme += (char)c;
            c = getNextChar(juliaScannerState);
        }

        lexeme += (char)c;
        if (!isValidLexeme(lexeme, juliaScannerState)) {
            fatalError(juliaScannerState, "ERROR: Invalid lexeme.");
        }

        return lexeme;
    }

    /*
     * Deconstructs a lexeme into derivative lexes for further analysis.
     *
     * @param   lexeme  Lexeme to derive.
     * @return  String array of every character in lexeme.
     */
    private String[] parseLexeme(String lexeme)
    {
        int i = 0;
        List<String> strings = new ArrayList<>();

        if (Character.isAlphabetic(lexeme.charAt(i)) && lexeme.contains("(")) {
            int end = lexeme.indexOf("(");
            strings.add(lexeme.substring(i, end));
            i = end;
        }

        for (; i < lexeme.length(); i++) {
            strings.add(String.valueOf(lexeme.charAt(i)));
        }

        return strings.toArray(new String[strings.size()]);
    }

    /*
     * Iterates through list of lexical units and finds all identifiers.
     *
     * @param   lexicalUnitList  List of lexical units to find identifiers from.
     * @return  List of IdentifierDescriptor objects of all identifiers in lexicalUnitList.
     */
    private List<IdentifierDescriptor> findIdentifiers(List<LexicalUnit> lexicalUnitList)
    {
        int integer = 0, functionCounter = 0;
        List<IdentifierDescriptor> identifierDescriptorList = new ArrayList<>();

        for (int i = 0; i < lexicalUnitList.size() - 2; i++) {
            if (lexicalUnitList.get(i).getTokenEnum() == TokenEnum.IDENTIFIER) {
                if (lexicalUnitList.get(i - 1).getTokenEnum() == TokenEnum.FUNCTION) {
                    identifierDescriptorList.add(new IdentifierDescriptor(
                            lexicalUnitList.get(i).getLexeme(),
                            IdentifierType.FUNCTION,
                            functionCounter++));
                }

                if (lexicalUnitList.get(i + 1).getTokenEnum() == TokenEnum.ASSIGNMENT_OP) {
                    identifierDescriptorList.add(new IdentifierDescriptor(
                            lexicalUnitList.get(i).getLexeme(),
                            IdentifierType.INTEGER,
                            Integer.parseInt(lexicalUnitList.get(i + 2)
                                    .getLexeme())));
                }
            }
        }

        return identifierDescriptorList;
    }

    /*
     * Finds all LexicalUnits and IdentifierDescriptors from Julia source file
     *
     * @param   file  Julia source file.
     * @return  JuliaScannerResults object that contains list of LexicalUnits and IdentifierDescriptors from source file.
     */
    public JuliaScannerResults lexSource(File file)
    {
        JuliaScannerResults juliaScannerResults;
        HashMap<String, IdentifierDescriptor> symbolMap;

        List<LexicalUnit> lexicalUnitList = new ArrayList<>();
        JuliaScannerState juliaScannerState = new JuliaScannerState(file);

        String lexeme = buildNextLexeme(juliaScannerState);

        while (lexeme != null) {
            TokenEnum token = TokenTable.getTokenEnum(lexeme);

            if (token == TokenEnum.NULL) {
                for (String lex : parseLexeme(lexeme)) {
                    if (TokenTable.getTokenEnum(lex) == TokenEnum.NULL) {
                        fatalError(juliaScannerState,
                                "ERROR: Unidentifiable symbol: \"" + lex + "\"");
                    }

                    lexicalUnitList.add(new LexicalUnit(lex, TokenTable.getTokenEnum(lex), juliaScannerState.getCurrentRow()));
                }
            } else {
                lexicalUnitList.add(new LexicalUnit(lexeme, TokenTable.getTokenEnum(lexeme), juliaScannerState.getCurrentRow()));
            }

            lexeme = buildNextLexeme(juliaScannerState);
        }

        symbolMap = new HashMap<>();
        for (IdentifierDescriptor descriptor : findIdentifiers(lexicalUnitList)) {
            symbolMap.put(descriptor.getLexeme(), descriptor);
        }

        juliaScannerResults = new JuliaScannerResults(lexicalUnitList, symbolMap);

        return juliaScannerResults;
    }
}
