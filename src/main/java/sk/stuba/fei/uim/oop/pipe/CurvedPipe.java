package sk.stuba.fei.uim.oop.pipe;


import java.awt.*;
import java.awt.geom.AffineTransform;

public class CurvedPipe extends Pipe {
    public CurvedPipe(int x, int y, int rotation) {
        super(x, y, rotation);
    }
    @Override
    public boolean isConnectedTo(Point other) {
        int dx = other.x - getX();
        int dy = other.y - getY();

        if (getRotation() == 0) {
            return (dx == -1 && dy == 0) || (dx == 0 && dy == -1);
        } else if (getRotation() == 90) {
            return (dx == 0 && dy == -1) || (dx == 1 && dy == 0);
        } else if (getRotation() == 180) {
            return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
        } else if (getRotation() == 270) {
            return (dx == 0 && dy == 1) || (dx == -1 && dy == 0);
        }
        return false;
    }




    @Override
    public void draw(Graphics g, int tileSize, Color color) {
        Graphics2D g2d = (Graphics2D) g;

        AffineTransform old = g2d.getTransform();

        g2d.rotate(Math.toRadians(rotation), tileSize * x + tileSize / 2.0, tileSize * y + tileSize / 2.0);

        g2d.setStroke(new BasicStroke(tileSize / 2));

        g2d.setColor(color);
        g2d.drawLine(tileSize * x + tileSize / 2, tileSize * y + 4, tileSize * x + tileSize / 2, tileSize * y + tileSize / 2);
        g2d.drawLine(tileSize * x + 4, tileSize * y + tileSize / 2, tileSize * x + tileSize / 2, tileSize * y + tileSize / 2);

        g2d.setTransform(old);
    }



}