import java.util.ArrayList;

public class StringConvertion {
	
	public ArrayList<Integer> replaceLetterWithPosition(String message) {
		ArrayList<Integer> output = new ArrayList<Integer>();
		System.out.println(message);
		message = message.toLowerCase();
		System.out.println(message);
		for(char letter : message.toCharArray()) {
			int currentAscii = (int)letter;
			if(currentAscii>96 && 123>currentAscii ) {
				output.add(currentAscii);
			}
		}
		return output;
	}
}
