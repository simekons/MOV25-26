package com.example.practica2;

import java.util.ArrayList;
import java.util.List;

import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
import com.example.engine.IAudio;
import com.example.engine.IGraphics;

/*
* MapGrid es la clase que gestiona el tablero de juego.
* */
public class MapGrid {

    private AndroidGraphics graphics;

    private AndroidImage backgroundImage;
    // Filas.
    private int rows;

    // Columnas.
    private int columns;


    // Tamaño cuadrado de cada celda
    private float cellSize;

    // Margen izquierdo para centrar el grid
    private float offsetX;

    // Margen superior para centrar el grid
    private float offsetY;


    // Ancho y alto de la pantalla.
    private float screenWidth;
    private float screenHeight;

    // Matriz de celdas.
    private Cell[][] cells;

    // Celda de comienzo.
    private Cell startingPoint;

    // Posiciones de camino.
    private List<float[]> pathPositions = new ArrayList<>();

    // Gráficos.

    // CONSTRUCTORA
    public MapGrid(Maps mapData, float screenWidth, float screenHeight, AndroidGraphics graphics) {
        this.rows = mapData.getRows();
        this.columns = mapData.getCols();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.graphics = graphics;

        // Tamaño de celda cuadrada.
        float idealCellWidth = screenWidth / columns;
        float idealCellHeight = screenHeight / rows;
        this.cellSize = Math.min(idealCellWidth, idealCellHeight);

        // Centrar grid.
        float gridWidth = cellSize * columns;
        float gridHeight = cellSize * rows;
        this.offsetX = (screenWidth - gridWidth) / 2f;
        this.offsetY = (screenHeight - gridHeight) / 2f;

        this.cells = new Cell[rows][columns];

        this.backgroundImage = this.graphics.loadImage(mapData.getBackground());
        createCellsFromMap(mapData.getMap());
    }

    // RENDERIZADO
    public void render() {
        this.graphics.drawImage(this.backgroundImage, (int)startingPoint.getX() + (int)this.screenWidth / 2 , 0, (int)this.screenWidth, (int)this.screenHeight);
        this.graphics.drawImage(this.backgroundImage, (int)startingPoint.getX() + (int)this.screenWidth / 2 , (int)this.screenHeight, (int)this.screenWidth, (int)this.screenHeight);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Cell cell = cells[row][col];
                if (cell != null) {
                    cell.render();
                }
            }
        }
    }

    // Creación de celdas.
    private void createCellsFromMap(String map) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                int index = row * columns + col;
                char c = map.charAt(index);

                // '#' marca el camino.
                boolean path = (c == '#');

                float x = offsetX + col * cellSize;
                float y = offsetY + row * cellSize;

                Cell cell = new Cell(
                        graphics,
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

    // Creación de camino.
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

    // Colocar una torre
    public Tower placeTowerAt(float x, float y, TowerType type, IGraphics iGraphics, IAudio iAudio) {
        Cell cell = getCellAtPosition(x, y);

        if (cell == null || cell.getPath() || cell.getTower() || !cell.isAvailable())
            return null;

        Tower tower;

        float size = cell.getSize() * 0.6f;
        int row = cell.getRow();
        int column = cell.getColumn();
        int cost = 100; // ejemplo

        switch (type) {
            case Rayo:
                tower = new TowerRayo(iGraphics, iAudio, row, column, size, cost, cell);
                break;
            case Fuego:
                tower = new TowerFuego(iGraphics, iAudio, row, column, size, cost, cell);
                break;
            case Hielo:
                tower = new TowerHielo(iGraphics, iAudio,  row, column, size, cost, cell);
                break;
            default:
                throw new IllegalArgumentException("Tipo de torre no válido");
        }

        cell.setTower(true);
        cell.setAvailable(false);

        return tower;
    }

    // Añadir una celda.
    public void addCell(Cell cell, int row, int column) {
        if (row >= 0 && row < rows && column >= 0 && column < columns) {
            cells[row][column] = cell;
        }
    }

    // Mostrar celdas disponibles.
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

    // --------------------------GETTERS--------------------------

    // Celda en [row][column].
    public Cell getCell(int row, int column) {
        if (row >= 0 && row < rows && column >= 0 && column < columns) {
            return cells[row][column];
        }
        return null;
    }

    // Celda en x, y.
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

    // Celda inicial.
    public Cell getStartingPoint() { return startingPoint; }

    // Filas.
    public int getRows() { return rows; }

    // Column.
    public int getColumns() { return columns; }

    // Posiciones de camino.
    public List<float[]> getPathPositions() {
        return pathPositions;
    }

    // Tamaño de celda.
    public float getCellSize() { return cellSize; }

    // Márgenes.
    public float getOffsetX() { return offsetX; }
    public float getOffsetY() { return offsetY; }



}

