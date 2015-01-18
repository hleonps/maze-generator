int WIDTH = 1000;
int HEIGHT = 1000;
int CELL_SIDE = 50;

Maze maze;
Player player;

//Maze maze = new Maze(WIDTH, HEIGHT);

void setup(){
	size(WIDTH, HEIGHT);
	stroke(#000000);
	maze = new Maze(WIDTH/CELL_SIDE, HEIGHT/CELL_SIDE);
	player = new Player(maze.firstCell.x, maze.firstCell.y);
}

void draw(){
	background(#ffffff);
	fill(255, 0, 0);
	rect(maze.firstCell.y * CELL_SIDE, maze.firstCell.x * CELL_SIDE, CELL_SIDE, CELL_SIDE);
	fill(0, 0, 255);
	rect(player.y * CELL_SIDE, player.x * CELL_SIDE, CELL_SIDE, CELL_SIDE);
	fill(0, 255, 0);
	rect(maze.endCell.y * CELL_SIDE, maze.endCell.x * CELL_SIDE, CELL_SIDE, CELL_SIDE);
	for (Cell[] cells : maze.grid) {
		for (Cell cell : cells) {
			if(!cell.walls[3].isOpen){
				line(cell.y * CELL_SIDE + CELL_SIDE, cell.x * CELL_SIDE, cell.y * CELL_SIDE + CELL_SIDE, cell.x * CELL_SIDE + CELL_SIDE);
			}

			if(!cell.walls[1].isOpen){
				line(cell.y * CELL_SIDE, cell.x * CELL_SIDE + CELL_SIDE, cell.y * CELL_SIDE + CELL_SIDE, cell.x * CELL_SIDE + CELL_SIDE);
			}
		}
	}

	if(maze.endCell.x == player.x && maze.endCell.y == player.y){
		maze = new Maze(WIDTH/CELL_SIDE, HEIGHT/CELL_SIDE);
		player = new Player(maze.firstCell.x, maze.firstCell.y);
	}
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
