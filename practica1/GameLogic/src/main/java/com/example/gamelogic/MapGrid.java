package com.example.gamelogic;

import java.util.ArrayList;
import java.util.List;
import com.example.engine.IGraphics;

public class MapGrid {

    private int rows;
    private int columns;

    private float cellSize;   // tamaño cuadrado de cada celda
    private float offsetX;    // margen izquierdo para centrar el grid
    private float offsetY;    // margen superior para centrar el grid

    private float screenWidth;
    private float screenHeight;

    private Cell[][] cells;
    private Cell startingPoint;
    private List<float[]> pathPositions = new ArrayList<>();

    private IGraphics iGraphics; // tu motor gráfico o contexto de renderizado

    public MapGrid(Maps mapData, float screenWidth, float screenHeight, IGraphics iGraphics) {
        this.rows = mapData.rows;
        this.columns = mapData.cols;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.iGraphics = iGraphics;

        // tamaño de celda cuadrada
        float idealCellWidth = screenWidth / columns;
        float idealCellHeight = screenHeight / rows;
        this.cellSize = Math.min(idealCellWidth, idealCellHeight);

        // centrar grid
        float gridWidth = cellSize * columns;
        float gridHeight = cellSize * rows;
        this.offsetX = (screenWidth - gridWidth) / 2f;
        this.offsetY = (screenHeight - gridHeight) / 2f;

        this.cells = new Cell[rows][columns];

        createCellsFromMap(mapData.map);
    }

    private void createCellsFromMap(String map) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                int index = row * columns + col;
                char c = map.charAt(index);

                boolean path = (c == '#'); // el carácter '#' marca el camino

                float x = offsetX + col * cellSize;
                float y = offsetY + row * cellSize;

                Cell cell = new Cell(
                        iGraphics,
                        x,
                        y,
                        cellSize,
                        0xff000000,
                        row,
                        col,
                        path
                );

                addCell(cell, row, col);

                // Guardar punto inicial (columna 0)
                if (path && col == 0) {
                    startingPoint = cell;
                }
            }
        }

        buildPathPositions();
    }

    private void buildPathPositions() {
        if (startingPoint == null) return;

        boolean[][] visited = new boolean[rows][columns];
        Cell current = startingPoint;

        pathPositions.clear();
        pathPositions.add(new float[]{
                current.getX() + current.getSize() / 2f,
                current.getY() + current.getSize() / 2f
        });
        visited[current.getRow()][current.getColumn()] = true;

        boolean foundNext = true;
        while (foundNext) {
            foundNext = false;
            int r = current.getRow();
            int c = current.getColumn();

            // Direcciones: arriba, derecha, abajo, izquierda
            int[][] dirs = {{-1,0},{0,1},{1,0},{0,-1}};
            for (int[] d : dirs) {
                int nr = r + d[0];
                int nc = c + d[1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < columns) {
                    Cell neighbor = getCell(nr, nc);
                    if (neighbor != null && neighbor.getPath() && !visited[nr][nc]) {
                        current = neighbor;
                        visited[nr][nc] = true;
                        pathPositions.add(new float[]{
                                neighbor.getX() + neighbor.getSize() / 2f,
                                neighbor.getY() + neighbor.getSize() / 2f
                        });
                        foundNext = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Dibuja todas las celdas en pantalla.
     */
    public void render() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Cell cell = cells[row][col];
                if (cell != null) {
                    cell.render();
                }
            }
        }
    }

    public void addCell(Cell cell, int row, int column) {
        if (row >= 0 && row < rows && column >= 0 && column < columns) {
            cells[row][column] = cell;
        }
    }

    public Cell getCell(int row, int column) {
        if (row >= 0 && row < rows && column >= 0 && column < columns) {
            return cells[row][column];
        }
        return null;
    }

    public Cell getStartingPoint() { return startingPoint; }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }

    public List<float[]> getPathPositions() {
        return pathPositions;
    }

    public float getCellSize() { return cellSize; }
    public float getOffsetX() { return offsetX; }
    public float getOffsetY() { return offsetY; }

    public Tower placeTowerAt(float x, float y, TowerType type, IGraphics iGraphics) {
        Cell cell = getCellAtPosition(x, y);

        if (cell == null || cell.getPath() || cell.getTower() || !cell.isAvailable())
            return null;

        Tower tower;

        float size = cell.getSize() * 0.6f;
        int row = cell.getRow();
        int column = cell.getColumn();
        int cost = 100; // ejemplo

        switch (type) {
            case Rayo -> tower = new TowerRayo(iGraphics, row, column, size, cost, cell);
            case Fuego -> tower = new TowerFuego(iGraphics, row, column, size, cost, cell);
            case Hielo -> tower = new TowerHielo(iGraphics, row, column, size, cost, cell);
            default -> throw new IllegalArgumentException("Tipo de torre no válido");
        }

        cell.setTower(true);
        cell.setAvailable(false);

        return tower;
    }


    public Cell getCellAtPosition(float x, float y) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Cell cell = getCell(row, col);
                if (cell == null) continue;

                float cx = cell.getX();
                float cy = cell.getY();
                float size = cell.getSize();

                // Verificar si el clic está dentro de los límites de la celda
                if (x >= cx && x <= cx + size && y >= cy && y <= cy + size) {
                    return cell;
                }
            }
        }
        return null; // no se tocó ninguna celda
    }


    public void showAvailableCells(boolean available) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Cell cell = cells[row][col];

                if (!cell.getPath() && !cell.getTower()) {
                    cell.setAvailable(available);
                }
            }
        }
    }
}

