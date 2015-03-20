
package br.edu.ufcg.wumpusia.main;

import br.edu.ufcg.wumpusia.ia.WumpusIAArriscado;
import br.edu.ufcg.wumpusia.ia.WumpusIASafeFirst;
import br.edu.ufcg.wumpusia.utils.Utils;

import java.io.IOException;

public class Main {

    public static void main(final String args[]) throws IOException {
        Long[] seeds = Utils.loadSeeds();
        
        for (int i = 0; i < 1000; i++) {
            System.out.println("Creating Wumpus with seed: " + seeds[i]);

            (new WumpusIASafeFirst(seeds[i])).run();
            (new WumpusIAArriscado(seeds[i])).run();
        }

        Utils.saveWords();
    }
}
