import processing.core.PImage;

import java.util.List;

public final class Background
{
    private String id;
    private List<PImage> images;
    private int imageIndex;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public String getId() {return id;}
    public List<PImage> getImages() {return images;}
    public int getImageIndex() {return imageIndex;}

    public PImage getCurrentImage() {
        return images.get(imageIndex);
    }
}
