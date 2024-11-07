package br.pucpr.estelar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import static java.awt.event.KeyEvent.*;

public class GameFrame extends JFrame {
    private final Set<Integer> keys = new TreeSet<>();

    private BufferedImage ship;
    private double x = 400;
    private boolean running = true;

    public GameFrame() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        setIgnoreRepaint(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                createBufferStrategy(2);
                new Thread(() -> gameLoop()).start();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys.remove(e.getKeyCode());
            }
        });
    }

    private void setup() throws Exception {
        ship = loadImage("destroyer.png");
    }

    private void update(double s) {
        if (isDown(VK_ESCAPE)) {
            running = false;
        }
        if (isDown(VK_RIGHT)) {
            x += 400 * s;
        } else if (isDown(VK_LEFT)) {
            x -= 400 * s;
        }
    }

    private void draw(Graphics2D g2d) {
        g2d.setBackground(Color.BLACK);
        g2d.clearRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.RED);
        g2d.drawImage(ship, (int)x, 400, null);
    }

    public void gameLoop() {
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        var time = 1L;
        while (running) {
            var before = System.currentTimeMillis();
            update(time / 1000.0);

            var g = (Graphics2D) getBufferStrategy().getDrawGraphics();
            draw(g);
            if (!getBufferStrategy().contentsLost()) {
                getBufferStrategy().show();
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Game interrupted");
                System.exit(1);
            }
            time = System.currentTimeMillis() - before;
        }
        EventQueue.invokeLater(this::dispose);
    }


    private boolean isDown(int key) {
        return keys.contains(key);
    }

    private BufferedImage loadImage(String name) throws IOException {
        var data = getClass().getResourceAsStream("/assets/image/" + name);
        if (data == null) {
            throw new IllegalArgumentException("Imagem " + name + " não encontrada!");
        }
        return ImageIO.read(data);
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new GameFrame().setVisible(true));
    }
}
