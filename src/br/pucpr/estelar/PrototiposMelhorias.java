package br.pucpr.estelar;

import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

import static java.awt.event.KeyEvent.*;

// Classe principal com melhorias aplicadas
public class GameFrame extends JFrame {
    private final Set<Integer> keys = new TreeSet<>();
    private BufferedImage ship;
    private double x = 400;
    private boolean running = true;
    private MovementStrategy movementStrategy;
    private final GameConfig config;

    public GameFrame(GameConfig config) {
        this.config = config;
        this.movementStrategy = new SimpleMovement();

        setSize(config.getWidth(), config.getHeight());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        setIgnoreRepaint(true);
        
        // Configurações de janela e inicialização do loop de jogo
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                createBufferStrategy(2);
                new Thread(() -> gameLoop()).start();
            }
        });
        
        // Adiciona o controlador de teclado e registro de eventos
        KeyboardHandler.getInstance().addListener(new KeyAdapter() {
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
        ship = ResourceManager.getInstance().loadImage("/assets/image/destroyer.png");
    }

    private void update(double s) {
        if (isDown(VK_ESCAPE)) {
            running = false;
        }
        movementStrategy.move(s, this);  // Usa a estratégia de movimentação
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

    public static void main(String[] args) {
        GameConfig config = GameConfigFactory.createDefaultConfig();
        EventQueue.invokeLater(() -> new GameFrame(config).setVisible(true));
    }

    public int getX() {
    return (int) x;  // Converte de double para int
    }


    public void setX(double x) {
        this.x = x;
    }

    public boolean isMovingRight() {
        return keys.contains(VK_RIGHT);
    }

    public boolean isMovingLeft() {
        return keys.contains(VK_LEFT);
    }
}

// Singleton para Gerenciamento de Recursos
class ResourceManager {
    private static ResourceManager instance;
    private final Map<String, BufferedImage> images = new HashMap<>();

    private ResourceManager() {}

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public BufferedImage loadImage(String path) {
        return images.computeIfAbsent(path, this::loadImageInternal);
    }

    private BufferedImage loadImageInternal(String path) {
        try {
            var data = getClass().getResourceAsStream(path);
            if (data == null) {
                throw new IllegalArgumentException("Imagem " + path + " não encontrada!");
            }
            return ImageIO.read(data);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar a imagem: " + path, e);
        }
    }
}

// Interface Strategy para Movimentação
interface MovementStrategy {
    void move(double deltaTime, GameFrame game);
}

// Implementação SimpleMovement
class SimpleMovement implements MovementStrategy {
    @Override
    public void move(double deltaTime, GameFrame game) {
        if (game.isMovingRight()) {
            game.setX(game.getX() + 400 * deltaTime);
        } else if (game.isMovingLeft()) {
            game.setX(game.getX() - 400 * deltaTime);
        }
    }
}

// Singleton para Gerenciamento de Eventos de Teclado (Observer)
class KeyboardHandler {
    private static KeyboardHandler instance = new KeyboardHandler();
    private Set<KeyListener> listeners = new HashSet<>();

    private KeyboardHandler() {}

    public static KeyboardHandler getInstance() {
        return instance;
    }

    public void addListener(KeyListener listener) {
        listeners.add(listener);
    }

    public void handleKeyPress(KeyEvent event) {
        listeners.forEach(listener -> listener.keyPressed(event));
    }
}

// Template Method para Ciclo de Jogo
abstract class GameLoop {
    protected abstract void update(double deltaTime);
    protected abstract void render(Graphics2D g2d);

    public void start() {
        while (true) {
            double deltaTime = computeDeltaTime();
            update(deltaTime);
            render(getGraphics());
            sleep();
        }
    }

    private double computeDeltaTime() {
        // Implementa a lógica para calcular o tempo delta entre frames
        return 0.016; // Exemplo de valor fixo
    }

    private Graphics2D getGraphics() {
        // Obtém o gráfico para renderização
        return (Graphics2D) new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).getGraphics();
    }

    private void sleep() {
        // Pausa para manter a taxa de atualização fixa
    }
}

// Configuração e Fabricação de Configurações (Factory)
class GameConfig {
    private final int width;
    private final int height;
    private final double shipSpeed;

    public GameConfig(int width, int height, double shipSpeed) {
        this.width = width;
        this.height = height;
        this.shipSpeed = shipSpeed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getShipSpeed() {
        return shipSpeed;
    }
}

class GameConfigFactory {
    public static GameConfig createDefaultConfig() {
        return new GameConfig(800, 600, 400); // Exemplo de valores padrão
    }
}
