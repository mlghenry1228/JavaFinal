import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

//test123

public class SimpleStarWar extends JPanel implements ActionListener, KeyListener {
    // 飛船位置
    private int shipX = 200, shipY = 450;
    private final int SHIP_WIDTH = 40, SHIP_HEIGHT = 20;
    // 子彈
    private java.util.List<Point> bullets = new ArrayList<>();
    private Timer timer;

    public SimpleStarWar() {
        setPreferredSize(new Dimension(480, 500));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        // 遊戲循環：每 16 ms 更新一次
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 畫飛船
        g.setColor(Color.GREEN);
        g.fillRect(shipX, shipY, SHIP_WIDTH, SHIP_HEIGHT);
        // 畫子彈
        g.setColor(Color.YELLOW);
        for (Point b : bullets) {
            g.fillRect(b.x, b.y, 4, 10);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 更新子彈位置：往上移動
        Iterator<Point> it = bullets.iterator();
        while (it.hasNext()) {
            Point b = it.next();
            b.y -= 8;
            if (b.y < 0) {
                it.remove();
            }
        }
        repaint();
    }

    // 處理鍵盤事件
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // 左右移動
        if (code == KeyEvent.VK_LEFT && shipX > 0) {
            shipX -= 10;
        } else if (code == KeyEvent.VK_RIGHT && shipX < getWidth() - SHIP_WIDTH) {
            shipX += 10;
        }
        // 空白鍵發射
        else if (code == KeyEvent.VK_SPACE) {
            bullets.add(new Point(shipX + SHIP_WIDTH/2 - 2, shipY));
        }
    }

    @Override public void keyReleased(KeyEvent e) { }
    @Override public void keyTyped(KeyEvent e) { }

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
