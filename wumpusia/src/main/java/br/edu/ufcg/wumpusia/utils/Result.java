package br.edu.ufcg.wumpusia.utils;

/**
 * Created by Zk on 19/03/2015.
 */
public class Result {
    private String mEuristica;
    private int mQtdeMoves;
    private boolean mWin;

    public Result(String euristica, int qtdeMoves, boolean win) {
        mEuristica = euristica;
        mQtdeMoves = qtdeMoves;
        mWin = win;
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

    @Override
    public String toString(){
        return (getEuristica() + "," + getQtdeMoves() + "," + getWin());
    }

}
