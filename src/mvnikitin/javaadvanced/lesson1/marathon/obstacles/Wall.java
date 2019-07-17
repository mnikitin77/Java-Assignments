package mvnikitin.javaadvanced.lesson1.marathon.obstacles;

import mvnikitin.javaadvanced.lesson1.marathon.participants.Competitor;

public class Wall extends Obstacle {
    int height;

    public Wall(int height) {
        this.height = height;
    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.jump(height);
    }
}