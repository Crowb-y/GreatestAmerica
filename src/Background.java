import processing.core.PImage;

import java.util.List;

public final class Background
{
    private List<PImage> images;
    private int imageIndex;

    public Background(String id, List<PImage> images) {
        this.images = images;
        imageIndex = 0;
    }

    public List<PImage> getImages() {return images;}
    public int getImageIndex() {return imageIndex;}

    public PImage getCurrentImage() {
        return images.get(imageIndex);
    }
}
