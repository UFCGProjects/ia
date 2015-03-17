package br.edu.ufcg.wumpusgame.main;

import br.edu.ufcg.wumpusgame.core.Wumpus;

import java.util.Scanner;

public class Main {

    public static void main(final String[] args) {
        final Wumpus wumpus = new Wumpus();

        final Scanner sc = new Scanner(System.in);

        while (!wumpus.isGameOver()) {

            System.out.println(wumpus.toString());

            final String in = sc.nextLine();

            if (in.equalsIgnoreCase("w")) {
                wumpus.moveUp();
            } else if (in.equalsIgnoreCase("s")) {
                wumpus.moveDown();
            } else if (in.equalsIgnoreCase("a")) {
                wumpus.moveLeft();
            } else if (in.equalsIgnoreCase("d")) {
                wumpus.moveRight();
            } else if (in.equalsIgnoreCase("aw")) {
                wumpus.attackUp();
            } else if (in.equalsIgnoreCase("aa")) {
                wumpus.attackLeft();
            } else if (in.equalsIgnoreCase("as")) {
                wumpus.attackDown();
            } else if (in.equalsIgnoreCase("ad")) {
                wumpus.attackRight();
            }

        }

        sc.nextLine();
    }

}
