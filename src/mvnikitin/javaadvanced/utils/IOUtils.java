package mvnikitin.javaadvanced.utils;

import java.util.Scanner;

public class IOUtils {

    public static int readNaturalNumber(Scanner sc) {
        int result = 0;

        do {
            if(sc.hasNextInt()) {
                result = sc.nextInt();
                if(result > 0)
                    break;
            } else {
                sc.next();
            }

            System.out.println("Некорректное значение.");

        } while (result <= 0);

        return result;
    }

    public static int readIntNumber(Scanner sc) {
        int result = 0;

        do {
            if(sc.hasNextInt()) {
                result = sc.nextInt();
                break;
            } else {
                sc.next();
            }

            System.out.println("Некорректное значение.");

        } while (result <= 0);

        return result;
    }

    public static String readString(Scanner sc) {
        String result = "";

        do {
            if(sc.hasNextLine())
                result = sc.nextLine();

            if(result.length() > 0)
                break;

        } while (true);

        return result;
    }

}
