package br.edu.ufcg.wumpusia.utils;

/**
 * Created by Zk on 19/03/2015.
 */
public class Result {
    private final Long mSeed;
    private String mEuristica;
    private int mQtdeMoves;
    private boolean mWin;

    public Result(String euristica, int qtdeMoves, boolean win, Long seed) {
        mEuristica = euristica;
        mQtdeMoves = qtdeMoves;
        mWin = win;
        mSeed = seed;
    }

    public String getEuristica() {
        return mEuristica;
    }

    public int getQtdeMoves() {
        return mQtdeMoves;
    }

    public boolean getWin() {
        return  mWin;
    }

    public Long getSeed() {
        return mSeed;
    }

    @Override
    public String toString(){
        return (getEuristica() + "," + getQtdeMoves() + "," + getWin() + "," + getSeed());
    }

}
