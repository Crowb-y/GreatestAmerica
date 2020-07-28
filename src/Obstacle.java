import processing.core.PImage;
import java.util.List;

public class Obstacle implements Passive {
    private final String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public void setPosition(Point position) {
        this.position = position;
    }

    public Obstacle(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public String getId() {
        return id;
    }

    public List<PImage> getImages() {
        return images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    @Override
    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    @Override
    public PImage getCurrentImage() {
        return images.get(imageIndex);
    }
}
