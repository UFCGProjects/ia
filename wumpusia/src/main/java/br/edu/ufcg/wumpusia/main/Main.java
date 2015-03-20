
package br.edu.ufcg.wumpusia.main;

import br.edu.ufcg.wumpusia.ia.WumpusIASimple;
import br.edu.ufcg.wumpusia.utils.Utils;

import java.io.IOException;

public class Main {

    public static void main(final String args[]) throws IOException {

        for (int i = 0; i < 1000; i++) {
            WumpusIASimple gameIA = new WumpusIASimple();
            gameIA.run();
        }

        Utils.saveWords();
    }
}
