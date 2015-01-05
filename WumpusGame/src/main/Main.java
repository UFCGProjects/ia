package main;

import java.util.Scanner;

import core.Wumpus;

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
            }

        }

    }

}
