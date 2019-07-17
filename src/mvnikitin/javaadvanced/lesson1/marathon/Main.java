package mvnikitin.javaadvanced.lesson1.marathon;

public class Main {
    public static void main(String[] args) {

        Course course = new Course();
        Team team = new Team("Трудовые резервы");

        // Даём старт марафону
        course.doIt(team);

        // выводим информацию о членах команды, прошедших дистанцию
        team.showResults();
    }
}