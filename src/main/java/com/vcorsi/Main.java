package com.vcorsi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.vcorsi.myfinder.Finder;
import com.vcorsi.myfinder.FinderFactory;

/**
 * Main class of the program.
 * 
 * @author vladimiro
 * 
 */
public class Main {

	private final Path inputPath;
	private final Path outputPath;
	private final Format format;

	/**
	 * @param inputPath
	 *            path to the input file
	 * @param outputPath
	 *            path where to write the output file
	 */
	public Main(final Path inputPath, final Path outputPath) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.format = new Format();
	}

	/**
	 * Reads input file, executes the research and writes the output file.
	 * 
	 * @throws IOException
	 */
	void execute() throws IOException {
		final BigDecimal bankTransfer;
		final List<BigDecimal> amounts = new ArrayList<>();
		// Scanning the input file
		try (Scanner scanner = new Scanner(inputPath)) {
			if (!scanner.hasNext()) {
				throw new IllegalStateException(
						"Bank transfer amount not found.");
			}
			final String sumLine = scanner.nextLine();
			bankTransfer = format.parse(sumLine);
			while (scanner.hasNext()) {
				amounts.add(format.parse(scanner.nextLine()));
			}
		}
		if (amounts.size() == 0) {
			throw new IllegalStateException("Due payment amounts not found.");
		}
		// Looking for a solution
		final BigDecimal[] duePayments = amounts.toArray(new BigDecimal[amounts
				.size()]);
		final Finder finder = FinderFactory.myFinder(duePayments, bankTransfer);
		final BigDecimal[] solution = finder.find();
		// Printing output file
		try (PrintWriter pw = new PrintWriter(new FileWriter(
				outputPath.toFile()))) {
			if (solution.length == 0) {
				pw.print("NO SOLUTION");
				return;
			}
			// sort ascending
			Arrays.sort(solution);
			for (int i = 0; i < solution.length; i++) {
				pw.print(format.format(solution[i]));
				if(i < solution.length - 1){
					pw.println();
				}
			}
		}
	}

	/**
	 * @param args
	 *            one argument: a file path containing the input file
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Error: provide an input file");
			System.exit(1);
		}
		try {
			final Path path = Paths.get(args[0]);
			if (!path.toFile().exists()) {
				throw new IllegalArgumentException("File does not exist");
			}
			final Main main = new Main(path, Paths.get("output.txt"));
			main.execute();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}

}
