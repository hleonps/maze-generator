import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MazeGenerator extends PApplet {

int WIDTH = 1000; //WIDTH OF THE WINDOW'S SCREEN
int HEIGHT = 1000; //HEIGHT OF THE WINDOW'S SCREEN
int CELL_SIZE = 50; //CELL SIDE LENGTH

Maze maze;
Player player;

public void setup(){
	size(WIDTH, HEIGHT);
	stroke(0xff000000);

	maze = new Maze(WIDTH/CELL_SIZE, HEIGHT/CELL_SIZE); //Adaptive maze, screen size divided by the size of the cell
	player = new Player(maze.firstCell.x, maze.firstCell.y); // The X & Y of the player are the same that first cell of the maze.
}

public void draw(){
	background(0xffffffff);
	// First Cell position
	fill(255, 0, 0);
	rect(maze.firstCell.y * CELL_SIZE, maze.firstCell.x * CELL_SIZE, CELL_SIZE, CELL_SIZE);

	// Player's position
	fill(0, 0, 255);
	rect(player.y * CELL_SIZE, player.x * CELL_SIZE, CELL_SIZE, CELL_SIZE);

	// End Cell position
	fill(0, 255, 0);
	rect(maze.endCell.y * CELL_SIZE, maze.endCell.x * CELL_SIZE, CELL_SIZE, CELL_SIZE);

	//draw the maze
	for (Cell[] cells : maze.grid) {
		for (Cell cell : cells) {
			if(!cell.walls[3].isOpen){
				line(cell.y * CELL_SIZE + CELL_SIZE, cell.x * CELL_SIZE, cell.y * CELL_SIZE + CELL_SIZE, cell.x * CELL_SIZE + CELL_SIZE);
			}

			if(!cell.walls[1].isOpen){
				line(cell.y * CELL_SIZE, cell.x * CELL_SIZE + CELL_SIZE, cell.y * CELL_SIZE + CELL_SIZE, cell.x * CELL_SIZE + CELL_SIZE);
			}
		}
	}

	if(maze.endCell.x == player.x && maze.endCell.y == player.y){
		resetMaze();
	}
}

public void resetMaze(){
	maze = new Maze(WIDTH/CELL_SIZE, HEIGHT/CELL_SIZE);
	player = new Player(maze.firstCell.x, maze.firstCell.y);
}

public void keyReleased(){
	if(keyCode == UP && maze.grid[player.x][player.y].walls[0].isOpen){
		player.x -=1;
	}
	else if(keyCode == DOWN && maze.grid[player.x][player.y].walls[1].isOpen){
		player.x +=1;
	}
	else if(keyCode == LEFT && maze.grid[player.x][player.y].walls[2].isOpen){
		player.y -=1;
	}
	else if(keyCode == RIGHT && maze.grid[player.x][player.y].walls[3].isOpen){
		player.y +=1;
	}
}
public class Cell{
	public Cell(int x, int y){
		this.x = x;
		this.y = y;
		this.isInMaze = false;;
		this.walls = new Wall[4];
	}

	public void setIsInMaze(){
		this.isInMaze = true;
	}

	int x;
	int y;
	boolean isInMaze;

	Wall[] walls; // TOP, BOTTOM, LEFT, RIGHT
}
public class Maze{
	public Maze(int rows, int cols){
		this.rows = rows;
		this.cols = cols;

		this.grid = new Cell[rows][cols];
		wallList = new ArrayList();
		generateCells();
		generateMaze();
	}

	private void generateMaze(){
		Cell currentCell = grid[PApplet.parseInt(random(rows))][PApplet.parseInt(random(cols))];
		firstCell = currentCell;
		insertCellToMaze(currentCell, null);

		while (!wallList.isEmpty()) {
			Wall currentWall = wallList.get(PApplet.parseInt(random(wallList.size())));
			if(!currentWall.isEdge){
				if(!(currentWall.c1.isInMaze && currentWall.c2.isInMaze)){
					currentWall.toOpenWall();
					if(currentWall.c1.isInMaze){
						insertCellToMaze(currentWall.c2, currentWall);
					}else{
						insertCellToMaze(currentWall.c1, currentWall);
					}
				}
			}
			wallList.remove(currentWall);
		}

		do{
			endCell = grid[PApplet.parseInt(random(rows))][PApplet.parseInt(random(cols))];
		}while(firstCell == endCell);
	}

	//Inserts the cell to the maze and its walls, but skip one of the wall.
	private void insertCellToMaze(Cell pCell, Wall skipWall){
		pCell.setIsInMaze();
		for (Wall wall : pCell.walls) {
			if(wall != skipWall){
				wallList.add(wall);
			}
		}
	}

	//Initializes the grid of cells
	private void generateCells(){
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				grid[i][j] = new Cell(i, j);
			}
		}
		generateWalls(); //Set all the walls of the maze.
	}

	//Initializes the walls of cells
	private void generateWalls() {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				
				// FIX TOP WALL		
				if(i == 0){
					grid[i][j].walls[0] = new Wall();
					grid[i][j].walls[0].c1 = grid[i][j];
					grid[i][j].walls[0].setIsEdge();
				}else{
					grid[i][j].walls[0] = grid[i-1][j].walls[1]; //The top wall is the same that the bottom wall above the cell.
					grid[i][j].walls[0].c2 = grid[i][j];
				}

				//FIX BOTTOM WALL
				grid[i][j].walls[1] = new Wall();
				grid[i][j].walls[1].c1 = grid[i][j];
				if(i == rows-1){
					grid[i][j].walls[1].setIsEdge();
				}

				//FIX LEFT WALL
				if(j == 0){
					grid[i][j].walls[2] = new Wall();
					grid[i][j].walls[2].c1 = grid[i][j];
					grid[i][j].walls[2].setIsEdge();
				}else{
					grid[i][j].walls[2] = grid[i][j-1].walls[3]; //The left wall is the same that the right wall of the previous cell.
					grid[i][j].walls[2].c2 = grid[i][j];
				}

				//FIX RIGHT WALL
				grid[i][j].walls[3] = new Wall();
				grid[i][j].walls[3].c1 = grid[i][j];
				if(j == cols-1){
					grid[i][j].walls[3].setIsEdge();
				}
			}
		}
	}

	int rows;
	int cols;

	Cell firstCell;
	Cell endCell;

	Cell[][] grid;
	ArrayList<Wall> wallList;
}
public class Player{
	public Player(int x, int y){
		this.x = x;
		this.y = y;
	}

	int x;
	int y;
}
public class Wall{
	public Wall(){
		this.isEdge = false;
		this.isOpen = false;
	}
	
	public void toOpenWall(){
		this.isOpen = true;
	}

	public void setIsEdge(){
		this.isEdge = true;
	}

	Cell c1;
	Cell c2;

	boolean isEdge; // Walls that only have one cell
	boolean isOpen;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#030202", "--stop-color=#cccccc", "MazeGenerator" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
