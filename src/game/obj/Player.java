package game.obj;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.Objects;
import javax.swing.ImageIcon;

public class Player extends HpRender {

    public Player() {
        super(new Hp(50, 50));
        this.image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/game/image/plane.png"))).getImage();
        this.imageSpeed = new ImageIcon(Objects.requireNonNull(getClass().getResource("/game/image/planeSpeed.png"))).getImage();
        Path2D path = new Path2D.Double();
        path.moveTo(0, 15);
        path.lineTo(20, 5);
        path.lineTo(PLAYER_SIZE + 15, PLAYER_SIZE / 2);
        path.lineTo(20, PLAYER_SIZE - 5);
        path.lineTo(0, PLAYER_SIZE - 15);
        playerShape = new Area (path);
    }

    public static final double PLAYER_SIZE=64;
    private double x,y;
    private final float MAX_SPEED = 1f;
    private float speed = 0f;
    private float angle = 0f;
    private final Area playerShape;
    private final Image image;
    private final Image imageSpeed;
    private boolean speedUp;
    private boolean alive = true;

    public void changeLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
    }

    public void changeAngle(float angle) {
        if (angle < 0) {
            angle = 359;
        }
        else if(angle > 359) {
            angle = 0;
        }
        this.angle = angle;
    }

    public void draw(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(x, y);
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(angle+45), PLAYER_SIZE/2, PLAYER_SIZE/2);
        g2d.drawImage(speedUp? imageSpeed: image, at, null);
        hpRender(g2d, getShape(), y);
        g2d.setTransform(oldTransform);

        /* test
        g2d.setColor(Color.GREEN);
        g2d.draw(getShape());
        g2d.draw(getShape().getBounds()); */

    }

    public void speed_Up() {
        speedUp = true;
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }else {
            speed += 0.01f;
        }
    }

    public void speed_Down() {
        speedUp = false;
        if (speed <= 0) {
            speed = 0;
        }else {
            speed -= 0.003f;
        }
    }

    public Area getShape() {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(Math.toRadians(angle), PLAYER_SIZE/2, PLAYER_SIZE/2);
        return new Area(at.createTransformedShape(playerShape));
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public float getAngle() {
        return angle;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void reset() {
        alive = true;
        resetHp();
        angle = 0;
        speed = 0;
    }
}
