package game.obj;

public class Hp {
    private double MAX_HP;
    private double currentHp;

    public double getMAX_HP() {
        return MAX_HP;
    }

    public void setMaxHp(double maxHp) {
        this.MAX_HP = maxHp;
    }

    public double getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(double currentHp) {
        this.currentHp = currentHp;
    }

    public Hp(double MAX_HP, double currentHp) {
        this.MAX_HP = MAX_HP;
        this.currentHp = currentHp;
    }

    public Hp() {

    }
}
