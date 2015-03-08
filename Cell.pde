/*
	Maze Generator by Heiner León | @H_leon12
	The MIT License (MIT) Copyright (c) 2015 Heiner León
*/

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
