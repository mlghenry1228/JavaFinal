import java.awt.*;

public class PlayerShip {
    private int x, y;
    public static final int WIDTH = 40, HEIGHT = 20;

    // Construct ship at given position
    public PlayerShip(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Get X coordinate
    public int getX() { return x; }

    // Get Y coordinate
    public int getY() { return y; }

    // Move ship left within bounds
    public void moveLeft(int bound) {
        if (x > 0) x -= 10;
        if (x < 0) x = 0;
    }

    // Move ship right within bounds
    public void moveRight(int bound) {
        if (x < bound - WIDTH) x += 10;
        if (x > bound - WIDTH) x = bound - WIDTH;
    }

    // Draw ship as a detailed spaceship with wings and thruster
    public void draw(Graphics g) {
        // Main body (green pointed fuselage)
        g.setColor(Color.GREEN);
        int[] xPointsBody = {x + WIDTH / 2, x, x + WIDTH};
        int[] yPointsBody = {y, y + HEIGHT, y + HEIGHT};
        g.fillPolygon(xPointsBody, yPointsBody, 3);

        // Wings (white triangular details)
        g.setColor(Color.WHITE);
        int[] xPointsWingLeft = {x + WIDTH / 4, x, x + WIDTH / 4};
        int[] yPointsWingLeft = {y + HEIGHT, y + HEIGHT / 2, y + HEIGHT / 2};
        g.fillPolygon(xPointsWingLeft, yPointsWingLeft, 3);
        int[] xPointsWingRight = {x + 3 * WIDTH / 4, x + WIDTH, x + 3 * WIDTH / 4};
        int[] yPointsWingRight = {y + HEIGHT, y + HEIGHT / 2, y + HEIGHT / 2};
        g.fillPolygon(xPointsWingRight, yPointsWingRight, 3);

        // Thruster (orange flame at bottom)
        g.setColor(Color.ORANGE);
        g.fillOval(x + WIDTH / 2 - 5, y + HEIGHT, 10, 8);
    }

    // Set X position for mouse control
    public void setX(int x) {
        if (x < 0) this.x = 0;
        else if (x > 480 - WIDTH) this.x = 480 - WIDTH; // 480 is window width
        else this.x = x;
    }
}