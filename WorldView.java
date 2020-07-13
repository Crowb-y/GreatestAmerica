import processing.core.PApplet;
import processing.core.PImage;
import java.util.Optional;

public final class WorldView
{
    private final PApplet screen;
    private final WorldModel world;
    private final int tileWidth;
    private final int tileHeight;
    private final Viewport viewport;

    public WorldView(
            int numRows,
            int numCols,
            PApplet screen,
            WorldModel world,
            int tileWidth,
            int tileHeight)
    {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        viewport = new Viewport(numRows, numCols);
    }

    public PApplet getScreen() {return screen;}
    public WorldModel getWorld() {return world;}
    public int getTileWidth() {return tileWidth;}
    public int getTileHeight() {return tileHeight;}
    public Viewport getViewPort() {return viewport;}

    public static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public void shiftView(int colDelta, int rowDelta) {
        int newCol = clamp(viewport.getCol() + colDelta, 0,
                world.getNumCols() - viewport.getNumCols());
        int newRow = clamp(viewport.getRow() + rowDelta, 0,
                world.getNumRows() - viewport.getNumRows());

        viewport.shift(newCol, newRow);
    }

    public void drawBackground() {
        for (int row = 0; row < viewport.getNumRows(); row++) {
            for (int col = 0; col < viewport.getNumCols(); col++) {
                Point worldPoint = viewport.viewportToWorld(col, row);
                Optional<PImage> image =
                        world.getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    screen.image(image.get(), col * tileWidth,
                            row * tileHeight);
                }
            }
        }
    }

    public void drawEntities() {
        for (Entity entity : world.getEntities()) {
            Point pos = entity.getPosition();

            if (viewport.contains(pos)) {
                Point viewPoint = viewport.worldToViewport(pos.getX(), pos.getY());
                screen.image(world.getCurrentImage(entity),
                        viewPoint.getX() * tileWidth,
                        viewPoint.getY() * tileHeight);
            }
        }
    }

    public void drawViewport() {
        drawBackground();
        drawEntities();
    }

}
