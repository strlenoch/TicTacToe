package ru.tictactoe.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Game {

    private final Field gameField;

    private boolean isOver;

    /**
     * Сообщение о победителе для JS
     */
    private String winnerMessage;

    public Game(int size) {
        gameField = new Field(size);
    }

    public Field getGameField() {
        return gameField;
    }

    public boolean isOver() {
        return isOver;
    }

    private boolean cellOnField(int i, int j) {
        return 0 <= i && i < gameField.getSize() && 0 <= j && j < gameField.getSize();
    }

    /**
     * Поиск максимальной длины цепочки в заданном направлении.
     * Для горизонатального направления (di = 0, dj = 1).
     * Для вертикального направления (di = 1, dj = 0).
     * Для диагонального направления (di = 1, dj = 1), либо (di = 1, dj = -1).
     *
     * @param directionI направление по строкам
     * @param directionJ направление по столбцам
     * @param symbol     крестик или нолик
     */
    private ArrayList<Chain> findChains(int directionI,
                                        int directionJ,
                                        char symbol) {
        ArrayList<Chain> chains = new ArrayList<>();

        for (int i = 0; i < gameField.getSize(); i++) {
            for (int j = 0; j < gameField.getSize(); j++) {
                if (gameField.getSymbolByCoordinates(i, j) == symbol) {
                    // если следующий и предыдущий символ лежат вне поля, то такие цепочки отбрасываем
                    if (!cellOnField(i - directionI, j - directionJ)
                            && !cellOnField(i + directionI, j + directionJ)) {
                        continue;
                    }

                    // если в предыдущей клетке стоит такой же символ, то текущий символ пропускаем
                    if (cellOnField(i - directionI, j - directionJ)
                            && gameField.getSymbolByCoordinates(i - directionI, j - directionJ) == symbol) {
                        continue;
                    }

                    int length = 0;
                    Chain tempChain = new Chain(directionI, directionJ);
                    tempChain.setStart(i, j);

                    // идем по направлению (di, dj)
                    for (int k = i, l = j; cellOnField(k, l); k += directionI, l += directionJ) {
                        if (gameField.getSymbolByCoordinates(k, l) != symbol) {
                            break;
                        }
                        length++;
                        tempChain.setFinish(k, l);
                    }

                    tempChain.setLength(length);
                    chains.add(tempChain);
                }
            }
        }
        return chains;
    }

    /**
     * Поиск цепочек из заданных символов во всех направлениях
     *
     * @param symbol крестик или нолик
     * @return массив всех цепочек, расположенных на поле
     */
    private ArrayList<Chain> findAllChains(char symbol) {
        // диагональные цепочки
        Set<Chain> set = new HashSet<>(findChains(1, 1, symbol));
        set.addAll(findChains(1, -1, symbol));
        // горизонтальные цепочки
        set.addAll(findChains(0, 1, symbol));
        // вертикальные цепочки
        set.addAll(findChains(1, 0, symbol));
        return new ArrayList<>(set);
    }

    /**
     * Поиск цепочки из заданных символов, которая имеет максимальную длину
     *
     * @param symbol крестик или колик
     * @return цепочка с максимальноцй длиной
     */
    private Chain findMaxChain(char symbol) {
        return Chain.getMaxChain(findAllChains(symbol));
    }

    /**
     * Алгорит, который пытается "помешать" человеку,
     * а если не получается, то ставит нолик в первую попавшуюся клеточку
     *
     * @param i строка, в которую пользователь хочет поставить крестик
     * @param j столбец, в который пользователь хочет поставить крестик
     */
    public void nextStep(int i, int j) {
        gameField.setX(i, j);

        if (gameField.isGameOver()) {
            setWinner();
            isOver = true;
            return;
        }

        int emptyBetweenFilled = findEmptyCellBetweenFilled();

        if (emptyBetweenFilled != -1) {
            int x = emptyBetweenFilled / gameField.getSize();
            int y = emptyBetweenFilled % gameField.getSize();
            gameField.setO(x, y);
            return;
        }

        ArrayList<Chain> allChains = findAllChains(Field.X);

        // сортируем по убыванию
        allChains.sort((o1, o2) -> -o1.compareTo(o2));

        // флаг завершенности хода компьютера
        boolean ok = false;

        for (Chain c : allChains) {
            int di = c.getDirectionI(), dj = c.getDirectionJ();
            try {
                // пытаемся поставить нолик в начале цепочки
                gameField.setO(c.getStartI() - di, c.getStartJ() - dj);
                ok = true;
                break;
            } catch (IllegalArgumentException e) {
                try {
                    // пытаемся поставить нолик в конце цепочки
                    gameField.setO(c.getFinishI() + di, c.getFinishJ() + dj);
                    ok = true;
                    break;
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        if (!ok) {
            int index = getEmptyCellIndex();
            // привели двумерный индекс к одномерному
            int x = index / gameField.getSize();
            int y = index % gameField.getSize();
            gameField.setO(x, y);
        }

        if (gameField.isGameOver()) {
            setWinner();
            isOver = true;
        }
    }

    /**
     * Поиск пустой ячейки(первой встретившейся)
     *
     * @return одмномерный индекс ячейки
     */
    private int getEmptyCellIndex() {
        for (int i = 0; i < gameField.getSize(); i++) {
            for (int j = 0; j < gameField.getSize(); j++) {
                if (gameField.getSymbolByCoordinates(i, j) == Field.EMPTY) {
                    return i * gameField.getSize() + j;
                }
            }
        }
        throw new IllegalStateException("Поле заполнено");
    }

    /**
     * Поиск пустой ячейки между двумя заполненными
     * (в заполненных должны стоять крестики).
     *
     * @return одмномерный индекс ячейки
     */
    private int findEmptyCellBetweenFilled() {
        for (int i = 0; i < gameField.getSize(); i++) {
            for (int j = 0; j < gameField.getSize(); j++) {
                if (gameField.getSymbolByCoordinates(i, j) != Field.EMPTY) {
                    continue;
                }

                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        try {
                            if (k == 0 && l == 0) {
                                continue;
                            }
                            if (gameField.getSymbolByCoordinates(i + k, j + l) == gameField.getSymbolByCoordinates(i - k, j - l)
                                    && gameField.getSymbolByCoordinates(i + k, j + l) == Field.X) {
                                return i * gameField.getSize() + j;
                            }
                            ;
                        } catch (RuntimeException ignored) {
                        }
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Установка сообщения о победителе
     */
    private void setWinner() {
        int user = findMaxChain(Field.X).getLength();
        int computer = findMaxChain(Field.O).getLength();

        if (user > computer) {
            winnerMessage = "Вы победили!";
        } else if (user == computer) {
            winnerMessage = "Ничья!";
        } else {
            winnerMessage = "Победил компьютер!";
        }
    }
}
