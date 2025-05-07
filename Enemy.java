import java.awt.*;

public class Enemy {
    private int x, y;
    private int width = 40, height = 20;
    private boolean landed = false;
    private int shootTimer = 0;
    private int shootCooldown = 1000 + (int)(Math.random() * 500); // 每隻間隔不同
    private int dx = (Math.random() < 0.5 ? -1 : 1) * (1 + (int)(Math.random() * 2)); // -2~2
    private int fireQueue = 0;           // 還要發幾顆
    private int fireDelay = 0;           // 間隔計時器
    private static final int FIRE_INTERVAL = 200; // 每顆間隔 200ms


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
        } else {
            // After landing, move left/right
            x += dx;

            // Bounce if hit screen edge
            if (x < 0 || x + width > 480) {
                dx = -dx;
                x += dx; // Prevent sticking
            }
        }
    }

    public void scheduleFire(int count) {
        fireQueue += count;
    }
    
    // 是否要開始一輪射擊（定時觸發）
    public boolean shouldStartShooting() {
        shootTimer += 16;
        if (shootTimer >= shootCooldown) {
            shootTimer = 0;
            return true;
        }
        return false;
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
    public boolean updateFire() {
        if (fireQueue > 0) {
            fireDelay += 16;
            if (fireDelay >= FIRE_INTERVAL) {
                fireDelay = 0;
                fireQueue--;
                return true; // 這一輪要發射
            }
        }
        return false; // 這一輪不發
    }
    
    
}