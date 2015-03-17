package br.edu.ufcg.wumpusgame.models;

public class Cell {

    private Hazard mHazard;

    private boolean mFeelBreeze;
    private boolean mSmellWumpus;
    private boolean mHearFlapping;
    private boolean mVisited;

    private final int mPositionY;
    private final int mPositionX;

    public Cell(final int positionY, final int positionX) {
        mHazard = Hazard.NONE;

        mPositionX = positionX;
        mPositionY = positionY;
    }

    public Hazard getHazard() {
        return mHazard;
    }

    public void setHazard(final Hazard hazard) {
        mHazard = hazard;
    }

    public boolean isFeelBreeze() {
        return mFeelBreeze;
    }

    public void setFeelBreeze(final boolean feelBreeze) {
        mFeelBreeze = feelBreeze;
    }

    public boolean isSmellWumpus() {
        return mSmellWumpus;
    }

    public void setSmellWumpus(final boolean smellWumpus) {
        mSmellWumpus = smellWumpus;
    }

    public boolean isHearFlapping() {
        return mHearFlapping;
    }

    public void setHearFlapping(final boolean hearFlapping) {
        mHearFlapping = hearFlapping;
    }

    public boolean isVisited() {
        return mVisited;
    }

    public void setVisited(final boolean visited) {
        mVisited = visited;
    }

    public int getPositionY() {
        return mPositionY;
    }

    public int getPositionX() {
        return mPositionX;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mFeelBreeze ? 1231 : 1237);
        result = prime * result + (mHazard == null ? 0 : mHazard.hashCode());
        result = prime * result + (mHearFlapping ? 1231 : 1237);
        result = prime * result + mPositionX;
        result = prime * result + mPositionY;
        result = prime * result + (mSmellWumpus ? 1231 : 1237);
        result = prime * result + (mVisited ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cell other = (Cell) obj;
        if (mFeelBreeze != other.mFeelBreeze) {
            return false;
        }
        if (mHazard != other.mHazard) {
            return false;
        }
        if (mHearFlapping != other.mHearFlapping) {
            return false;
        }
        if (mPositionX != other.mPositionX) {
            return false;
        }
        if (mPositionY != other.mPositionY) {
            return false;
        }
        if (mSmellWumpus != other.mSmellWumpus) {
            return false;
        }
        if (mVisited != other.mVisited) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (isVisited()) {
            return "V";
        } else {
            return "O";
        }
    }

    public String getId() {
        return String.format("(%d,%d)", getPositionX(), getPositionY());
    }

}
