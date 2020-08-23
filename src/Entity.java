import processing.core.PImage;

import java.util.List;

abstract public class Entity {

    private final String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images, int index) {
        this.id = id;
        this.images = images;
        this.position = position;
        this.imageIndex = index;
    }

    protected Point getPosition() {
        return position;
    }

    protected void setPosition(Point position) {
        this.position = position;
    }

    protected void setImages(List<PImage> myImages){ this.images = myImages; }

    protected void setImageIndex(int index) {this.imageIndex = index; }

    protected int getImageIndex() { return this.imageIndex; }

    protected List<PImage> getImages() { return images; }

    protected String getId() { return this.id; }

    protected PImage getCurrentImage() {
        return images.get(imageIndex);
    }

}
