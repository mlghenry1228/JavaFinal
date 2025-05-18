import java.awt.Graphics;
import java.awt.Rectangle;

public class DiagonalBullet {
    double x, y;
    double dx, dy;

    public DiagonalBullet(double x, double y, double angleDegree) {
        this.x = x;
        this.y = y;

        // 轉弧度
        double angleRad = Math.toRadians(angleDegree);

        dx = Math.cos(angleRad) * 8;
        dy = -Math.sin(angleRad) * 8;  // Y 軸向下，這裡要反向
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public boolean isOutOfBounds(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    public void draw(Graphics g) {
        g.fillRect((int)x, (int)y, 4, 10);
    }

    public Rectangle getBounds() {//取得子彈碰撞範圍
        return new Rectangle((int)x, (int)y, 4, 10);
    }
}

