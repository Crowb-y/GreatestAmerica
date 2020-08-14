import processing.core.PImage;
import java.util.List;

public class Obstacle extends Entity {

    public Obstacle(String id, Point position, List<PImage> images, int index) {
        super(id, position, images, index);
    }

    public static Obstacle createObstacle(String id, Point pos, List<PImage> images) {
        return new Obstacle(id, pos, images, 0);
    }
}
