/*
	Maze Generator by Heiner León | @H_leon12
	The MIT License (MIT) Copyright (c) 2015 Heiner León
*/

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