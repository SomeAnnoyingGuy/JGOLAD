package util;

import java.util.Stack;

//Befunge-93 interpreter written by IQuick
public class BefungeInterpreter {
	
	//0 - UP 1 - RIGHT 2 - DOWN 3 - LEFT
	private byte dir = 0;
	private static byte[][] Directions = {{0,1},{1,0},{0,-1},{-1,0}}; 
	
	private char[][] board;
	private int x = 0;
	private int y = 0;
	private boolean stringmode = false;
	private Stack<Integer> memory = new Stack<Integer>();
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
		board = new char[height][width];
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
		//do stuff
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
				if (current == '|') {
					if (popMemory() == 0) {
						dir = 2;
					} else {
						dir = 0;
					}
				}
				if (current == '_') {
					if (popMemory() == 0) {
						dir = 1;
					} else {
						dir = 3;
					}
				}
				
				//input handling
				if (current == '&') {
					int inputInt = 0;
					try {
						int i = 0;
						while (i < input.length() && !Character.isDigit(input.charAt(i))) i++;
						int j = i;
						while (j < input.length() && Character.isDigit(input.charAt(j))) j++;
						inputInt = Integer.parseInt(input.substring(i, j)); // might be an off-by-1 here
						input = input.substring(Math.min(j+1,input.length()));
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
				if (current == 'g') {
					int setY = popMemory();
					int setX = popMemory();
					if (setY < height && setY >= 0 && setX < width && setX >= 0) {
						pushMemory(board[setY][setX]);
					} else {
						pushMemory(' ');
					}
				}
				
				//operations
				if (current == '+') {
					pushMemory(
							popMemory() + popMemory()
							);
				}
				if (current == '-') {
					pushMemory(
							-popMemory() + popMemory()
							);
				}
				if (current == '*') {
					pushMemory(
							popMemory() * popMemory()
							);
				}
				if (current == '/') {
					int value1 = popMemory();
					int value2 = popMemory();
					pushMemory(
							(value1 != 0)?(int) Math.floor(value2/value1):0
							);
				}
				if (current == '%') {
					int value1 = popMemory();
					int value2 = popMemory();
					pushMemory(
							(value1 != 0)?value2 % value1:0
							);
				}
				if (current == '!') {
					pushMemory((popMemory() == 0)?1:0);
				}
				if (current == '`') {
					pushMemory((popMemory() < popMemory())?1:0);
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
				
				//output handling
				if (current == '.') {
					output += popMemory() + " ";
				}
				if (current == ',') {
					output += (char) popMemory();
				}
				if (current == 'p') {
					int setY = popMemory();
					int setX = popMemory();
					int value = popMemory();
					if (setY < height && setY >= 0 && setX < width && setX >= 0) {
						board[setY][setX] = (char) value;
					}
				}
				
				if (current == '@') {
					break;
				}
			}
			
			if (endTime < System.currentTimeMillis()) {
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
	
	public String getOutput() {
		return output;
	}
	
	public void setTime(long timeMilis) {
		maxTime = timeMilis;
	}
}
