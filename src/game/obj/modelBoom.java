package game.obj;

public class modelBoom {

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public  modelBoom(double size, float angle) {
        this.size = size;
        this.angle = angle;
    }

    public modelBoom() {
    }

    private double size;
    private double angle;
}
