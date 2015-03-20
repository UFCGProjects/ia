
package br.edu.ufcg.wumpusia.main;

import br.edu.ufcg.wumpusia.ia.WumpusIAArriscado;
import br.edu.ufcg.wumpusia.ia.WumpusIASafeFirst;
import br.edu.ufcg.wumpusia.utils.Utils;

import java.io.IOException;

public class Main {

    public static void main(final String args[]) throws IOException {
//        Long[] seeds = Utils.loadSeeds();
        
        for (int i = 0; i < 1; i++) {
            long seed = System.currentTimeMillis();

            System.out.println("Creating Wumpus with seed: " + seed);

            (new WumpusIASafeFirst(seed)).run();
            (new WumpusIAArriscado(seed)).run();
        }

        Utils.saveWords();
    }
}
