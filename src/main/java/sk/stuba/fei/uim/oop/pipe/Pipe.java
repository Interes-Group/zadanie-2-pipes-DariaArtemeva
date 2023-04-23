package sk.stuba.fei.uim.oop.pipe;



import java.awt.*;

public abstract class Pipe {
    protected int x;
    protected int y;
    protected int rotation;
    protected boolean correct;
    private boolean locked = false;

    public Pipe(int x, int y, int rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point getPoint() { return new Point(x, y); }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public boolean getCorrect() {return correct; }

    public void setCorrect(boolean correct) { this.correct = correct; }

    public void rotate() {
        if (!locked) {
            rotation = (rotation + 90) % 360;
        }
    }


    public abstract void draw(Graphics g, int cellSize, Color color);

    public abstract boolean isConnectedTo(Point otherPoint);

}
