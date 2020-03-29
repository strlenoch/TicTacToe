package ru.tictactoe.model;


public class Field {
    static final char X = 'X';
    static final char O = 'O';
    static final char EMPTY = ' ';

    /**
     * Массив хранит в себе числа, характеризующие текущее расположение крестиков и ноликов на поле
     */
    private char[][] field;

    private int size;

    Field(int size) {
        this.size = size;
        field = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = Field.EMPTY;
            }
        }
    }

    public char[][] getField() {
        return field;
    }

    public int getSize() {
        return size;
    }

    private void checkBounds(int i, int j) {
        if (i < 0 || size <= i || j < 0 || size <= j) {
            throw new IllegalArgumentException(
                    String.format("Выход за границы поля: size = %d, i = %d, j = %d", size, i, j)
            );
        }
    }

    private void checkEmptyCell(int i, int j) {
        if (field[i][j] != Field.EMPTY) {
            throw new IllegalArgumentException(
                    String.format("Клетка (%d, %d) не пуста", i, j)
            );
        }
    }

    void setX(int i, int j) {
        checkBounds(i, j);
        checkEmptyCell(i, j);
        field[i][j] = Field.X;
    }

    void setO(int i, int j) {
        checkBounds(i, j);
        checkEmptyCell(i, j);
        field[i][j] = Field.O;
    }

    boolean isGameOver() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == Field.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    char getSymbolByCoordinates(int i, int j) {
        checkBounds(i, j);
        return field[i][j];
    }
}
