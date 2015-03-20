
package br.edu.ufcg.wumpusia.utils;

import java.io.*;
import java.util.ArrayList;

public class Utils {

    final static ArrayList<Result> results = new ArrayList<Result>();

    public final static String styleSheet =
            "node {" +
                    "       fill-color: black;" +
                    "}" +
                    "node.unvisited {" +
                    "       fill-color: red;" +
                    "}" +
                    "node.current {" +
                    "       fill-color: blue;" +
                    "}"
                    +
                    "node.unvisitedsafe {" +
                    "       fill-color: #ccc;" +
                    "}" +
                    "node.visited {" +
                    "       fill-color: green;" +
                    "}";

    private static final String OUT_PUT_FILE = "result.csv";
    private static final String PATH_SEEDS = "seeds.txt";

    public static ArrayList<String> readCSV(final String path) {
        final BufferedReader reader;

        final ArrayList<String> data = new ArrayList<String>();

        try {
            reader = new BufferedReader(new FileReader(path));

            String line;

            while ((line = reader.readLine()) != null) {
                data.add(line);
            }

            reader.close();

        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return data;

    }

    public static void addResultado(String euristica, int qtdeMoves, boolean win, Long seed) {
        Result resultTemp = new Result(euristica, qtdeMoves, win, seed);
        results.add(resultTemp);
    }

    public static void saveWords() throws IOException {
        PrintWriter writerAnalysis;

        writerAnalysis = new PrintWriter(new BufferedWriter(new FileWriter(OUT_PUT_FILE, false)));

        writerAnalysis.println("euristica,moves,win,seed");

        for (Result result : results) {
            writerAnalysis.println(result.toString());
        }

        writerAnalysis.flush();
        writerAnalysis.close();
    }


    public static Long[] loadSeeds() {
        Long[] seeds = new Long[10000];

        final BufferedReader reader;

        int index = 0;

        try {
            reader = new BufferedReader(new FileReader(PATH_SEEDS));

            String line;

            while ((line = reader.readLine()) != null) {
                seeds[index++] = Long.parseLong(line);
            }

            reader.close();

        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return seeds;
    }
}
