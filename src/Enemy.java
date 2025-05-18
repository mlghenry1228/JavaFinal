import java.awt.*;

/**
 * Represents an enemy alien ship that descends, lands, moves horizontally,
 * and fires bullets at randomized intervals.
 */
public class Enemy {
    /** Current x-coordinate of the enemy. */
    private int x;

    /** Current y-coordinate of the enemy. */
    private int y;

    /** Width of the enemy ship. */
    private int width = 40;

    /** Height of the enemy ship. */
    private int height = 20;

    /** Whether the enemy has finished descending and landed. */
    private boolean landed = false;

    /** Timer tracking time since last shot attempt (in milliseconds). */
    private int shootTimer = 0;

    /** Cooldown time before next shot attempt (randomized between 1000–1500 ms). */
    private int shootCooldown = 1000 + (int)(Math.random() * 500);

    /** Horizontal velocity: random direction and speed between 1–3 pixels/frame. */
    private int dx = (Math.random() < 0.5 ? -1 : 1) * (1 + (int)(Math.random() * 2));

    /** Number of bullets queued to fire (from external scheduling). */
    private int fireQueue = 0;

    /** Delay accumulator for queued firing (in milliseconds). */
    private int fireDelay = 0;

    /** Interval between firing queued bullets (in milliseconds). */
    private static final int FIRE_INTERVAL = 200;

    /**
     * Constructs an Enemy at the specified horizontal position, starting at the top of the screen.
     *
     * @param x the initial x-coordinate of the enemy
     */
    public Enemy(int x) {
        this.x = x;
        this.y = 0;
    }

    /**
     * Returns the current y-coordinate of the enemy.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Updates the enemy's position. Before landing, it descends at a fixed rate.
     * After landing, it moves horizontally and bounces off screen edges.
     */
    public void update() {
        if (!landed) {
            y += 3;
            if (y >= 100 + (int)(Math.random() * 100)) {
                landed = true;
            }
        } else {
            x += dx;
            if (x < 0 || x + width > 480) {
                dx = -dx;
                x += dx;
            }
        }
    }

    /**
     * Adds a specified number of bullets to the firing queue.
     *
     * @param count the number of bullets to schedule for firing
     */
    public void scheduleFire(int count) {
        fireQueue += count;
    }

    /**
     * Determines whether the enemy should start a shooting action based on its cooldown.
     * This method should be called each game tick (~16 ms).
     *
     * @return {@code true} if the cooldown has elapsed and shooting should start; {@code false} otherwise
     */
    public boolean shouldStartShooting() {
        shootTimer += 16;
        if (shootTimer >= shootCooldown) {
            shootTimer = 0;
            return true;
        }
        return false;
    }

    /**
     * Draws the enemy ship as a red irregular hexagon with black spikes and a white core.
     *
     * @param g the Graphics context on which to draw
     */
    public void draw(Graphics g) {
        // Main body
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

        // Spikes
        g.setColor(Color.BLACK);
        g.fillRect(x + width / 4 - 2, y + height, 4, 6);
        g.fillRect(x + 3 * width / 4 - 2, y + height, 4, 6);

        // Core
        g.setColor(Color.WHITE);
        g.fillOval(x + width / 4, y + height / 4, width / 2, height / 2);
    }

    /**
     * Returns the bounding rectangle of the enemy for collision detection.
     *
     * @return a Rectangle representing the enemy's bounds
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Processes the queued firing mechanism. Each call simulates a game tick (~16 ms).
     * When enough time has elapsed and bullets are queued, one bullet is fired.
     *
     * @return {@code true} if a bullet should be fired on this tick; {@code false} otherwise
     */
    public boolean updateFire() {
        if (fireQueue > 0) {
            fireDelay += 16;
            if (fireDelay >= FIRE_INTERVAL) {
                fireDelay = 0;
                fireQueue--;
                return true;
            }
        }
        return false;
    }
}
