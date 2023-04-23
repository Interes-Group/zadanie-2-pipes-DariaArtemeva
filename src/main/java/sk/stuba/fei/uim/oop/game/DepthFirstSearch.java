package sk.stuba.fei.uim.oop.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DepthFirstSearch {
    private int boardSize;
    private Point startPoint;
    private Point endPoint;
    private boolean[][] visited;

    public DepthFirstSearch(int boardSize, Point startPoint, Point endPoint) {
        this.boardSize = boardSize;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.visited = new boolean[boardSize][boardSize];
    }

    public List<Point> getPath() {
        Stack<Point> path = new Stack<>();
        if (dfs(startPoint, path)) {
            return new ArrayList<>(path);
        } else {
            return Collections.emptyList();
        }
    }

    private boolean dfs(Point point, Stack<Point> path) {
        if (isOutOfBounds(point) || visited[point.y][point.x]) {
            return false;
        }

        visited[point.y][point.x] = true;
        path.push(point);

        if (point.equals(endPoint)) {
            return true;
        }

        List<Point> neighbors = getNeighbors(point);
        Collections.shuffle(neighbors);

        for (Point neighbor : neighbors) {
            if (dfs(neighbor, path)) {
                return true;
            }
        }

        path.pop();
        return false;
    }

    private boolean isOutOfBounds(Point point) {
        return point.x < 0 || point.x >= boardSize || point.y < 0 || point.y >= boardSize;
    }

    private List<Point> getNeighbors(Point point) {
        List<Point> neighbors = new ArrayList<>();

        neighbors.add(new Point(point.x - 1, point.y));
        neighbors.add(new Point(point.x + 1, point.y));
        neighbors.add(new Point(point.x, point.y - 1));
        neighbors.add(new Point(point.x, point.y + 1));

        return neighbors;
    }
}
