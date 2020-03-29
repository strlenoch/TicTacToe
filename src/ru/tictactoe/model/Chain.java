package ru.tictactoe.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Цепочка символов на игровом поле
 */
class Chain implements Comparable<Chain> {

    private int length;

    private int startI;
    private int startJ;
    private int finishI;
    private int finishJ;

    private final int directionI, directionJ;

    Chain(int directionI, int directionJ) {
        this.directionI = directionI;
        this.directionJ = directionJ;
    }

    void setStart(int i, int j) {
        startI = i;
        startJ = j;
    }

    void setFinish(int i, int j) {
        finishI = i;
        finishJ = j;
    }

    void setLength(int length) {
        this.length = length;
    }

    int getDirectionI() {
        return directionI;
    }

    int getDirectionJ() {
        return directionJ;
    }

    int getLength() {
        return length;
    }

    int getStartI() {
        return startI;
    }

    int getStartJ() {
        return startJ;
    }

    int getFinishI() {
        return finishI;
    }

    int getFinishJ() {
        return finishJ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chain chain = (Chain) o;
        return length == chain.length &&
                startI == chain.startI &&
                startJ == chain.startJ &&
                finishI == chain.finishI &&
                finishJ == chain.finishJ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, startI, startJ, finishI, finishJ);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d) -> (%d, %d) [len=%d]", startI, startJ, finishI, finishJ, length);
    }

    @Override
    public int compareTo(Chain chain) {
        return Integer.compare(length, chain.length);
    }

    static Chain getMaxChain(ArrayList<Chain> chains) {
        Chain firstChain;
        Chain secondChain;
        firstChain = chains.get(0);
        for (int i = 1; i < chains.size(); i++) {
            secondChain = chains.get(i);
            firstChain = firstChain.compareTo(secondChain) > 0 ? firstChain : secondChain;
        }
        return firstChain;
    }
}
