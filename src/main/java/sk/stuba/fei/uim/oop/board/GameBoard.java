package sk.stuba.fei.uim.oop.board;

import sk.stuba.fei.uim.oop.game.DepthFirstSearch;
import sk.stuba.fei.uim.oop.game.PipeGame;
import sk.stuba.fei.uim.oop.pipe.CurvedPipe;
import sk.stuba.fei.uim.oop.pipe.Pipe;
import sk.stuba.fei.uim.oop.pipe.StraightPipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
public class GameBoard extends JPanel implements MouseListener, MouseMotionListener {
    private int boardSize;
    private int currentLevel;
    private Pipe[][] pipes;
    private Point startPoint;
    private Point endPoint;
    private List<Point> generatedPath;
    private int correctPathLen;
    private Point mousePosition;

    private PipeGame pipeGame;

    public GameBoard(PipeGame pipeGame) {
        this.pipeGame = pipeGame;
        boardSize = 8;
        currentLevel = 1;
        pipes = new Pipe[boardSize][boardSize];
        initializeBoard();
        setPreferredSize(new Dimension(400, 400));

        setBackground(Color.WHITE);

        addMouseListener(this);
        addMouseMotionListener(this);
    }
    public int getCurrentLevel() {
        return currentLevel;
    }


    private void initializeBoard() {
        correctPathLen = 0;
        startPoint = new Point(0, (int) (Math.random() * boardSize));
        endPoint = new Point(boardSize - 1, (int) (Math.random() * boardSize));

        DepthFirstSearch dfs = new DepthFirstSearch(boardSize, startPoint, endPoint);
        generatedPath = dfs.getPath();
        generatedPath.add(endPoint);

        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                pipes[y][x] = null;
            }
        }
        for (int i = 0; i < generatedPath.size() - 1; i++) {
            Point current = generatedPath.get(i);
            Point next = generatedPath.get(i + 1);

            int dx = next.x - current.x;
            int dy = next.y - current.y;

            int rotation = 0;
            if (dx == 1) {
                rotation = 0;
            } else if (dx == -1) {
                rotation = 180;
            } else if (dy == 1) {
                rotation = 90;
            } else if (dy == -1) {
                rotation = 270;
            }

            if (i > 0 && i < generatedPath.size() - 2) {
                Point prev = generatedPath.get(i - 1);

                int dx_prev = current.x - prev.x;
                int dy_prev = current.y - prev.y;

                if ((dx_prev != 0 && dy_prev == 0 && dx == 0 && dy != 0) || (dx_prev == 0 && dy_prev != 0 && dx != 0 && dy == 0)) {
                    pipes[current.y][current.x] = new CurvedPipe(current.x, current.y, rotation);
                } else {
                    pipes[current.y][current.x] = new StraightPipe(current.x, current.y, rotation);
                }
            } else {
                pipes[current.y][current.x] = new StraightPipe(current.x, current.y, rotation);
            }

            int rotations = (int) (Math.random() * 4);
            for (int j = 0; j < rotations; j++) {
                pipes[current.y][current.x].rotate();
            }
        }
        int endPointRotation = 0;
        pipes[endPoint.y][endPoint.x] = new StraightPipe(endPoint.x, endPoint.y, endPointRotation);
    }

    public void resetGame() {
        initializeBoard();
        repaint();
    }

    public void checkPath() {
        correctPathLen = getCorrectPathLen();
        recheckCorrectPath();
        Pipe lastCorrectPipe = pipes[generatedPath.get(correctPathLen - 1).y][generatedPath.get(correctPathLen - 1).x];
        Pipe endPointPipe = pipes[endPoint.y][endPoint.x];

        int lastCorrectPipeOut = (lastCorrectPipe.getRotation() + 180) % 360;
        int endPointPipeOut = (endPointPipe.getRotation() + 180) % 360;

        if (lastCorrectPipe.getPoint().equals(endPoint)) {
            newLevel();
        } else if (Math.abs(lastCorrectPipeOut - endPointPipeOut) == 180) {
            int dx = endPoint.x - lastCorrectPipe.getPoint().x;
            int dy = endPoint.y - lastCorrectPipe.getPoint().y;
            if (lastCorrectPipeOut == 0 && dx == 1 && dy == 0) {
                newLevel();
            } else if (lastCorrectPipeOut == 180 && dx == -1 && dy == 0) {
                newLevel();
            } else if (lastCorrectPipeOut == 90 && dx == 0 && dy == 1) {
                newLevel();
            } else if (lastCorrectPipeOut == 270 && dx == 0 && dy == -1) {
                newLevel();
            }
        }

        repaint();
    }






    public void newLevel() {
        currentLevel++;
        resetGame();
        pipeGame.updateLevelLabel(currentLevel);
    }



    public void setBoardSize(int newSize) {
        boardSize = newSize;
        pipes = new Pipe[boardSize][boardSize];
        resetGame();
    }

    private Point sumPoint(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    private Pipe getPipe(Point p) {
        if(p.x < 0 || p.y < 0 || p.x >= boardSize || p.y >= boardSize) return null;
        return pipes[p.y][p.x];
    }

    private Pipe getNeighbourPipe(Point p, Point d) {
        Point new_p = new Point(p.x + d.x, p.y + d.y);
        return getPipe(new_p);
    }

    private boolean checkConnection(Point p1, Point p2) {
        Pipe pipe1 = pipes[p1.y][p1.x];
        Pipe pipe2 = pipes[p2.y][p2.x];
        if (pipe1 == null || pipe2 == null) return false;

        if (p2.equals(endPoint)) {
            return pipe1.isConnectedTo(p2);
        }

        return pipe1.isConnectedTo(p2) && pipe2.isConnectedTo(p1);
    }


    public int getCorrectPathLen() {
        int i;
        for(i = 0; i < generatedPath.size()-1; i++) {
            Point current = generatedPath.get(i);
            Point next = generatedPath.get(i + 1);
            if(!checkConnection(current, next)) break;
        }
        return i+1;
    }

    public void recursiveCheck(Pipe curr_pipe, Pipe prev_pipe) {
        Point[] augment = new Point[] {
                new Point(1, 0),
                new Point(-1, 0),
                new Point(0, 1),
                new Point(0, -1)};
        Point curr_point = curr_pipe.getPoint();
        for(Point a: augment) {
            Pipe neighbour = getNeighbourPipe(curr_point, a);
            if(neighbour == null) continue;
            if(neighbour == prev_pipe) continue;
            if(!checkConnection(curr_point, neighbour.getPoint())) continue;
            neighbour.setCorrect(true);
            recursiveCheck(neighbour, curr_pipe);
        }
    }

    private void resetCorrectPath() {
        for(Point p: generatedPath) {
            Pipe pipe = getPipe(p);
            if(pipe != null) pipe.setCorrect(false);
        }
    }

    public void recheckCorrectPath() {
        resetCorrectPath();
        Pipe first_pipe = getPipe(generatedPath.get(0));
        if(first_pipe == null) return;
        first_pipe.setCorrect(true);
        recursiveCheck(first_pipe, null);
    }

    protected void drawMouseRect(Graphics g, int size) {
        if(mousePosition == null) return;
        g.setColor(Color.ORANGE);
        int x = mousePosition.x / size;
        int y = mousePosition.y / size;
        g.fillRect(x * size, y * size, size, size);
    }

    protected void drawBoundaryPosition(Graphics g, Point point, int size, Color color) {
        g.setColor(color);
        g.fillOval(point.x * size, point.y * size, size, size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int tileSize = Math.min(getWidth() / boardSize, getHeight() / boardSize);
        int counter = 0;

        drawMouseRect(g, tileSize);
        drawBoundaryPosition(g, startPoint, tileSize, Color.RED);
        drawBoundaryPosition(g, endPoint, tileSize, Color.GREEN);

        for (Point point : generatedPath) {
            Pipe pipe = pipes[point.y][point.x];
            Color color = pipe.getCorrect() ? Color.BLUE : Color.BLACK;
            pipe.draw(g, tileSize, color);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int tileSize = Math.min(getWidth() / boardSize, getHeight() / boardSize);
        int x = e.getX() / tileSize;
        int y = e.getY() / tileSize;

        if (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
            Pipe clickedPipe = pipes[y][x];
            if (clickedPipe != null) {
                clickedPipe.rotate();
                correctPathLen = 0;
                repaint();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mousePosition = null;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}
}