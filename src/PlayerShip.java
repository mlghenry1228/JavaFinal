import java.awt.*;

/**
 * Represents the player’s spaceship, which can move horizontally
 * within the game window and be rendered with wings and a thruster.
 */
public class PlayerShip {
    /** Current x-coordinate of the ship’s top point. */
    private int x;

    /** Current y-coordinate of the ship’s top point. */
    private int y;

    /** Width of the ship in pixels. */
    public static final int WIDTH = 40;

    /** Height of the ship in pixels. */
    public static final int HEIGHT = 20;

    /**
     * Constructs a PlayerShip at the specified position.
     *
     * @param x the initial x-coordinate of the ship
     * @param y the initial y-coordinate of the ship
     */
    public PlayerShip(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the current x-coordinate of the ship.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the current y-coordinate of the ship.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Moves the ship left by a fixed amount, ensuring it does not
     * go past the left edge of the game area.
     *
     * @param bound the width of the game area (unused for left movement)
     */
    public void moveLeft(int bound) {
        if (x > 0) {
            x -= 10;
        }
        if (x < 0) {
            x = 0;
        }
    }

    /**
     * Moves the ship right by a fixed amount, ensuring it does not
     * go past the right edge of the game area.
     *
     * @param bound the width of the game area
     */
    public void moveRight(int bound) {
        if (x < bound - WIDTH) {
            x += 10;
        }
        if (x > bound - WIDTH) {
            x = bound - WIDTH;
        }
    }

    /**
     * Draws the ship with a pointed fuselage, wings, and a thruster.
     *
     * @param g the Graphics context on which to draw the ship
     */
    public void draw(Graphics g) {
        // Main body (green pointed fuselage)
        g.setColor(Color.GREEN);
        int[] xPointsBody  = { x + WIDTH / 2, x, x + WIDTH };
        int[] yPointsBody  = { y, y + HEIGHT, y + HEIGHT };
        g.fillPolygon(xPointsBody, yPointsBody, 3);

        // Left wing (white detail)
        g.setColor(Color.WHITE);
        int[] xPointsWingLeft = {
            x + WIDTH / 4, x, x + WIDTH / 4
        };
        int[] yPointsWingLeft = {
            y + HEIGHT, y + HEIGHT / 2, y + HEIGHT / 2
        };
        g.fillPolygon(xPointsWingLeft, yPointsWingLeft, 3);

        // Right wing (white detail)
        int[] xPointsWingRight = {
            x + 3 * WIDTH / 4, x + WIDTH, x + 3 * WIDTH / 4
        };
        int[] yPointsWingRight = {
            y + HEIGHT, y + HEIGHT / 2, y + HEIGHT / 2
        };
        g.fillPolygon(xPointsWingRight, yPointsWingRight, 3);

        // Thruster (orange flame)
        g.setColor(Color.ORANGE);
        g.fillOval(x + WIDTH / 2 - 5, y + HEIGHT, 10, 8);
    }

    /**
     * Sets the ship’s x-coordinate (e.g., for mouse control),
     * clamping it within the horizontal bounds [0, windowWidth - WIDTH].
     *
     * @param x the desired x-coordinate
     */
    public void setX(int x) {
        if (x < 0) {
            this.x = 0;
        } else if (x > 480 - WIDTH) {
            this.x = 480 - WIDTH;
        } else {
            this.x = x;
        }
    }

    /**
     * Returns the bounding rectangle of the ship for collision detection.
     *
     * @return a Rectangle representing the ship’s bounds
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
