package golad;

import java.io.File;

public class Guesser {
	public static void start() {
		WinUtil.display("Note: This is a fun little feature that may not survive in later releases.\n"
				+ "This tries to 'guess' a rulestring that certain predetermined life forms exist in.\n"
				+ "To use this, save 2 (or more, up to 10) different boards in the directory of this jar file and name them 'guesser-1.ser' and 'guesser-2.ser'\n"
				+ "The 2 boards must be the same size. Also, only 'Neutral' and 'Dead' cells are supported.\n"
				+ "This may not work on other operating systems than Windows due to file structures. Working on a fix.");

		File f1 = new File("guesser-1.ser");
		File f2 = new File("guesser-2.ser");

		Board[] boards = new Board[10];

		try {
			if (f1.exists()) {
				if (!f2.exists()) {
					WinUtil.display("Failure! " + f2.getAbsolutePath() + " does not exist.");
					return;
				}
			} else {
				WinUtil.display("Failure! " + f1.getAbsolutePath() + " does not exist.");
				return;
			}
			
			for(int i = 0; i < boards.length; i++){
				File f = new File("guesser-"+i+".ser");
				if(f.exists()){
					boards[i] = (Board) SerializeUtil.load(f);
				}
			}
			
			byte UNPROVEN = 0;
			byte DISPROVEN = -1;
			byte PROVEN = 1;
			
			byte[] condLives = new byte[9];
			byte[] condBirth = new byte[9];

			for(int i = 0; i < boards.length-1; i++){
				Board b1 = boards[i];
				Board b2 = boards[i+1];
				if(b1 != null && b2 != null){
					for(int x = 0; x < b1.getWidth(); x++){
						for(int y = 0; y < b1.getHeight(); y++){
							Cellstate before = Cellstate.getStateFromID(b1.getAt(x, y));
							Cellstate after = Cellstate.getStateFromID(b2.getAt(x, y));
							int surround = b1.getSurroundingLiving(x, y);
							if(before.isAlive()){
								if(after.isAlive()){
									condLives[surround] = PROVEN;
								}else{
									condLives[surround] = DISPROVEN;
								}
							}else{
								if(after.isAlive()){
									condBirth[surround] = PROVEN;
								}else{
									condBirth[surround] = DISPROVEN;
								}
							}
						}
					}
				}
			}
			
			String strSurvive = "";
			String strSurviveOpt = "";
			
			String strBirth = "";
			String strBirthOpt = "";
			
			for(int i = 0; i < condLives.length; i++){
				if(condLives[i] == PROVEN){
					strSurvive += i;
				}else if(condLives[i] == UNPROVEN){
					strSurviveOpt += i;
				}
			}
			for(int i = 0; i < condBirth.length; i++){
				if(condBirth[i] == PROVEN){
					strBirth += i;
				}else if(condBirth[i] == UNPROVEN){
					strBirthOpt += i;
				}
			}
			
			String rulestring = strSurvive+"+"+strSurviveOpt+"/"+strBirth+"+"+strBirthOpt;
			
			WinUtil.display("Your rulestring should be: " + rulestring+"\n"
					+ "NOTE: The numbers after the '+' are optional, any rule with or without those numbers should work for this specific pattern.\n"
					+ "You should remove the '+' and maybe those optional numbers before use.");
			
		} catch (Exception e) {
			e.printStackTrace();
			WinUtil.display("Failure! " + e.getMessage() + " with cause " + e.getCause());
		}
	}
}
