import java.awt.*;

public class Enemy {
    private int x, y;
    private int width = 40, height = 20;
    private boolean landed = false;

    public Enemy(int x) {
        this.x = x;
        this.y = 0; // 從上方掉下來
    }

    public int getY() {
        return y;
    }
    
    public void update() {
        if (!landed) {
            y += 3; // 敵人往下掉
            if (y >= 100 + (int)(Math.random() * 100)) {
                landed = true; // 停住
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // 你可以在未來加：發射子彈、死亡動畫等等
}
