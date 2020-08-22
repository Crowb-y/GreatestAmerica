import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class Wall extends Entity{

    public Wall(String id, Point position, List<PImage> images, int index){
        super(id, position, images, index);
    }

    public static Wall createWall(String id, Point pos, List<PImage> images) {

        return new Wall(id, pos, images, 0);
    }
}
