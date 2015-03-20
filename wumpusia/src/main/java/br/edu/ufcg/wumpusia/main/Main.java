
package br.edu.ufcg.wumpusia.main;

import br.edu.ufcg.wumpusia.ia.WumpusIAOptimized;
import br.edu.ufcg.wumpusia.ia.WumpusIASimple;
import br.edu.ufcg.wumpusia.utils.Utils;

import java.io.IOException;

public class Main {

    public static void main(final String args[]) throws IOException {
        Long[] seeds = Utils.loadSeeds();


        for (int i = 0; i < 1000; i++) {
            System.out.println("Creating Wumpus with seed: " + seeds[i]);
            WumpusIASimple gameIA = new WumpusIASimple(seeds[i]);
            WumpusIAOptimized gameIAOp = new WumpusIAOptimized(seeds[i]);
            gameIA.run("simples");
            gameIAOp.run("otimizada");
        }

        Utils.saveWords();
    }
}
