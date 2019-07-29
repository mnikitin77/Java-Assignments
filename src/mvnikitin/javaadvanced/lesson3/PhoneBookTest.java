package mvnikitin.javaadvanced.lesson3;

import java.util.*;

class PhoneBook {

    private Map<String, Set<String>> phoneBook;

    public PhoneBook() {
        // Использую TreeMap для сортировки по умолчанию.
        phoneBook = new TreeMap<>();
    }

    /**
     * Метод добавляет фамилию и телефон в телефонный справочник.
     * Если в справочнике фамилия уже присутствует, добавляет ещё один телефон.
     * @param secondName - фамилия.
     * @param phone - телефон.
     * @return true, если номер добавлен.
     */
    boolean add(String secondName, String phone) {
        if (phoneBook.get(secondName) == null) {
            phoneBook.put(secondName, new HashSet<>());
        }

        // если такой телефон уже есть, вернёт false
        return phoneBook.get(secondName).add(phone);
    }

    /**
     * Метод возвращает телефоны по фамилии в виде массива строк.
     * @param secondName - фамилия для поиска телефонов.
     * @return String[] - массив номеров телефонов или null.
     */
    String[] get(String secondName) {
        String[] phones = null;

        Set<String> phonesSet = phoneBook.get(secondName);
        if (phonesSet != null) {
            phones = new String[phonesSet.size()];
            phones = phonesSet.toArray(phones);
        }

        return phones;
    }

    public void printBook() {
        for ( String secondName: phoneBook.keySet() ) {
            System.out.println(secondName + ": " +
                    Arrays.deepToString(phoneBook.get(secondName).toArray()));
        }
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook testPhoneBook = new PhoneBook();

        testPhoneBook.add("Иванов", "+7 916 123 4567");
        testPhoneBook.add("Иванов", "+7 985 001 0555");

        testPhoneBook.add("Васильев", "+7 903 322 2233");
        testPhoneBook.add("Васильев", "+7 926 321 1234");
        testPhoneBook.add("Васильев", "+7 910 344 6736");

        testPhoneBook.add("Огурцов", null);

        testPhoneBook.add("Рюрикович", "+7 911 765 4321");
        testPhoneBook.add("Рюрикович", "+7 911 765 4321");
        testPhoneBook.add("Рюрикович", "+7 911 765 4321");

        // Печатаем всю телефонную книгу
        System.out.println("Содержимое телефонной книги:");
        testPhoneBook.printBook();

        System.out.println();
        System.out.println("Вывод на экран несколько записей PhoneBook::get():");

        System.out.println("Гаврилов: " +
                Arrays.deepToString(testPhoneBook.get("Гаврилов")));
        System.out.println("Васильев: " +
                Arrays.deepToString(testPhoneBook.get("Васильев")));
    }
}
