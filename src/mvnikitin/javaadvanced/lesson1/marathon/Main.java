package mvnikitin.javaadvanced.lesson1.marathon;

import mvnikitin.javaadvanced.lesson1.marathon.participants.*;
import mvnikitin.javaadvanced.lesson1.marathon.obstacles.*;

public class Main {
    public static void main(String[] args) {
        Competitor[] competitors = {new Human("Боб"), new Cat("Барсик"), new Dog("Бобик")};
        Obstacle[] course = {new Cross(80), new Wall(2), new Wall(1), new Cross(120)};
        for (Competitor c : competitors) {
            for (Obstacle o : course) {
                o.doIt(c);
                if (!c.isOnDistance()) break;
            }
        }
        for (Competitor c : competitors) {
            c.info();
        }
    }
}