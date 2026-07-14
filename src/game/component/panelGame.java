package game.component;


import game.obj.Bullet;
import game.obj.Effect;
import game.obj.Player;
import game.obj.Rocket;
import game.obj.sound.Sound;

import javax.swing.JComponent;
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class panelGame extends JComponent {

    //Game FPs
    private static final int FPS = 60;
    private final int TARGET_TIME = 1_000_000_000 / FPS;
    // Game Object
    private Sound sound;
    private Player player;
    private List<Bullet> bullets;
    private List<Rocket> rockets;
    private List<Effect>  effects;
    private int score = 0;

    private int width, height;
    private BufferedImage image;
    private Graphics2D g2d;
    private final boolean start = true;
    private Key key;
    private int shotTime;

    public void start() {
        width = getWidth();
        height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    long startTime = System.nanoTime();

                    drawBackground();
                    drawGame();
                    render();

                    long time = System.nanoTime() - startTime;

                    if (time < TARGET_TIME) {
                        long sleep = (TARGET_TIME - time) / 1_000_000;
                        sleep(sleep);
                    }

                }
            }
        });
        initObject();
        initKeyboard();
        initBullets();
        thread.start();
    }

    public void addRocket() {
        Random random = new Random();
        int locationY= random.nextInt(height-50)+25;
        Rocket rocket = new Rocket();
        rocket.changeLocation(0, locationY);
        rocket.changeAngle(0);
        rockets.add(rocket);
        int locationY2= random.nextInt(height-50)+25;
        Rocket rocket2 = new Rocket();
        rocket2.changeLocation(width, locationY2);
        rocket2.changeAngle(180);
        rockets.add(rocket2);
    }

    private void initObject(){
        sound = new Sound();
        player = new Player();
        player.changeLocation(150,150);
        rockets = new ArrayList<>();
        effects = new ArrayList<>();
        new Thread(new Runnable()  {
            @Override
            public void run() {
                while (start) {
                    addRocket();
                    sleep(3000);
                }
            }
        }
        ).start();
    }

    private void resetGame(){
        score = 0;
        rockets.clear();
        bullets.clear();
        player.changeLocation(150,150);
        player.reset();

    }

    private void initKeyboard() {
        key = new Key();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKey_left(true);
                }else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKey_right(true);

                }else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKey_space(true);
                }else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKey_j(true);
                }else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKey_k(true);
                }else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(true);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKey_left(false);
                }else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKey_right(false);
                }else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKey_space(false);
                }else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKey_j(false);
                }else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKey_k(false);
                }else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(false);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                float s = 0.5f;
                while (start) {
                    if(player.isAlive()){
                        float angle = player.getAngle();
                        if (key.isKey_left()) {
                            angle -= s;
                        }
                        if (key.isKey_right()) {
                            angle += s;
                        }
                        if (key.isKey_j()||key.isKey_k()) {
                            if(shotTime==0){
                                if(key.isKey_j()){
                                    bullets.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 5, 3f));
                                }else{
                                    bullets.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 20, 3f));
                                }
                                sound.soundShoot();
                            }
                            shotTime++;
                            if(shotTime== 15){
                                shotTime = 0;
                            }
                        }else {
                            shotTime = 0;
                        }
                        if  (key.isKey_space()) {
                            player.speed_Up();
                        }else {
                            player.speed_Down();
                        }
                        player.update();
                        player.changeAngle(angle);
                    } else {
                        if(key.isKey_enter()) {
                            resetGame();
                        }
                    }
                    for (int i = 0; i < rockets.size(); i++) {
                        Rocket rocket = rockets.get(i);
                        if (rocket != null) {
                            rocket.update();
                            if (!rocket.check(width, height)) {
                                rockets.remove(rocket);
                            } else {
                                if(player.isAlive()){
                                    checkPlayer(rocket);
                                }
                            }
                        }
                    }
                    sleep(5);
                }
            }
        }).start();
    }

    private void checkBullets(Bullet bullet) {
        for (int i = 0; i < rockets.size(); i++) {
            Rocket rocket = rockets.get(i);
            if (rocket != null) {
                Area area = new Area(bullet.getShape());
                area.intersect(rocket.getShape());
                if (!area.isEmpty()) {
                    effects.add(new Effect(bullet.getCenterX(), bullet.getCenterY(),3, 5, 60, 0.5f, new Color(230, 207, 105)));
                    if (rocket.updateHp(bullet.getSize())){
                        score++;
                        rockets.remove(rocket);
                        sound.soundDestroy();
                        double x= rocket.getX() + Rocket.ROCKET_SIZE /2;
                        double y= rocket.getY() + Rocket.ROCKET_SIZE /2;
                        effects.add(new Effect(x, y,5, 5, 75, 0.05f, new Color(32, 108, 255)));
                        effects.add(new Effect(x, y,5, 5, 75, 0.1f, new Color(32, 178, 169)));
                        effects.add(new Effect(x, y,10, 10, 100, 0.3f, new Color(230, 207, 105)));
                        effects.add(new Effect(x, y,10, 5, 100, 0.5f, new Color(255, 70, 70)));
                        effects.add(new Effect(x, y,10, 5, 150, 0.2f, new Color(255, 255, 255)));

                    } else {
                        sound.soundHit();
                    }
                    bullets.remove(bullet);
                }
            }
        }
    }

    private void checkPlayer(Rocket rocket) {
        if (rocket != null) {
            Area area = new Area(player.getShape());
            area.intersect(rocket.getShape());
            if (!area.isEmpty()) {
                double rocketHp = rocket.getHp();
                if (rocket.updateHp(player.getHp())) {
                    rockets.remove(rocket);
                    sound.soundDestroy();
                    double x = rocket.getX() + Rocket.ROCKET_SIZE / 2;
                    double y = rocket.getY() + Rocket.ROCKET_SIZE / 2;
                    effects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 108, 255)));
                    effects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                    effects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                    effects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                    effects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));

                }
                if (player.updateHp(rocketHp)) {
                    player.setAlive(false);
                    sound.soundDestroy();
                    double x = rocket.getX() + Rocket.ROCKET_SIZE / 2;
                    double y = rocket.getY() + Rocket.ROCKET_SIZE / 2;
                    effects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 108, 255)));
                    effects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                    effects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                    effects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                    effects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));

                }
            }
        }
    }

    private void initBullets() {
        bullets = new ArrayList<>();
        new Thread(new Runnable()  {
            @Override
            public void run() {
                while (start) {
                    for (int i = 0; i < bullets.size(); i++) {
                        Bullet bullet = bullets.get(i);
                        if (bullet != null) {
                            bullet.update();
                            checkBullets(bullet);
                            if (!bullet.check(width, height)) {
                                bullets.remove(bullet);
                            }
                        } else {
                            bullets.remove(bullet);
                        }
                    }
                    for (int i = 0; i < effects.size(); i++) {
                        Effect e = effects.get(i);
                        if (e != null) {
                            e.update();
                            if (!e.check()) {
                                effects.remove(e);
                            }
                        } else {
                            effects.remove(e);
                        }
                    }
                    sleep(1);
                }
            }
        }).start();
    }

    private void drawBackground() {
        g2d.setColor(new Color(30, 30, 30));
        g2d.fillRect(0, 0, width, height);
    }

    private void drawGame() {
        if(player.isAlive()){
            player.draw(g2d);
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet != null){
                bullet.draw(g2d);
            }
        }
        for (int i = 0; i < rockets.size(); i++) {
            Rocket rocket = rockets.get(i);
            if (rocket != null){
                rocket.draw(g2d);
            }
        }
        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            if (effect != null) {
                effect.draw(g2d);
            }
        }
        g2d.setColor(Color.WHITE);
        g2d.setFont(getFont().deriveFont( Font.BOLD, 15f));
        g2d.drawString("Score: " + score, 10, 20);
        if (!player.isAlive()) {
            String text = "GAME OVER";
            String textKey = "Press Key Enter TO Continue";
            g2d.setFont(getFont().deriveFont(Font.BOLD, 50f));
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D r2 = fm.getStringBounds(text, g2d);
            double textWith = r2.getWidth();
            double textHeight = r2.getHeight();
            double x = (width - textWith) / 2;
            double y = (height - textHeight) / 2;
            g2d.drawString(text, (int) x, (int) y + fm.getAscent());
            g2d.setFont(getFont().deriveFont(Font.BOLD, 15f));
            fm = g2d.getFontMetrics();
            r2 = fm.getStringBounds(text, g2d);
            textWith = r2.getWidth();
            textHeight = r2.getHeight();
            x = (width - textWith) / 2;
            y = (height - textHeight) / 2;
            g2d.drawString(textKey, (int) x, (int) y + fm.getAscent() + 50);
        }
    }

    private void render() {
        repaint();
        Graphics  g = getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    private void sleep(long speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }
}
