package sk.stuba.fei.uim.oop.pipe;



import java.awt.*;

public class StraightPipe extends Pipe {
    public StraightPipe(int x, int y, int rotation) {
        super(x, y, rotation);
    }
    @Override
    public void draw(Graphics g, int tileSize, Color color) {
        int x = getX() * tileSize;
        int y = getY() * tileSize;
        g.setColor(color);
        if (getRotation() == 0 || getRotation() == 180) {
            g.fillRect(x + tileSize / 4 + 1, y, tileSize / 2, tileSize);
        } else {
            g.fillRect(x, y + tileSize / 4 + 1, tileSize, tileSize / 2);
        }
    }
    @Override
    public boolean isConnectedTo(Point otherPoint) {
        int dx = otherPoint.x - this.getX();
        int dy = otherPoint.y - this.getY();

        if (this.getRotation() == 0 || this.getRotation() == 180) {
            return (dy == 1 || dy == -1) && dx == 0;
        } else {
            return (dx == 1 || dx == -1) && dy == 0;
        }
    }
}