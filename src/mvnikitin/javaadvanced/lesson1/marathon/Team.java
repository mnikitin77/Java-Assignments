package mvnikitin.javaadvanced.lesson1.marathon;

import mvnikitin.javaadvanced.lesson1.marathon.participants.*;
import mvnikitin.javaadvanced.lesson1.marathon.obstacles.Obstacle;

public class Team {

    private String name;
    private final static int MEMBERS_COUNT = 4;
    private Competitor[] teamMembers;

    public Team(String name) {
        this.name = name;
        teamMembers = new Competitor[MEMBERS_COUNT];

        // Наполняем команду участниками
        teamMembers[0] = new Human("Боб");
        teamMembers[1] = new Cat("Барсик");
        teamMembers[2] = new Dog("Бобик");
        teamMembers[3] = new Human("Иван");

        // Представляем команду
        info();
    }

    /**
     * Метод прохождения препятствия участниками, не соседшими с дистанции.
     * @param obstacle - Препятствие, которое требуется пройти команде
     */
    public void passObstacle(Obstacle obstacle) {
        for(Competitor member: teamMembers) {
            if (member.isOnDistance())
                obstacle.doIt(member);
        }

        System.out.println();
    }

    /**
     * Метод вывода информации обо всех членах команды.
     */
    public void info() {
        System.out.println("Представляем участников из команды \"" + name + "\":");

        for(Competitor member: teamMembers) {
            member.info();
        }

        System.out.println();
    }

    /**
     * Мметод для вывода информации о членах команды, прошедших дистанцию.
     */
    public void showResults() {
        System.out.println("Поздравляем наших победителей из команды \"" + name + "\":");

        for(Competitor member: teamMembers) {
            if (member.isOnDistance())
                member.info();
        }

        System.out.println();
    }
}
