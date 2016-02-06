import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Interpreter {

	public File program;
	public static Map<String, Object> vars;
	public int lineNum;
	public static Scanner in;

	public Interpreter(File program) {
		this.program = program;
		lineNum = 1;
		vars = new HashMap<String, Object>();
		try {
			in = new Scanner(program);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (in.hasNext()) {
			processLine(in.nextLine().split(" "));
			lineNum++;
		}
	}

	private void loop(int num, ArrayList<String> statement) {
		for (int i = 0; i < num; i++) {
			for (String s : statement) {
				processLine(s.split(" "));
			}
		}
	}

	public void processLine(String[] line) {
		switch (line[0]) {
		case "PRINT":
			print(line);
			break;

		case "FOR":
			forLoop(line);
			break;

		default:
			if (line[1].equals("=")) {
				assignment(line);
			
			} else if (line[1].equals("*=")) {
				doMath(line[0], line[2], "*");
			} else if (line[1].equals("-=")) {
				doMath(line[0], line[2], "-");
			} else if (line[1].equals("+=")) {
				if (!(vars.get(line[0]) instanceof String)) {
					doMath(line[0], line[2], "+");
				} else if (line[2].contains("\"")) {
					String s = line[2].substring(1);
					if (!line[3].equals(";")) {

						int i = 3;
						while (!line[i].contains("\"")) {
							s += " " + line[i];
							i++;
						}
						s += " " + line[i].substring(0, line[i].length() - 1);
						vars.put(line[0], vars.get(line[0]) + s);
					} else {
						vars.put(
								line[0],
								vars.get(line[0])
										+ s.substring(0, s.length() - 1));
					}
				} else {
					if (vars.get(line[2]) instanceof String) {
						vars.put(line[0], (String) vars.get(line[0])
								+ (String) vars.get(line[2]));
					} else {
						System.err.println("RUNTIME ERROR: LINE " + lineNum);
						System.exit(1);
					}
				}

			}
		}
	}

	private void doMath(String x, String y, String op) {
		if (vars.containsKey(x)) {
			try {
				int b;
				int a = (int) vars.get(x);
				if (vars.containsKey(y)) {
					b = (int) vars.get(y);
				} else {
					b = Integer.parseInt(y);
				}
				if (op == "*")
					vars.put(x, a * b);
				else if (op == "-")
					vars.put(x, a - b);
				else
					vars.put(x, a + b);

			} catch (Exception e) {
				System.err.println("RUNTIME ERROR: LINE " + lineNum);
				System.exit(1);
			}
		}

		else {
			System.err.println("RUNTIME ERROR: LINE " + lineNum);
			System.exit(1);
		}
	}
	private void forLoop(String[] line){
		ArrayList<String> statement = new ArrayList<String>();
		String temp = "";
		int loopnum = 1;
		for (int i = 2; i < line.length; i++) {
			if (line[i].equals("ENDFOR")) {
				loopnum--;
				if (loopnum == 0)
					break;
			} else if (line[i].equals("FOR")) {
				String s = "";
				int nest = 1;

				while (true) {

					if (line[i].equals("FOR"))
						nest++;
					else if (line[i].equals("ENDFOR"))
						nest--;
					s += line[i] + " ";
					i++;
					if (nest == 0) {
						break;
					}

				}
				statement.add(s);
			}

			else if (line[i].equals(";")) {
				temp += line[i];
				statement.add(temp);
				temp = "";
			} else {
				temp += line[i] + " ";

			}
		}
		loop(Integer.parseInt(line[1]), statement);
	}
	
	private void assignment(String[] line){
		if (line[2].contains("\"")) {
			String s = line[2].substring(1);
			if (!line[3].equals(";")) {

				int i = 3;
				while (!line[i].contains("\"")) {
					s += " " + line[i];
					i++;
				}
				s += " " + line[i].substring(0, line[i].length() - 1);
				vars.put(line[0], s);
			} else {
				vars.put(line[0], s.substring(0, s.length() - 1));
			}
		} else if (vars.containsKey(line[2])) {
			vars.put(line[0], vars.get(line[2]));
		} else {
			vars.put(line[0], Integer.parseInt(line[2]));
		}
	}
	private void print(String[] line){
		if (vars.containsKey(line[1]))
			System.out.println(line[1] + " = " + vars.get(line[1]));
		else if (line[1].charAt(0) == '\"') {
			String s = line[1].substring(1);
			int i = 2;
			while (!line[i].contains("\"")) {
				s += line[i];
				i++;
			}
			System.out.println(s
					+ line[i].substring(0, line[i].length() - 1));
		} else {
			System.err.println("RUNTIME ERROR: LINE " + lineNum);
			System.exit(1);
		}
	}

	public static void main(String args[]) {
		Interpreter test = new Interpreter(new File(args[0]));
		test.run();
		
	}
}
