/*
 * Class:       CS 4308 Section n
 * Term:        Fall 2021
 * Name:        Surafel Assefa
 * Instructor:  Sharon Perry
 * Project:     Deliverable 1 Scanner
 */

package julia.project.school;

public enum TokenEnum
{
    NULL,

    // Variable-name tokens
    INTEGER_LIT,
    IDENTIFIER,

    // Constant tokens
    LEFT_PAREN,
    RIGHT_PAREN,
    ASSIGNMENT_OP,

    // Comparsion tokens
    LESSTHAN_OP,
    LESSEQUAL_OP,
    GREATERTHAN_OP,
    GREATEREQUAL_OP,
    EQUALTO_OP,
    NOTEQUAL_OP,

    // Math tokens
    ADD_OP,
    ADDASSIGN_OP,
    SUB_OP,
    SUBASSIGN_OP,
    MUL_OP,
    MULASSIGN_OP,
    DIV_OP,
    DIVASSIGN_OP,

    // Keywords
    FUNCTION,
    END,
    PRINT,

    IF,
    THEN,
    ELSE,

    WHILE,
    DO,

    REPEAT,
    UNTIL;

    int getOpCode()
    {
        return this.ordinal();
    }
}
