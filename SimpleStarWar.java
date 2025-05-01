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
    List<Enemy> enemies = new ArrayList<>();

    private int spawnTimer = 0;



    // 分數
    private int score = 0;

    private Timer timer;

    public SimpleStarWar() {
        setPreferredSize(new Dimension(480, 500));
        setBackground(Color.BLACK);
        setFocusable(true);//這個 SimpleStarWar 面板要負責監聽鍵盤事件
        addKeyListener(this);//Java GUI 元件預設是不能接收鍵盤事件的要先設定可以 focus，才會收到鍵盤事件

        ship = new PlayerShip(200, 450);

        //用滑鼠移動
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                ship.setX(e.getX());
                repaint();
            }
        });
        //用左鍵射擊
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                bullets.add(new Point(ship.getX() + PlayerShip.WIDTH / 2 - 2, ship.getY()));
            }
        });
       

        

        

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

        // 畫分數
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);

        // 畫出敵人
        for (Enemy e : enemies) {
            e.draw(g);
        }
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        // 1. 每秒新增一個敵人
        spawnTimer += 16;
        if (spawnTimer >= 1000) {
            enemies.add(new Enemy((int)(Math.random() * 440)));
            spawnTimer = 0;
        }

        // 2. 更新子彈
        Iterator<Point> bit = bullets.iterator();
        while (bit.hasNext()) {
            Point b = bit.next();
            b.y -= 8;
            if (b.y < 0) bit.remove();
        }

        // 3. 更新敵人
        for (Enemy enemy : enemies) {
            enemy.update();
        }

        // 4. 清除超出底部的敵人
        enemies.removeIf(enemy -> enemy.getY() > getHeight());

        // 5. 碰撞檢查：子彈 vs 敵人
        Iterator<Enemy> eit = enemies.iterator();
        while (eit.hasNext()) {
            Enemy enemy = eit.next();
            boolean removed = false;

            Iterator<Point> bit2 = bullets.iterator();
            while (bit2.hasNext()) {
                Point b = bit2.next();
                Rectangle shot = new Rectangle(b.x, b.y, 4, 10);
                if (shot.intersects(enemy.getBounds())) {
                    eit.remove();
                    bit2.remove();  // ✅ 用正確的迭代器
                    score++;
                    removed = true;
                    break;
                }
            }

            if (removed) continue;
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
