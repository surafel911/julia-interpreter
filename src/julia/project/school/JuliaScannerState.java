/*
 * Class:       CS 4308 Section n
 * Term:        Fall 2021
 * Name:        Surafel Assefa
 * Instructor:  Sharon Perry
 * Project:     Deliverable 1 Scanner
 */

package julia.project.school;

import java.io.*;

/*
 * Class to manage the state of the Julia scanner.
 */
public class JuliaScannerState
{
    private PushbackReader _pushBackReader;
    private int _currentRow, _currentColumn;

    /*
     * Opens Julia source and creates PushbackReader.
     *
     * @param   file    Julia source file
     * @return  PushbackReader of source file.
     */
    private PushbackReader openSource(File file)
    {
        PushbackReader reader = null;

        try {
            reader = new PushbackReader(new BufferedReader(new FileReader(file)));
            reader.ready();
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("ERROR: Cannot find file.");
            System.exit(-1);
        } catch (IOException ioException) {
            System.out.println("ERROR: File not ready.");
            System.exit(-1);
        } catch (Exception exception) {
            System.out.println("ERROR: Failed to open file.");
            System.exit(-1);
        }

        return reader;
    }

    /*
     * Constructor of class to manage the state of the Julia scanner.
     *
     * @param   lexeme              Lexeme of the identifier
     * @param   identifierEnum      Type of identifier
     * @param   identifierValue     Value of the identifier
     */
    public JuliaScannerState(File file)
    {
        _currentRow = 0;
        _currentColumn = 0;
        _pushBackReader = openSource(file);
    }

    public int getCurrentRow() { return _currentRow; }
    public int getCurrentColumn() { return _currentColumn; }
    public PushbackReader getPushBackReader() { return _pushBackReader; }

    public void setCurrentRow(int currentRow) { _currentRow = currentRow; }
    public void setCurrentColumn(int currentColumn) { _currentColumn = currentColumn; }
}