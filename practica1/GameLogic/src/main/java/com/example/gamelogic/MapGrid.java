package com.example.gamelogic;

public class MapGrid {

    private int rows;
    private int columns;

    private float cellWidth;
    private float cellHeight;

    private Cell[][] cells;

    public MapGrid(int rows, int columns, float cellWidth, float cellHeight){
        this.rows = rows;
        this.columns = columns;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;

        this.cells = new Cell[this.rows][this.columns];
    }

    public void addCell(Cell cell, int row, int column) {
        if (row >= 0 && row < this.rows && column >= 0 && column < this.columns) {
            if (this.cells[row][column] == null) {
                this.cells[row][column] = cell;
            }
        }
    }

    public int getRows() { return this.rows; }
    public int getColumns() { return this.columns; }

    public Cell getCell(int row, int column) { return this.cells[row][column]; }
}
