import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleStarWar extends JPanel implements ActionListener, KeyListener {
    // 玩家
    private PlayerShip ship;

    // 子彈
    private List<Point> bullets = new ArrayList<Point>();

    // 敵人
    private List<Rectangle> enemies = new ArrayList<Rectangle>();
    private int enemySpeed = 2;           // 整體水平速度
    private static final int DROP_DIST = 10; // 每次下移量

    // 分數
    private int score = 0;

    private Timer timer;

    public SimpleStarWar() {
        setPreferredSize(new Dimension(480, 500));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        ship = new PlayerShip(200, 450);

        // 初始化敵人：4 行 8 列，每格 40×20，間隔 10 px
        int rows = 4, cols = 8;
        int w = 40, h = 20, paddingX = 10, paddingY = 10;
        int startX = 30, startY = 30;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = startX + c * (w + paddingX);
                int y = startY + r * (h + paddingY);
                enemies.add(new Rectangle(x, y, w, h));
            }
        }

        // 遊戲循環：每 16 ms 更新一次
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 畫飛船
        ship.draw(g);

        // 畫子彈
        g.setColor(Color.YELLOW);
        for (Point b : bullets) {
            g.fillRect(b.x, b.y, 4, 10);
        }

        // 畫敵人
        g.setColor(Color.RED);
        for (Rectangle e : enemies) {
            g.fillRect(e.x, e.y, e.width, e.height);
        }

        // 畫分數
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 1. 更新子彈：往上移，出界就移除
        Iterator<Point> bit = bullets.iterator();
        while (bit.hasNext()) {
            Point b = bit.next();
            b.y -= 8;
            if (b.y < 0) {
                bit.remove();
            }
        }

        // 2. 更新敵人位置：水平移動，碰邊就反向並下移
        boolean hitEdge = false;
        for (Rectangle enemy : enemies) {
            enemy.x += enemySpeed;
            if (enemy.x < 0 || enemy.x + enemy.width > getWidth()) {
                hitEdge = true;
            }
        }
        if (hitEdge) {
            enemySpeed = -enemySpeed;
            for (Rectangle enemy : enemies) {
                enemy.y += DROP_DIST;
            }
        }

        // 3. 碰撞檢測：子彈 vs 敵人
        Iterator<Rectangle> eit = enemies.iterator();
        while (eit.hasNext()) {
            Rectangle enemy = eit.next();
            boolean removed = false;
            Iterator<Point> bit2 = bullets.iterator();
            while (bit2.hasNext()) {
                Point b = bit2.next();
                Rectangle shot = new Rectangle(b.x, b.y, 4, 10);
                if (shot.intersects(enemy)) {
                    // 碰撞：移除敵人與子彈，+1 分
                    eit.remove();
                    bit2.remove();
                    score += 1;
                    removed = true;
                    break;
                }
            }
            if (removed) {
                // 如果這個敵人已被移除，不用再檢查它
                continue;
            }
        }

        repaint();
    }

    // 鍵盤控制：左右移動 + 空白鍵發射
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            ship.moveLeft(getWidth());
        } else if (code == KeyEvent.VK_RIGHT) {
            ship.moveRight(getWidth());
        } else if (code == KeyEvent.VK_SPACE) {
            bullets.add(new Point(ship.getX() + PlayerShip.WIDTH / 2 - 2, ship.getY()));
        }
    }
    @Override public void keyReleased(KeyEvent e) { }
    @Override public void keyTyped(KeyEvent e) { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Simple Star War - with PlayerShip class");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new SimpleStarWar());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
