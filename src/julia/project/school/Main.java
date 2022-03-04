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

public class Main {
    private static void lexFile(String path)
    {
        File file = null;

        System.out.println(path);

        try {
            JuliaScanner scanner = new JuliaScanner();

            JuliaScannerResults juliaScannerResults = scanner.lexSource(new File(path));

            JuliaParser parser = new JuliaParser(juliaScannerResults.getLexicalUnitList(), juliaScannerResults.getSymbolMap());

            parser.parse();

            System.out.println("\n");

        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("\n");
    }

    public static void main(String[] args) {
        lexFile("docs/Julia-Files/Test2.jl");

    }
}
