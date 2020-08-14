import processing.core.PImage;

import java.util.List;

public class Blacksmith extends Entity {

    public Blacksmith(String id, Point position, List<PImage> images, int index) {
        super(id, position, images, index);
    }

    public static Blacksmith createBlacksmith(String id, Point pos, List<PImage> images) {
        return new Blacksmith(id, pos, images, 0);
    }
}