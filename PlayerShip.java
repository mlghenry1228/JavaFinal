import java.awt.*;
class PlayerShip {
    private int x, y;
    public static final int WIDTH = 40, HEIGHT = 20;

    public PlayerShip(int x, int y) {//建構飛船
        this.x = x;
        this.y = y;
    }
    public int getX() { return x; }//取得座標
    public int getY() { return y; }
    public void moveLeft(int bound) {//左移
        if (x > 0) x -= 10;
        if (x < 0) x = 0;
    }
    public void moveRight(int bound) {//右移
        if (x < bound - WIDTH) x += 10;
        if (x > bound - WIDTH) x = bound - WIDTH;
    }
    public void draw(Graphics g) {//畫飛船
        g.setColor(Color.GREEN);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
}

