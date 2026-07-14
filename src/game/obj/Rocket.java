package game.obj;

import java.awt.*;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

public class Rocket extends HpRender {


    public Rocket() {
        super(new Hp(20, 20));
        this.image = new ImageIcon(getClass().getResource("/game/image/rocket.png")).getImage();
        Path2D path = new Path2D.Double();
        path.moveTo(0, ROCKET_SIZE/2);
        path.lineTo(15, 10);
        path.lineTo(ROCKET_SIZE-5, 13);
        path.lineTo(ROCKET_SIZE+10, ROCKET_SIZE/2);
        path.lineTo(ROCKET_SIZE-5, ROCKET_SIZE-13);
        path.lineTo(15, ROCKET_SIZE-10);
        rocketShape = new Area(path);
    }

    public static final double ROCKET_SIZE = 50;
    private double x;
    private double y;
    private final float speed = 0.3f;
    private float angle = 0;
    private final Image image;
    private final Area rocketShape;


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
        g2d.rotate(Math.toRadians(angle + 45), ROCKET_SIZE/2, ROCKET_SIZE/2);
        g2d.drawImage(image, at, null);
        Shape shape = getShape();
        hpRender(g2d, shape, y);
        g2d.setTransform(oldTransform);

        /* test
        g2d.setColor(Color.RED);
        g2d.draw(shape);
        g2d.draw(shape.getBounds()); */
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

    public Area getShape() {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(Math.toRadians(angle), ROCKET_SIZE/2, ROCKET_SIZE/2);
        return new Area(at.createTransformedShape(rocketShape));
    }

    public boolean check(int width, int height) {
        Rectangle size = getShape().getBounds();
        if (x<= -size.getWidth() || y< -size.getHeight() || x > width || y > height) {
            return false;
        }else {
            return true;
        }
    }

}
