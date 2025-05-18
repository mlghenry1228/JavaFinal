import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Represents a bullet that moves diagonally at a specified angle.
 */
public class DiagonalBullet {
    /**
     * Current x-coordinate of the bullet.
     */
    private double x;

    /**
     * Current y-coordinate of the bullet.
     */
    private double y;

    /**
     * Horizontal movement increment per frame.
     */
    private double dx;

    /**
     * Vertical movement increment per frame.
     */
    private double dy;

    /**
     * Constructs a new DiagonalBullet at the given position and moving at the given angle.
     *
     * @param x            the initial x-coordinate of the bullet
     * @param y            the initial y-coordinate of the bullet
     * @param angleDegree  the angle of movement in degrees (0 = right, 90 = up)
     */
    public DiagonalBullet(double x, double y, double angleDegree) {
        this.x = x;
        this.y = y;

        // Convert angle from degrees to radians
        double angleRad = Math.toRadians(angleDegree);

        // Compute velocity components (speed = 8 units/frame)
        dx = Math.cos(angleRad) * 8;
        dy = -Math.sin(angleRad) * 8;
    }

    /**
     * Updates the bullet's position by adding the velocity components.
     */
    public void move() {
        x += dx;
        y += dy;
    }

    /**
     * Checks whether the bullet has moved outside the given bounds.
     *
     * @param width   the width of the drawing area
     * @param height  the height of the drawing area
     * @return {@code true} if the bullet is outside [0, width]×[0, height]; {@code false} otherwise
     */
    public boolean isOutOfBounds(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    /**
     * Draws the bullet as a filled rectangle on the given Graphics context.
     *
     * @param g  the Graphics context on which to draw
     */
    public void draw(Graphics g) {
        g.fillRect((int) x, (int) y, 4, 10);
    }

    /**
     * Returns the bounding rectangle of the bullet, used for collision detection.
     *
     * @return a Rectangle representing the bullet's bounds
     */
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 4, 10);
    }
}
