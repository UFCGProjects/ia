package org.wumpusgame.core;

import java.util.ArrayList;

import org.wumpusgame.models.Cell;
import org.wumpusgame.models.Hazard;
import org.wumpusgame.utils.Utils;

public class Wumpus {

    private final int HEIGHT_SIZE = 5;
    private final int WIDTH_SIZE = 5;
    private int mArrow = 1;

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

        // GENERATE WUMPUS POSITION
        cell = mMatrix[Utils.randInt(0, HEIGHT_SIZE - 1)][Utils.randInt(0, WIDTH_SIZE - 1)];
        cell.setHazard(Hazard.WUMPUS);

        for (final Cell c : getNeighbors(cell)) {
            c.setSmellWumpus(true);
        }

        // GENERATE COLONY OF BATS POSITION
        do {
            cell = mMatrix[Utils.randInt(0, HEIGHT_SIZE - 1)][Utils.randInt(0, WIDTH_SIZE - 1)];
        } while (!cell.getHazard().equals(Hazard.NONE));

        cell.setHazard(Hazard.COLONY_BATS);

        for (final Cell c : getNeighbors(cell)) {
            c.setHearFlapping(true);
        }

        // GENERATE BOTTOMLESS POSITION
        do {
            cell = mMatrix[Utils.randInt(0, HEIGHT_SIZE - 1)][Utils.randInt(0, WIDTH_SIZE - 1)];
        } while (!cell.getHazard().equals(Hazard.NONE));

        cell.setHazard(Hazard.BOTTOMLESS);

        for (final Cell c : getNeighbors(cell)) {
            c.setFeelBreeze(true);
        }

        // GENERATE START PLAYER POSITION
        do {
            cell = mMatrix[Utils.randInt(0, HEIGHT_SIZE - 1)][Utils.randInt(0, WIDTH_SIZE - 1)];
        } while (!cell.getHazard().equals(Hazard.NONE));

        mCurrentCell = cell;
        mCurrentCell.setVisited(true);

    }

    private ArrayList<Cell> getNeighbors(final Cell cell) {
        final ArrayList<Cell> neighbors = new ArrayList<Cell>();

        // SIDES

        if (hasUpCell(cell)) {
            neighbors.add(getUpCell(cell));
        }

        if (hasDownCell(cell)) {
            neighbors.add(getDownCell(cell));
        }

        if (hasLeftCell(cell)) {
            neighbors.add(getLeftCell(cell));
        }

        if (hasRightCell(cell)) {
            neighbors.add(getRightCell(cell));
        }

        // EDGES

        // if (cell.getPositionX() > 0 && cell.getPositionY() > 0) {
        // neighbors.add(mMatrix[cell.getPositionY() - 1][cell.getPositionX() -
        // 1]);
        // }
        //
        // if (cell.getPositionX() < WIDTH_SIZE - 1 && cell.getPositionY() > 0)
        // {
        // neighbors.add(mMatrix[cell.getPositionY() - 1][cell.getPositionX() +
        // 1]);
        // }
        //
        // if (cell.getPositionX() > 0 && cell.getPositionY() < HEIGHT_SIZE - 1)
        // {
        // neighbors.add(mMatrix[cell.getPositionY() + 1][cell.getPositionX() -
        // 1]);
        // }
        //
        // if (cell.getPositionX() < WIDTH_SIZE - 1 && cell.getPositionY() <
        // HEIGHT_SIZE - 1) {
        // neighbors.add(mMatrix[cell.getPositionY() + 1][cell.getPositionX() +
        // 1]);
        // }

        return neighbors;
    }

    public Cell getUpCell(final Cell cell) {
        if (hasUpCell(cell)) {
            return mMatrix[cell.getPositionY() - 1][cell.getPositionX()];
        }

        return null;
    }

    public boolean hasUpCell(final Cell cell) {
        return cell.getPositionY() > 0;
    }

    public Cell getDownCell(final Cell cell) {
        if (hasDownCell(cell)) {
            return mMatrix[cell.getPositionY() + 1][cell.getPositionX()];
        }

        return null;
    }

    public boolean hasDownCell(final Cell cell) {
        return cell.getPositionY() < HEIGHT_SIZE - 1;
    }

    public Cell getLeftCell(final Cell cell) {
        if (hasLeftCell(cell)) {
            return mMatrix[cell.getPositionY()][cell.getPositionX() - 1];
        }

        return null;
    }

    public boolean hasLeftCell(final Cell cell) {
        return cell.getPositionX() > 0;
    }

    public Cell getRightCell(final Cell cell) {
        if (hasRightCell(cell)) {
            return mMatrix[cell.getPositionY()][cell.getPositionX() + 1];
        }

        return null;
    }

    public boolean hasRightCell(final Cell cell) {
        return cell.getPositionX() < WIDTH_SIZE - 1;
    }

    public void moveUp() {
        if (hasUpCell(mCurrentCell)) {
            mCurrentCell = mMatrix[mCurrentCell.getPositionY() - 1][mCurrentCell.getPositionX()];
            mCurrentCell.setVisited(true);
        }

        checkBats();
    }

    public void moveDown() {
        if (hasDownCell(mCurrentCell)) {
            mCurrentCell = mMatrix[mCurrentCell.getPositionY() + 1][mCurrentCell.getPositionX()];
            mCurrentCell.setVisited(true);
        }

        checkBats();
    }

    public void moveLeft() {
        if (hasLeftCell(mCurrentCell)) {
            mCurrentCell = mMatrix[mCurrentCell.getPositionY()][mCurrentCell.getPositionX() - 1];
            mCurrentCell.setVisited(true);
        }

        checkBats();
    }

    public void moveRight() {
        if (hasRightCell(mCurrentCell)) {
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

    public Cell getCurrentCell() {
        return mCurrentCell;
    }

    public boolean isGameOver() {

        if (!hasArrow()) {
            return true;
        }

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

    private boolean hasArrow() {
        return mArrow > 0 ? true : false;
    }

    public void attackUp() {
        attack(getUpCell(getCurrentCell()));
    }

    public void attackDown() {
        attack(getDownCell(getCurrentCell()));
    }

    public void attackLeft() {
        attack(getLeftCell(getCurrentCell()));
    }

    public void attackRight() {
        attack(getRightCell(getCurrentCell()));
    }

    public void attack(final Cell cell) {
        mArrow--;

        if (cell != null && cell.getHazard().equals(Hazard.WUMPUS)) {
            System.out.println("You killed the wumpus!\nYou WIN!\n");
        }

    }
}
