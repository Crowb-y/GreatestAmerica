import processing.core.PImage;

import java.util.List;

public class Blacksmith implements Passive {

    private final String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Blacksmith(String id, Point position, List<PImage> images, int imageIndex) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void nextImage() {

    }

    @Override
    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    @Override
    public PImage getCurrentImage() {
        return images.get(imageIndex);
    }

    @Override
    public void setPosition(Point pos) {
        position = pos;
    }
}
