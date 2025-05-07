import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleStarWar extends JPanel implements ActionListener, KeyListener {
    // Player ship
    private PlayerShip ship;

    // Player bullets
    private List<Point> bullets = new ArrayList<>();

    // Enemy bullets
    private List<Point> enemyBullets = new ArrayList<>();

    // Enemies
    private List<Enemy> enemies = new ArrayList<>();

    // Enemy spawn timer (ms)
    private int spawnTimer = 0;

    // Score
    private int score = 0;

    // Player health points
    private int playerHP = 3;

    // Game over flag
    private boolean gameOver = false;

    // Timer for game loop
    private Timer timer;

    // Timer for continuous shooting (mouse)
    private Timer mouseShootTimer;

    // Timer for continuous shooting (spacebar)
    private Timer spaceShootTimer;

    private int highScore = 0;

    private boolean showLevelUp = false;
    private int levelUpTimer = 0;
    private int lastLevelShown = 0;

    private int enemyFireCountPerShot = 1;

    private boolean showMenu = true;  // 初始顯示主畫面
    private Rectangle startButton = new Rectangle(180, 220, 120, 40);  // 按鈕區域
    private Rectangle exitButton = new Rectangle(180, 280, 120, 40); // 離開按鈕
    private Rectangle restartButton = new Rectangle(150, 200, 180, 40);
    private Rectangle backToMenuButton = new Rectangle(150, 260, 180, 40);
    private Rectangle exitButtonGameOver = new Rectangle(150, 320, 180, 40);


    private int spawnCooldown = 250;      // 初始 0.25 秒
    


    public SimpleStarWar() {
        setPreferredSize(new Dimension(480, 500));
        setBackground(Color.BLACK);
        setFocusable(true); // Enable keyboard focus
        addKeyListener(this); // Register key listener

        ship = new PlayerShip(200, 450);

        // Mouse movement and drag for ship
        MouseMotionAdapter mouseMotion = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!gameOver) {
                    ship.setX(e.getX());
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!gameOver) {
                    ship.setX(e.getX());
                    repaint();
                }
            }
        };
        addMouseMotionListener(mouseMotion);

        // Mouse press and release for continuous shooting
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (showMenu) {
                    if (startButton.contains(e.getPoint())) {
                        showMenu = false;
                        repaint();
                        return;
                    }
                    if (exitButton.contains(e.getPoint())) {
                        System.exit(0); // 離開程式
                    }
                    return;
                }
                
                if (gameOver) {
                    if (restartButton.contains(e.getPoint())) {
                        resetGame();
                    } else if (backToMenuButton.contains(e.getPoint())) {
                        resetGame();
                        showMenu = true;
                        repaint();
                    } else if (exitButtonGameOver.contains(e.getPoint())) {
                        System.exit(0);
                    }
                    return;
                }
                if (!gameOver && e.getButton() == MouseEvent.BUTTON1) {
                    // Fire immediately
                    bullets.add(new Point(ship.getX() + PlayerShip.WIDTH / 2 - 2, ship.getY()));
                    // Start continuous shooting every 200ms
                    mouseShootTimer = new Timer(200, evt -> {
                        bullets.add(new Point(ship.getX() + PlayerShip.WIDTH / 2 - 2, ship.getY()));
                    });
                    mouseShootTimer.start();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && mouseShootTimer != null) {
                    mouseShootTimer.stop(); // Stop continuous shooting
                }
            }
        });

        // Game loop: update every 16 ms
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        
        if (showMenu) {
            // 畫主畫面標題
            String title = "星際大戰";
            Font titleFont = new Font("Microsoft JhengHei", Font.BOLD, 36);
            if (!titleFont.canDisplay('星')) {
                titleFont = new Font("Noto Sans TC", Font.BOLD, 36);
            }
            g.setFont(titleFont);
            FontMetrics fmTitle = g.getFontMetrics(titleFont);
            int titleWidth = fmTitle.stringWidth(title);
            int titleX = (getWidth() - titleWidth) / 2;
            g.setColor(Color.WHITE);
            g.drawString(title, titleX, 150);
        
            // 畫「開始遊戲」按鈕背景
            g.setColor(Color.GRAY);
            g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
        
            // 畫「開始遊戲」按鈕文字（置中）
            String btnText = "開始遊戲";
            Font btnFont = new Font("Microsoft JhengHei", Font.BOLD, 20);
            if (!btnFont.canDisplay('開')) {
                btnFont = new Font("Noto Sans TC", Font.BOLD, 20);
            }
            g.setFont(btnFont);
            FontMetrics fmBtn = g.getFontMetrics(btnFont);
            int textWidth = fmBtn.stringWidth(btnText);
            int textHeight = fmBtn.getAscent();
            int textX = startButton.x + (startButton.width - textWidth) / 2;
            int textY = startButton.y + (startButton.height + textHeight) / 2 - 4;
        
            g.setColor(Color.BLACK);
            g.drawString(btnText, textX, textY);

            // 畫離開遊戲按鈕背景
            g.setColor(Color.GRAY);
            g.fillRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);

            // 畫離開遊戲文字（置中）
            String exitText = "離開遊戲";
            Font exitFont = new Font("Microsoft JhengHei", Font.BOLD, 20);
            if (!exitFont.canDisplay('離')) {
                exitFont = new Font("Noto Sans TC", Font.BOLD, 20);
            }
            g.setFont(exitFont);
            FontMetrics fmExit = g.getFontMetrics(exitFont);
            int exitTextWidth = fmExit.stringWidth(exitText);
            int exitTextHeight = fmExit.getAscent();
            int exitTextX = exitButton.x + (exitButton.width - exitTextWidth) / 2;
            int exitTextY = exitButton.y + (exitButton.height + exitTextHeight) / 2 - 4;

            g.setColor(Color.BLACK);
            g.drawString(exitText, exitTextX, exitTextY);

            g.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("最高分：" + highScore, 10, getHeight() - 10);

            return;
        }
        
        if (gameOver) {
            g.setColor(Color.WHITE);
            Font titleFont = new Font("Microsoft JhengHei", Font.BOLD, 30);
            if (!titleFont.canDisplay('遊')) {
                titleFont = new Font("Noto Sans TC", Font.BOLD, 30);
            }
            g.setFont(titleFont);
            String message = "遊戲結束";
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (getWidth() - messageWidth) / 2, 140);
        
            Font buttonFont = new Font("Microsoft JhengHei", Font.BOLD, 20);
            if (!buttonFont.canDisplay('再')) {
                buttonFont = new Font("Noto Sans TC", Font.BOLD, 20);
            }
            g.setFont(buttonFont);
            g.setColor(Color.GRAY);
        
            // 畫 3 個按鈕
            g.fillRect(restartButton.x, restartButton.y, restartButton.width, restartButton.height);
            g.fillRect(backToMenuButton.x, backToMenuButton.y, backToMenuButton.width, backToMenuButton.height);
            g.fillRect(exitButtonGameOver.x, exitButtonGameOver.y, exitButtonGameOver.width, exitButtonGameOver.height);
        
            g.setColor(Color.BLACK);
            g.drawString("再來一場", restartButton.x + 50, restartButton.y + 25);
            g.drawString("返回主畫面", backToMenuButton.x + 35, backToMenuButton.y + 25);
            g.drawString("離開遊戲", exitButtonGameOver.x + 50, exitButtonGameOver.y + 25);
        
            g.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("得分：" + score + " 最高分：" + highScore, 140, 380);

            return;
        }
        
        if (showLevelUp) {
            g.setColor(Color.ORANGE);
            Font msgFont = new Font("Microsoft JhengHei", Font.BOLD, 28);
            if (!msgFont.canDisplay('難')) {
                msgFont = new Font("Noto Sans TC", Font.BOLD, 28);
            }
            g.setFont(msgFont);
            String msg = "難度提升！";
            int msgWidth = g.getFontMetrics().stringWidth(msg);
            g.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
        }
        
        // Draw player ship
        ship.draw(g);

        // Draw player bullets
        g.setColor(Color.YELLOW);
        for (Point b : bullets) {
            g.fillRect(b.x, b.y, 4, 10);
        }

        // Draw enemy bullets
        g.setColor(Color.RED);
        for (Point b : enemyBullets) {
            g.fillRect(b.x, b.y, 4, 10);
        }

        // Draw score and HP
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("HP: " + playerHP, 10, 40);

        // Draw enemies
        for (Enemy e : enemies) {
            e.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;
        if (showMenu || gameOver) return; // 主畫面或結束不執行遊戲邏輯
        // 每得 50 分降低冷卻（最小不能低於指定值）
        int level = score / 50;
        spawnCooldown = Math.max(80, 250 - level * 30);

        if (level > lastLevelShown) {
            showLevelUp = true;
            levelUpTimer = 0;
            lastLevelShown = level;
            enemyFireCountPerShot = level + 1; // 例如 level = 1 -> 發射 2 顆
        }
        

        

        // Spawn enemy every 0.25 second
        spawnTimer += 16;
        if (spawnTimer >= spawnCooldown) {
            enemies.add(new Enemy((int)(Math.random() * 440)));
            spawnTimer = 0;
        }

        // Enemy shooting 
        for (Enemy enemy : enemies) {
            // 1. 若符合冷卻時間，安排連續射擊 N 顆
            if (enemy.shouldStartShooting()) {
                enemy.scheduleFire(enemyFireCountPerShot);
            }
        
            // 2. 若這幀該發射其中一顆，則真的加子彈
            if (enemy.updateFire()) {
                enemyBullets.add(new Point(
                    enemy.getBounds().x + enemy.getBounds().width / 2 - 2,
                    enemy.getBounds().y + enemy.getBounds().height
                ));
            }
        }
        
        
        

        // Update player bullets
        Iterator<Point> bit = bullets.iterator();
        while (bit.hasNext()) {
            Point b = bit.next();
            b.y -= 8;
            if (b.y < 0) bit.remove();
        }

        // Update enemy bullets
        Iterator<Point> ebit = enemyBullets.iterator();
        while (ebit.hasNext()) {
            Point b = ebit.next();
            b.y += 8;
            if (b.y > getHeight()) ebit.remove();
        }

        // Update enemies
        for (Enemy enemy : enemies) {
            enemy.update();
        }

        // Remove enemies that move off-screen
        enemies.removeIf(enemy -> enemy.getY() > getHeight());

        // 如果場上敵人超過 15 個，直接 Game Over
        if (enemies.size() > 15) {
            gameOver = true;
            timer.stop();
            if (mouseShootTimer != null) mouseShootTimer.stop();
            if (spaceShootTimer != null) spaceShootTimer.stop();
            repaint();
            if (score > highScore) {
                highScore = score;
            }
            return;  // 不要再執行後面的碰撞與 repaint
        }

        // Check collisions: player bullets vs enemies
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
                    bit2.remove();
                    score++;
                    removed = true;
                    break;
                }
            }

            if (removed) continue;
        }

        // Check collisions: enemy bullets vs player
        Iterator<Point> ebit2 = enemyBullets.iterator();
        while (ebit2.hasNext()) {
            Point b = ebit2.next();
            Rectangle shot = new Rectangle(b.x, b.y, 4, 10);
            if (shot.intersects(ship.getBounds())) {
                ebit2.remove();
                playerHP -= 1;
                if (playerHP <= 0) {
                    playerHP = 0;
                    gameOver = true;
                    if (score > highScore) {
                        highScore = score;
                    }                
                    timer.stop();
                    if (mouseShootTimer != null) mouseShootTimer.stop();
                    if (spaceShootTimer != null) spaceShootTimer.stop();
                }
            }
        }
        if (showLevelUp) {
            levelUpTimer += 16;
            if (levelUpTimer >= 1000) { // 顯示 1 秒
                showLevelUp = false;
            }
        }
        
        repaint();
    }

    // Keyboard controls: left/right movement and spacebar for shooting
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) return;

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            ship.moveLeft(getWidth());
        } else if (code == KeyEvent.VK_RIGHT) {
            ship.moveRight(getWidth());
        } else if (code == KeyEvent.VK_SPACE) {
            // Fire immediately
            bullets.add(new Point(ship.getX() + PlayerShip.WIDTH / 2 - 2, ship.getY()));
            // Start continuous shooting every 500ms
            if (spaceShootTimer == null || !spaceShootTimer.isRunning()) {
                spaceShootTimer = new Timer(500, evt -> {
                    bullets.add(new Point(ship.getX() + PlayerShip.WIDTH / 2 - 2, ship.getY()));
                });
                spaceShootTimer.start();
            }
        }
    }

    private void resetGame() {
        score = 0;
        playerHP = 3;
        gameOver = false;
        bullets.clear();
        enemyBullets.clear();
        enemies.clear();
        ship.setX(200);
        spawnTimer = 0;
        if (mouseShootTimer != null) mouseShootTimer.stop();
        if (spaceShootTimer != null) spaceShootTimer.stop();
        timer.start();
        repaint();
    }
    
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && spaceShootTimer != null) {
            spaceShootTimer.stop(); // Stop continuous shooting
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Simple Star War - with Enemy Attacks");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new SimpleStarWar());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}