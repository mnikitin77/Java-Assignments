package mvnikitin.javaadvanced.lesson5;

public class MultithreadingTest {

    private static final int SIZE = 10000000;

    public static void main(String[] args) {

        MultithreadingTest threadTest = new MultithreadingTest();
        threadTest.methodOne();
        System.out.println();
    }

    private void methodOne() {
        float[] array = new float[SIZE];

        System.out.println("Метод 1:");
        initArray(array);

        long startTime = System.currentTimeMillis();
        calculateFormula(array);

        System.out.println("Результат выполнения: " +
                formatDate(System.currentTimeMillis() - startTime));
    }

    private void methodTho(int partsCount) {  // нужно сделать много массивов, как просил преподаватель.

    }


/******************************************************************************
*                               Хелперы                                       *
******************************************************************************/

    private void initArray(float[] arr) {
        for (float element: arr)
            element = 1.0f;
    }

    private void calculateFormula(float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) *
                    Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
    }

    // заимствовано здесь:
    // http://qaru.site/questions/63869/java-convert-milliseconds-to-time-format
    String formatDate(long dateInMillis) {

        return String.format("%02d:%02d:%02d.%d",
                (dateInMillis / (1000 * 60 * 60)) % 24,
                (dateInMillis / (1000 * 60)) % 60,
                (dateInMillis / 1000) % 60,
                dateInMillis % 1000);
    }
}
