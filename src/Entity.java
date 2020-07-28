import processing.core.PImage;

public interface Entity {
    Point getPosition();

    void nextImage();

    Action createAnimationAction(int repeatCount);

    PImage getCurrentImage();

    void setPosition(Point pos);
}
