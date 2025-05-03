import java.awt.*;

public class Enemy {
    private int x, y;
    private int width = 40, height = 20;
    private boolean landed = false;

    // Construct enemy at given X position
    public Enemy(int x) {
        this.x = x;
        this.y = 0; // Start from top
    }

    // Get Y position
    public int getY() {
        return y;
    }

    // Update enemy position
    public void update() {
        if (!landed) {
            y += 3; // Move downward
            if (y >= 100 + (int)(Math.random() * 100)) {
                landed = true; // Stop at random height
            }
        }
    }

    // Draw enemy as an alien ship with spikes and core
    public void draw(Graphics g) {
        // Main body (red irregular hexagon)
        g.setColor(Color.RED);
        int[] xPointsBody = {
            x + width / 2, x, x + width / 4,
            x + width / 2, x + 3 * width / 4, x + width
        };
        int[] yPointsBody = {
            y, y + height / 2, y + height,
            y + height, y + height, y + height / 2
        };
        g.fillPolygon(xPointsBody, yPointsBody, 6);

        // Spikes (black protrusions)
        g.setColor(Color.BLACK);
        g.fillRect(x + width / 4 - 2, y + height, 4, 6); // Bottom left spike
        g.fillRect(x + 3 * width / 4 - 2, y + height, 4, 6); // Bottom right spike

        // Core (white central circle)
        g.setColor(Color.WHITE);
        g.fillOval(x + width / 4, y + height / 4, width / 2, height / 2);
    }

    // Get bounds for collision detection
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}