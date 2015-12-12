/*
	Maze Generator by Heiner León | @H_leon12
	The MIT License (MIT) Copyright (c) 2015 Heiner León
*/

int WIDTH = 500; //WIDTH OF THE WINDOW'S SCREEN
int HEIGHT = 500; //HEIGHT OF THE WINDOW'S SCREEN
int CELL_SIZE = 25; //CELL SIDE LENGTH

//COLORS
color COLOR_START_SQUARE = color(255, 0, 0);
color COLOR_FINISH_SQUARE = color(0, 0, 255);
color COLOR_PLAYER_SQUARE = color(0, 255, 0);

Maze maze;
Player player;

void setup(){
	size(500, 500);

	maze = new Maze(WIDTH/CELL_SIZE, HEIGHT/CELL_SIZE); //Adaptive maze, screen size divided by the size of the cell
	player = new Player(maze.firstCell.x, maze.firstCell.y); // The X & Y of the player are the same that first cell of the maze.
}

void draw(){
	background(#ffffff);
	strokeWeight(1);

	// First Cell position
	stroke(COLOR_START_SQUARE);
	fill(COLOR_START_SQUARE);
	rect(maze.firstCell.y * CELL_SIZE, maze.firstCell.x * CELL_SIZE, CELL_SIZE, CELL_SIZE);

	// Player's position
	stroke(COLOR_PLAYER_SQUARE);
	fill(COLOR_PLAYER_SQUARE);
	rect(player.y * CELL_SIZE, player.x * CELL_SIZE, CELL_SIZE, CELL_SIZE);

	// End Cell position
	stroke(COLOR_FINISH_SQUARE);
	fill(COLOR_FINISH_SQUARE);
	rect(maze.endCell.y * CELL_SIZE, maze.endCell.x * CELL_SIZE, CELL_SIZE, CELL_SIZE);

	//draw the maze
	strokeWeight(1.2);
	stroke(#000000);
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

void resetMaze(){
	maze = new Maze(WIDTH/CELL_SIZE, HEIGHT/CELL_SIZE);
	player = new Player(maze.firstCell.x, maze.firstCell.y);
}

void keyReleased(){
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