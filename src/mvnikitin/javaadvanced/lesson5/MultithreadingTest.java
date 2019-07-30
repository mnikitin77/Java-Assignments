package mvnikitin.javaadvanced.lesson5;

public class MultithreadingTest {

    private static final int SIZE = 10000000;

    public static void main(String[] args) {

        MultithreadingTest threadTest = new MultithreadingTest();

        threadTest.methodOne();
        System.out.println();

        threadTest.methodTho(3);
    }

    /**
     * Метод 1: вычисления целого массива в основном потоке.
     */
    private void methodOne() {

        float[] array = new float[SIZE];
        initArray(array, 1.0f);

        System.out.println("Метод 1:");
        long startTime = System.currentTimeMillis();
        performCalculations(array);
        long timeSpent = System.currentTimeMillis() - startTime;

        System.out.println("Результат выполнения: " +
                formatDate(timeSpent) + " [" + timeSpent + " мс]");
    }

    /**
     * Метод 2: вычисления массива по частям в нескольих потоках.
     * @param threadsCount - число потоков для вычислений
     */
    private void methodTho(int threadsCount) {

        float[] array = new float[SIZE];
        initArray(array, 1.0f);

        //class ArrayProcessingThread implements Runnable {
        class ArrayProcessingThread extends Thread {

            private int sourceArrayStartingIndex;
            private int elementsCount;

            private float[] partArray;

            ArrayProcessingThread (
                    int sourceArrayStartingIndex,
                    int elementsCount) {
                this.sourceArrayStartingIndex = sourceArrayStartingIndex;
                this.elementsCount = elementsCount;

                partArray = new float[elementsCount];
            }

            @Override
            public void run() {
//                try {//TODO
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                // разбивка массива (копирование части исходного в новый)
                System.arraycopy(array, sourceArrayStartingIndex,
                        partArray, 0, elementsCount);

                // вычисления
                performCalculations(partArray);
            }

            /**
             * Метод "обратной склейки" внутреннего массива в исходный
             */
            public void updateSourceArray() {
                System.arraycopy(partArray, 0, array,
                        sourceArrayStartingIndex, elementsCount);
            }
        }

        System.out.println("Метод 2:");

        // Создаём объекты классов для параллельных вычислений
        // в отдельных потоках.
        ArrayProcessingThread[] arrayThreads =
                new ArrayProcessingThread[threadsCount];

        // Рассчитываем размер массива для потока
        int partsSize = calculatePartArraySize(array.length, threadsCount);

        // Фиксируем начало измерений
        long startTime = System.currentTimeMillis();

        // Стартуем N - 1 поток и производим вычисления
        for (int i = 0; i < arrayThreads.length - 1; i++) {

            arrayThreads[i] =
                    new ArrayProcessingThread(i * partsSize, partsSize);
            arrayThreads[i].start();
        }

//        arrayThreads[0] = new ArrayProcessingThread(0 * partsSize, partsSize);
//        arrayThreads[0].start();

        // Запускаем последний поток (размер элементов массива может быть
        // меньше, чем partsSize
        arrayThreads[arrayThreads.length - 1] =
                new ArrayProcessingThread(
                (threadsCount - 1) * partsSize,
                array.length - (threadsCount - 1) * partsSize);
        arrayThreads[arrayThreads.length - 1].start();

        //TODO Сделать склейку
        for (ArrayProcessingThread t: arrayThreads) {
            t.updateSourceArray();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Останавливаем секундомер, фиксируем измерение
        long timeSpent = System.currentTimeMillis() - startTime;

        System.out.println("Результат выполнения: " +
                formatDate(timeSpent) + " [" + timeSpent + " мс]");
    }


/******************************************************************************
*                               Хелперы                                       *
******************************************************************************/

    /**
     * Метод инициализации элементов массива числом
     * @param arr - массив
     * @param initValue - числло, которым инициализируется массив
     */
    private void initArray(float[] arr, float initValue) {
        for (int i = 0; i < arr.length; i++)
            arr[i] = initValue;
    }

    /**
     * Метод вычислений новй значения элементов массива
     * @param arr - массив
     */
    private void performCalculations(float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) *
                    Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
    }

    // заимствовано здесь:
    // http://qaru.site/questions/63869/java-convert-milliseconds-to-time-format

    /**
     * Форматирует время в миллисекундах в строку
     * @param timeInMillis - время в милиссекундах
     * @return - строка в виде чч:мм:сс.мск
     */
    String formatDate(long timeInMillis) {

        return String.format("%02d:%02d:%02d.%d",
                (timeInMillis / (1000 * 60 * 60)) % 24,
                (timeInMillis / (1000 * 60)) % 60,
                (timeInMillis / 1000) % 60,
                timeInMillis % 1000);
    }

    /**
     * Метод деления на целого на partsCount равных частей, если целое делится
     * нацело на части, и N - 1 равных и одну размером в
     * sourceLength - (partsCount - 1) * размер_части
     * @param sourceLength - исходная длина
     * @param partsCount - число частей
     * @return - длина части
     */
    int calculatePartArraySize(int sourceLength, int partsCount) {
        int res = 0;

        if(partsCount > 0) {
            // получаем остаток от деления
            int remainder = sourceLength % partsCount;
            if (remainder == 0) {
                return sourceLength / partsCount;
            }
            else {
                // сохраняем целую часть от деления
                res = sourceLength / partsCount;
                // добавляем ближайшее большее целое от деления остатка
                // на число частей дробно
                res += (int)Math.ceil((double)remainder / partsCount);
            }
        }

        return res;
    }
}
