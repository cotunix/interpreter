import java.io.File;
import java.util.*;

public class Interpreter {

	public File program;
	public static HashMap<String, Object> vars;

	public static void main(String[] args) {
		vars = new HashMap<String, Object>();
		Scanner in = new Scanner("prog1.zpm");
		while (in.hasNext()) {
			String temp = in.nextLine();
			String[] line = temp.split(" ");
			switch(line[0]){
				case "PRINT":
					if (line[1] != "\"")
						System.out.println(vars.get(line[1]));
					else
						System.out.println(temp);
					break;
				default:
					if (line[1] == "="){
						String s = "";
						if (line [2] == "\"") {
							int i = 3;
							while (true) {
								s += line[i];
								i++;
								if (line[i] == "\"")
									break;
							}
							vars.put(line[0], s);
						}
						else
							vars.put(line[0], line[2]);
					}
			}
		}

		in.close();
	}
}
