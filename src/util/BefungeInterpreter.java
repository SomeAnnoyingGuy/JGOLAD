package util;

import java.util.Stack;

public class BefungeInterpreter {
	
	//0 - UP 1 - RIGHT 2 - DOWN 3 - LEFT
	private byte dir = 0;
	private static byte[][] Directions = {{0,1},{1,0},{0,-1},{-1,0}}; 
	
	private char[][] board;
	private int x = 0;
	private int y = 0;
	private Stack memory = new Stack();
	private String input;
	private String output = "";
	public BefungeInterpreter() {
		
	}
	
	public void Execute(String code) {
		Execute(code.split("\n"), "");
	}
	
	public void Execute(String code, String input) {
		Execute(code.split("\n"), input);
	}
	
	public void Execute(String code, String regex, String input) {
		Execute(code.split(regex), input);
	}
	
	public void Execute(String[] code, String in) {
		//reset (some parts)
		dir = 1;
		x = 0;
		y = 0;
		input = in;
		//convert code into a board
		if (code.length < 1) throw new IllegalArgumentException("Length of code is:"+code.length);
		int height = code.length;
		int width = 0;
		for (int i = 0; i < code.length; i++) {
			if (width < code[i].length()) {
				width = code[i].length();
			}
		}
		if (width < 1) throw new IllegalArgumentException("Width of code is:"+width);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i > code[j].length() - 1) {
					board[j][i] = ' '; 
				} else {
					board[j][i] = code[j].charAt(i);
				}
			}
		}
	}
	
	private void Move() {
		this.x += Directions[dir][0];
		this.y += Directions[dir][1];
		
		if (this.x > this.board[0].length - 1) {
			this.x = 0;
		}
		if (this.x < 0) {
			this.x = this.board[0].length - 1;
		}
		if (this.y > this.board.length - 1) {
			this.y = 0;
		}
		if (this.y < 0) {
			this.y = this.board.length - 1;
		}
	}
}
