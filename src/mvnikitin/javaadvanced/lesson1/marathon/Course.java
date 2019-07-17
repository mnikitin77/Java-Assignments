package mvnikitin.javaadvanced.lesson1.marathon;

import mvnikitin.javaadvanced.lesson1.marathon.obstacles.*;

public class Course {

    private Obstacle[] obstacles;
    private static final int OBSTACLES_COUNT = 4;

    public Course() {
        obstacles = new Obstacle[OBSTACLES_COUNT];

        // Создаём препятствия
        obstacles[0] = new Cross(120);
        obstacles[1] = new Wall(2);
        obstacles[2] = new Water(200);
        obstacles[3] = new Cross(2000);
    }

    /**
     * Метод старта и прохождения марафона командой.
     * @param team - команда участников марафона.
     */
    public void doIt(Team team) {
        for(Obstacle o: obstacles) {
            team.passObstacle(o);
        }
    }
}
