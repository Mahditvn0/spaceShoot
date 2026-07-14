package game.obj;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Effect {
    private final double x;
    private final double y;
    private final double maxDistance;
    private final int maxSize;
    private final Color color;
    private final int totalEffect;
    private final float speed;
    private double currentDistance;
    private modelBoom[] boom;
    private float alpha = 1f;

    public Effect(double x, double y, int totalEffect, int maxSize, double maxDistance, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.maxDistance = maxDistance;
        this.maxSize = maxSize;
        this.color = color;
        this.totalEffect = totalEffect;
        this.speed = speed;
        createRandom();
    }

    private void createRandom() {
        boom = new modelBoom[totalEffect];
        float per = 360f / totalEffect;
        Random rand = new Random();
        for (int i = 1; i <= totalEffect; i++) {
            int r =  rand.nextInt((int)per) + 1;
            int boomSize = rand.nextInt(maxSize) + 1;
            float angle = i * per + r;
            boom[i - 1] = new modelBoom(boomSize,angle);
        }
    }

    public void draw(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();
        Composite oldComposite = g2d.getComposite();
        g2d.setColor(color);
        g2d.translate(x, y);
        for (modelBoom boom : boom) {
            double bx = Math.cos(Math.toRadians(boom.getAngle())) * currentDistance;
            double by = Math.sin(Math.toRadians(boom.getAngle())) * currentDistance;
            double boomSize = boom.getSize();
            double space = boomSize / 2;
            if (currentDistance >= maxDistance - (maxDistance * 0.7f)){
                alpha = (float) ((maxDistance - currentDistance) - (maxDistance * 0.7f));
            }
            if (alpha > 1) {
                alpha = 1;
            } else if (alpha < 0) {
                alpha = 0;
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.fill(new Rectangle2D.Double(bx - space, by - space, boomSize, boomSize));
        }
        g2d.setComposite(oldComposite);
        g2d.setTransform(oldTransform);
    }

    public void update() {
        currentDistance += speed;
    }

    public boolean check() {
        return currentDistance < maxDistance;
    }


}
