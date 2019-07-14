package mvnikitin.javaadvanced.lesson1.marathon.obstacles;

import mvnikitin.javaadvanced.lesson1.marathon.participants.Competitor;

public class Cross extends Obstacle {
    int length;

    public Cross(int length) {
        this.length = length;
    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.run(length);
    }
}