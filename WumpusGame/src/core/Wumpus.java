package core;

import java.util.ArrayList;

import models.Cell;
import models.Hazard;
import utils.Utils;

public class Wumpus {

    private final int HEIGHT_SIZE = 5;
    private final int WIDTH_SIZE = 5;

    Cell[][] mMatrix;
    Cell mCurrentCell;

    public Wumpus() {

        mMatrix = new Cell[HEIGHT_SIZE][WIDTH_SIZE];

        for (int i = 0; i < mMatrix.length; i++) {
            for (int j = 0; j < mMatrix[i].length; j++) {
                mMatrix[i][j] = new Cell(i, j);
            }
        }

        Cell cell = null;

        // WUMPUS POSITION
        cell = mMatrix[Utils.randInt(0, HEIGHT_SIZE - 1)][Utils.randInt(0, WIDTH_SIZE - 1)];
        cell.setHazard(Hazard.WUMPUS);

        for (final Cell c : getNeighbors(cell)) {
            c.setSmellWumpus(true);
        }

        // COLONY OF BATS POSITION
        do {
            cell = mMatrix[Utils.randInt(0, HEIGHT_SIZE - 1)][Utils.randInt(0, WIDTH_SIZE - 1)];
        } while (!cell.getHazard().equals(Hazard.NONE));

        cell.setHazard(Hazard.COLONY_BATS);

        for (final Cell c : getNeighbors(cell)) {
            c.setHearFlapping(true);
        }

        // BOTTOMLESS POSITION
        do {
            cell = mMatrix[Utils.randInt(0, HEIGHT_SIZE - 1)][Utils.randInt(0, WIDTH_SIZE - 1)];
        } while (!cell.getHazard().equals(Hazard.NONE));

        cell.setHazard(Hazard.BOTTOMLESS);

        for (final Cell c : getNeighbors(cell)) {
            c.setFeelBreeze(true);
        }

        // START PLAYER POSITION
        do {
            cell = mMatrix[Utils.randInt(0, HEIGHT_SIZE - 1)][Utils.randInt(0, WIDTH_SIZE - 1)];
        } while (!cell.getHazard().equals(Hazard.NONE));

        mCurrentCell = cell;
        mCurrentCell.setVisited(true);

    }

    private ArrayList<Cell> getNeighbors(final Cell cell) {
        final ArrayList<Cell> neighbors = new ArrayList<Cell>();

        // SIDES

        if (cell.getPositionX() > 0) {
            neighbors.add(mMatrix[cell.getPositionY()][cell.getPositionX() - 1]);
        }

        if (cell.getPositionX() < WIDTH_SIZE - 1) {
            neighbors.add(mMatrix[cell.getPositionY()][cell.getPositionX() + 1]);
        }

        if (cell.getPositionY() > 0) {
            neighbors.add(mMatrix[cell.getPositionY() - 1][cell.getPositionX()]);
        }

        if (cell.getPositionY() < HEIGHT_SIZE - 1) {
            neighbors.add(mMatrix[cell.getPositionY() + 1][cell.getPositionX()]);
        }

        // EDGES

        if (cell.getPositionX() > 0 && cell.getPositionY() > 0) {
            neighbors.add(mMatrix[cell.getPositionY() - 1][cell.getPositionX() - 1]);
        }

        if (cell.getPositionX() < WIDTH_SIZE - 1 && cell.getPositionY() > 0) {
            neighbors.add(mMatrix[cell.getPositionY() - 1][cell.getPositionX() + 1]);
        }

        if (cell.getPositionX() > 0 && cell.getPositionY() < HEIGHT_SIZE - 1) {
            neighbors.add(mMatrix[cell.getPositionY() + 1][cell.getPositionX() - 1]);
        }

        if (cell.getPositionX() < WIDTH_SIZE - 1 && cell.getPositionY() < HEIGHT_SIZE - 1) {
            neighbors.add(mMatrix[cell.getPositionY() + 1][cell.getPositionX() + 1]);
        }

        return neighbors;
    }

    public void moveUp() {
        if (mCurrentCell.getPositionY() > 0) {
            mCurrentCell = mMatrix[mCurrentCell.getPositionY() - 1][mCurrentCell.getPositionX()];
            mCurrentCell.setVisited(true);
        }

        checkBats();
    }

    public void moveDown() {
        if (mCurrentCell.getPositionY() < HEIGHT_SIZE - 1) {
            mCurrentCell = mMatrix[mCurrentCell.getPositionY() + 1][mCurrentCell.getPositionX()];
            mCurrentCell.setVisited(true);
        }

        checkBats();
    }

    public void moveLeft() {
        if (mCurrentCell.getPositionX() > 0) {
            mCurrentCell = mMatrix[mCurrentCell.getPositionY()][mCurrentCell.getPositionX() - 1];
            mCurrentCell.setVisited(true);
        }

        checkBats();
    }

    public void moveRight() {
        if (mCurrentCell.getPositionX() < WIDTH_SIZE - 1) {
            mCurrentCell = mMatrix[mCurrentCell.getPositionY()][mCurrentCell.getPositionX() + 1];
            mCurrentCell.setVisited(true);
        }

        checkBats();
    }

    public void checkBats() {
        if (mCurrentCell.getHazard().equals(Hazard.COLONY_BATS)) {
            System.out.println("Bats carried you away!\n");
            mCurrentCell = mMatrix[Utils.randInt(0, HEIGHT_SIZE - 1)][Utils.randInt(0, WIDTH_SIZE - 1)];
            mCurrentCell.setVisited(true);
        }
    }

    @Override
    public String toString() {
        String out = "";

        for (int i = 0; i < mMatrix.length; i++) {
            for (int j = 0; j < mMatrix[i].length; j++) {
                if (mMatrix[i][j].equals(mCurrentCell)) {
                    out += "P" + " ";
                } else {
                    out += mMatrix[i][j].toString() + " ";
                }
            }

            out += "\n";
        }

        if (mCurrentCell.isFeelBreeze()) {
            out += "You feel a breeze\n";
        }

        if (mCurrentCell.isHearFlapping()) {
            out += "You hear flapping.\n";
        }

        if (mCurrentCell.isSmellWumpus()) {
            out += "You smell a wumpus.\n";
        }

        out += "\n";

        return out;
    }

    public boolean isGameOver() {
        switch (mCurrentCell.getHazard()) {
        case BOTTOMLESS:
            System.out.println(toString());
            System.out.println("You fell down a pit!\nYou are dead!\n");
            return true;
        case WUMPUS:
            System.out.println(toString());
            System.out.println("You have been eaten by a wumpus!\nYou are dead!\n");
            return true;
        default:
            return false;
        }

    }

}
