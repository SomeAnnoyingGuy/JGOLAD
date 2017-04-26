package util;

import java.util.Stack;

public class BefungeInterpreter {
	
	//0 - UP 1 - RIGHT 2 - DOWN 3 - LEFT
	private byte dir = 0;
	private static byte[][] Directions = {{0,1},{1,0},{0,-1},{-1,0}}; 
	
	private char[][] board;
	private int x = 0;
	private int y = 0;
	private boolean stringmode = false;
	private Stack<Integer> memory = new Stack();
	private String input;
	private String output = "";
	private long maxTime = 15000;
	public BefungeInterpreter() {
		
	}
	
	public void Execute(String code) throws Exception {
		Execute(code.split("\n"), "");
	}
	
	public void Execute(String code, String input) throws Exception {
		Execute(code.split("\n"), input);
	}
	
	public void Execute(String code, String regex, String input) throws Exception {
		Execute(code.split(regex), input);
	}
	
	public void Execute(String[] code, String in) throws Exception {
		//reset (some parts)
		dir = 1;
		x = 0;
		y = 0;
		stringmode = false;
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
		
		//set the timer
		long endTime = System.currentTimeMillis() + this.maxTime ;
		//do shit
		while (true) {
			char current = board[this.y][this.x];
			if (stringmode) {
				if (current != '"') {
					pushMemory(current);
				} else {
					stringmode = false;
				}
			} else {
				//movement
				if (current == '^') {
					dir = 0;
				}
				if (current == '>') {
					dir = 1;
				}
				if (current == 'v') {
					dir = 2;
				}
				if (current == '<') {
					dir = 3;
				}
				if (current == '?') {
					dir = (byte) (Math.random()*4);
				}
				if (current == '#') {
					Move(); //extra move will cause a skip
				}
				
				//input handling
				if (current == '&') {
					int inputInt = 0;
					String[] splited = input.split(" ");
					try {
						inputInt = Integer.parseInt(splited[0]);
						input = input.substring(splited[0].length());
					} catch (Exception e) {
						inputInt = 0;
					}
					pushMemory(inputInt);
				}
				if (current == '~') {
					try {
						pushMemory(input.charAt(0));
						input = input.substring(1);
					} catch (Exception e) {
						pushMemory(0);
					}
				}
				if (current == '"') {
					stringmode = true;
				}
				if (current >= '0' && current <= '9') {
					pushMemory(Character.getNumericValue(current));
				}
				
				//operations
				if (current == '+') {
					pushMemory(
							popMemory() + popMemory()
							);
				}
				if (current == '-') {
					pushMemory(
							popMemory() - popMemory()
							);
				}
				if (current == '*') {
					pushMemory(
							popMemory() * popMemory()
							);
				}
				if (current == '/') {
					pushMemory(
							Math.floorDiv(popMemory(), popMemory())
							);
				}
				if (current == '!') {
					pushMemory((popMemory() == 0)?1:0);
				}
				if (current == '`') {
					pushMemory((popMemory() > popMemory())?1:0);
				}
				if (current == ':') {
					int value = popMemory();
					pushMemory(value);
					pushMemory(value);
				}
				if (current == '$') {
					popMemory();
				}
				if (current == '\\') {
					int value1 = popMemory();
					int value2 = popMemory();
					pushMemory(value1);
					pushMemory(value2);
				}
				
				if (current == '@') {
					break;
				}
			}
			
			if (endTime > System.currentTimeMillis()) {
				throw new Exception("Program timed out");
			}
			
			//move to nexx instruction
			Move();
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
	
	private int popMemory() {
		int data = 0;
		try {
			data = memory.pop();
		} catch (Exception e) {
			data = 0;
		}
		return data;
	}
	
	private void pushMemory(char value) {
		pushMemory((int) value);
	}
	
	private void pushMemory(int value) {
		memory.push(value);
	}
}
