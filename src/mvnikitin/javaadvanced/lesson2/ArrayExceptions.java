package mvnikitin.javaadvanced.lesson2;

import java.util.Scanner;
import mvnikitin.javaadvanced.utils.*;

class MySizeArrayException extends Exception {
    public MySizeArrayException(int size) {
        super("Размерность массива должна иметь значение " + size);
    }
}

class MyArrayDataException extends NumberFormatException {
    public MyArrayDataException(int line, int col) {
        super("Не удалось преобразовать к int значение в ячейке [" + line +
                "][" + col + "].");
    }
}

public class ArrayExceptions {

    private String[][] strings;
    private static final int ARRAY_SIZE = 4;

    public ArrayExceptions() {
        fillArray();
    }

    public static void main(String[] args) {

        ArrayExceptions testObject = new ArrayExceptions();

        try {
            long arraySum = testObject.calcArray4x4IntSum();
            System.out.println("Сумма значений элементов массива: " + arraySum);
        }
        catch (MySizeArrayException sizeArrayEx) {
            sizeArrayEx.printStackTrace();
        }
        catch (MyArrayDataException arrayDataEx) {
            arrayDataEx.printStackTrace();
        }
    }

    /**
     * Метод расчёта суммы элементов массива 4 х 4.
     * @return long - сумма элементов массива.
     * @throws MySizeArrayException - любая из расмерностей массива не равна 4.
     * @throws MyArrayDataException - не удалось привести элемент [i][j] к int.
     */
    public long calcArray4x4IntSum()
        throws MySizeArrayException,
            MyArrayDataException {

        long res = 0;

        if (strings.length != ARRAY_SIZE)
            throw new MySizeArrayException(ARRAY_SIZE);
        else {
            for (int i = 0; i < strings.length; i++)
                if (strings[i].length != ARRAY_SIZE)
                    throw new MySizeArrayException(ARRAY_SIZE);
        }

        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings[i].length; j++) {
                try {
                    res += Integer.parseInt(strings[i][j]);
                }
                catch (NumberFormatException formatEx) {
                    throw new MyArrayDataException(i, j);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return res;
    }

    /**
     * Хэлпер для заполнения массива значениями из консоли.
     */
    private void fillArray() {

        Scanner userInput = new Scanner(System.in);

        int linesCount = 0;
        System.out.println("Задайте число строк массива:");
        linesCount = IOUtils.readNaturalNumber(userInput);

        int colsCount = 0;
        System.out.println("Задайте число столбцов массива:");
        colsCount = IOUtils.readNaturalNumber(userInput);

        strings = new String[linesCount][colsCount];

        System.out.println("\nЗаполните массив числовыми значениями" +
                        " для вычисления их суммы\n");

        // наполняем массив
        for (int i = 0; i < strings.length; i++) {

            System.out.println("Заполняем строку " + i + ":");

            for (int j = 0; j < strings[i].length; j++) {
                System.out.println("Задайте элемент [" + i + "][" + j + "]:");
                strings[i][j] = IOUtils.readString(userInput);
            }
        }

        userInput.close();

        System.out.println("\nВаш массив:");

        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings[i].length; j++) {
                System.out.print(strings[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();

    }
}
