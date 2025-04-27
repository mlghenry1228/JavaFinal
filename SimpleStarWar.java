import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleStarWar extends JPanel implements ActionListener, KeyListener {
    // Ship position
    private int shipX = 200, shipY = 450;
    private final int SHIP_WIDTH = 40, SHIP_HEIGHT = 20;
    private static final int SHIP_SPEED = 10; // Ship movement speed
    private Timer timer;

    public SimpleStarWar() {
        setPreferredSize(new Dimension(480, 500));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        // Game loop: update every 16 ms
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw ship
        g.setColor(Color.GREEN);
        g.fillRect(shipX, shipY, SHIP_WIDTH, SHIP_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Repaint screen
        repaint();
    }

    // Handle keyboard events
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // Left and right movement
        if (code == KeyEvent.VK_LEFT && shipX > 0) {
            shipX -= SHIP_SPEED;
        } else if (code == KeyEvent.VK_RIGHT && shipX < getWidth() - SHIP_WIDTH) {
            shipX += SHIP_SPEED;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
    @Override
    public void keyTyped(KeyEvent e) { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Simple Star War");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new SimpleStarWar());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}