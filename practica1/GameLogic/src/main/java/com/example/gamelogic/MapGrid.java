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

    public MapGrid(int rows, int columns, float screenWidth, float screenHeight, IGraphics iGraphics) {
        this.rows = rows;
        this.columns = columns;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.iGraphics = iGraphics;

        // tamaño ideal de celda
        float idealCellWidth = screenWidth / columns;
        float idealCellHeight = screenHeight / rows;

        // mantener celdas cuadradas
        this.cellSize = Math.min(idealCellWidth, idealCellHeight);

        // dimensiones reales del grid
        float gridWidth = this.cellSize * columns;
        float gridHeight = this.cellSize * rows;

        // centrar el grid
        this.offsetX = (screenWidth - gridWidth) / 2f;
        this.offsetY = (screenHeight - gridHeight) / 2f;

        // crear matriz de celdas
        this.cells = new Cell[this.rows][this.columns];

        // crear celdas automáticamente
        createCells();
    }

    /**
     * Crea las celdas del grid.
     */
    private void createCells() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                boolean path = (row == 4); // ejemplo: fila 4 es camino
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

                if (path && startingPoint == null) {
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
}

