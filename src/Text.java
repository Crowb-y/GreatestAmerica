import processing.core.PImage;
import java.util.List;

public class Text extends Entity{

    public Text(String id, Point position, List<PImage> images, int index) {
        super(id, position, images, index);
    }

    public static Text createText(String id, Point pos, List<PImage> images) {
        return new Text(id, pos, images, 0);
    }
}


