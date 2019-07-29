package mvnikitin.javaadvanced.lesson3;

import java.util.Map;
import java.util.TreeMap;

public class WordsCounter {

    // Использую TreeMap, только с целью сортировки по ключу
    private Map<String, Integer> uniqueWords;

    public WordsCounter() {
        uniqueWords = new TreeMap<>();
    }

    /**
     * Метод расчитывает число повторений слов и выводит слова и число
     * повторений на консоль стандартным способом в алфавитном порядке.
     * @param words - входной массив строк для подсчёта.
     */
    public void wordsCountInfo(String[] words) {
        for (String s: words) {
            // getOrDefault для этого советовали или для чего-то ещё?
            uniqueWords.put(s, uniqueWords.getOrDefault(s, 0) + 1);
        }

        System.out.println(uniqueWords);
    }

    public static void main(String[] args) {

        String[] words = {
                "Russia",
                "Mexico",
                "Spain",
                "Germany",
                "Russia",
                "Mexico",
                "USA",
                "China",
                "Mexico",
                "China",
                "Austria",
                "Italy",
                "Russia",
                "China",
                "USA",
                "Czech Republic",
                "Turkey",
                "France",
                "Canada",
                "Brasil"
        };

        WordsCounter counter = new WordsCounter();
        counter.wordsCountInfo(words);
    }
}
