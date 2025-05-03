import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

    // Enemy shooting timer (ms)
    private int enemyShootTimer = 0;

    // Score
    private int score = 0;

    // Player health points
    private int playerHP = 100;

    // Game over flag
    private boolean gameOver = false;

    // Timer for game loop
    private Timer timer;

    // Timer for continuous shooting (mouse)
    private Timer mouseShootTimer;

    // Timer for continuous shooting (spacebar)
    private Timer spaceShootTimer;

    // Random for enemy shooting
    private Random random = new Random();

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

        if (gameOver) {
            // Draw game over screen
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String message = "Game Over";
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (getWidth() - messageWidth) / 2, getHeight() / 2);
            return;
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

        // Spawn enemy every second
        spawnTimer += 16;
        if (spawnTimer >= 1000) {
            enemies.add(new Enemy((int)(Math.random() * 440)));
            spawnTimer = 0;
        }

        // Enemy shooting every 2 seconds
        enemyShootTimer += 16;
        if (enemyShootTimer >= 2000 && !enemies.isEmpty()) {
            Enemy shooter = enemies.get(random.nextInt(enemies.size()));
            enemyBullets.add(new Point(shooter.getBounds().x + shooter.getBounds().width / 2 - 2, shooter.getBounds().y + shooter.getBounds().height));
            enemyShootTimer = 0;
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
                playerHP -= 20;
                if (playerHP <= 0) {
                    playerHP = 0;
                    gameOver = true;
                    timer.stop();
                    if (mouseShootTimer != null) mouseShootTimer.stop();
                    if (spaceShootTimer != null) spaceShootTimer.stop();
                }
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