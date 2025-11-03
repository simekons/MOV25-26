package com.example.gamelogic;

import com.example.engine.IEngine;
import com.example.engine.IGraphics;
import com.example.engine.IImage;
import com.example.engine.IInput;
import com.example.engine.IScene;

import java.util.List;

public class GameScene implements IScene {

    private IEngine iEngine;
    private IGraphics iGraphics;

    private Button exitButton;
    private IImage exitImage;

    //private Cell cell;

    private MapGrid cells;

    public GameScene(IEngine iEngine){
        this.iEngine = iEngine;
        this.iGraphics = this.iEngine.getGraphics();

        loadAssets();
        //this.cell = new Cell(this.iGraphics, 200, 200, 300, 0xff000000, 3, 4);

        this.exitButton = new Button(this.iGraphics, this.exitImage, 25, 25, 50, 50);

        this.cells = new MapGrid(8, 15, 50, 50);
        createCells();

    }

    private void createCells(){
        int w = 900 / this.cells.getColumns();
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 15; col++){
                //System.out.println("x: " + (w*col) + " y: " + (w*row));
                boolean path = row == 4 ? true : false;
                Cell cell = new Cell(this.iGraphics, w * col, w * row,w, 0xff000000, row, col, path);
                this.cells.addCell(cell, row, col);
            }
        }
    }
    public void loadAssets(){
        // this.exitImage = iGraphics.loadImage("sprites/exit.png");
    }

    @Override
    public void render() {
        //this.cell.render();
        for (int row = 0; row < cells.getRows(); row++){
            for (int col = 0; col < cells.getColumns(); col++){
                this.cells.getCell(row, col).render();
            }
        }

        int h = (900/this.cells.getColumns()) * 80;
        this.iGraphics.setColor(0xff333333);
        iGraphics.fillRectangle(0, h, 900, 300);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void handleInput(List<IInput.TouchEvent> events) {

    }
}
