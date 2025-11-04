package com.example.gamelogic;

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

        // crear celdas usando el mapa
        createCellsFromMap(mapData.map);
    }


    /**
     * Crea las celdas del grid.
     */
    /**
     * Crea las celdas del grid usando un string de mapa (# = camino, . = vacío).
     */
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

                // El punto de inicio solo si está en la primera columna
                if (path && col == 0) {
                    startingPoint = cell;
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

    public float getCellSize() { return cellSize; }
    public float getOffsetX() { return offsetX; }
    public float getOffsetY() { return offsetY; }

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

