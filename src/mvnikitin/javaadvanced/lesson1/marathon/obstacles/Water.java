package mvnikitin.javaadvanced.lesson1.marathon.obstacles;

import mvnikitin.javaadvanced.lesson1.marathon.participants.Competitor;

public class Water extends Obstacle {
    int length;

    public Water(int length) {
        this.length = length;
    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.swim(length);
    }
}