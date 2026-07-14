package game.obj;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class HpRender {
    private final Hp hp;
    public HpRender(Hp hp) {
        this.hp = hp;
    }


    protected void hpRender(Graphics2D g2d, Shape shape, double y) {
        if(hp.getCurrentHp() != hp.getMAX_HP()) {
            double hpY = shape.getBounds().getY() - y - 15;
            g2d.setColor(new Color(70, 70, 70));
            g2d.fill(new Rectangle2D.Double(0, hpY, Player.PLAYER_SIZE, 2));
            g2d.setColor(new Color(253, 91, 91));
            double hpSize = hp.getCurrentHp() / hp.getMAX_HP() * Player.PLAYER_SIZE;
            g2d.fill(new Rectangle2D.Double(0, hpY, hpSize, 2));
        }
    }

    public boolean updateHp(double cutHp) {
        hp.setCurrentHp(hp.getCurrentHp() - cutHp);
        return !(hp.getCurrentHp() > 0);
    }

    public double getHp () {
        return hp.getCurrentHp();
    }

    public void resetHp() {
        hp.setCurrentHp(hp.getMAX_HP());
    }
}
