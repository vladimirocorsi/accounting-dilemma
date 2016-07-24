package com.vcorsi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.vcorsi.Main;

public class MainTest {

	@Rule
	public TemporaryFolder temp = new TemporaryFolder();

	@Test
	public void testWithSolution() throws IOException {
		final File inputFile = temp.newFile();
		try (PrintWriter pw = new PrintWriter(new FileWriter(inputFile))) {
			pw.println("70.75");

			pw.println("10.12");
			pw.println("20.10");
			pw.println("30.48");
			pw.println("40.09");
			pw.print("50.65");
		}
		final File outputFile = temp.newFile();
		final Main main = new Main(inputFile.toPath(), outputFile.toPath());
		main.execute();
		try (Scanner scanner = new Scanner(outputFile.toPath())) {
			assertEquals(scanner.nextLine(), "20.10");
			assertEquals(scanner.nextLine(), "50.65");
			assertFalse(scanner.hasNext());
		}
	}

	@Test
	public void testWithSolutionNotIncludingBiggestInput() throws IOException {
		final File inputFile = temp.newFile();
		try (PrintWriter pw = new PrintWriter(new FileWriter(inputFile))) {
			pw.println("40.60");

			pw.println("10.12");
			pw.println("20.10");
			pw.println("50.65");
			pw.println("30.48");
			pw.print("40.09");
		}
		final File outputFile = temp.newFile();
		final Main main = new Main(inputFile.toPath(), outputFile.toPath());
		main.execute();
		try (Scanner scanner = new Scanner(outputFile.toPath())) {
			assertEquals(scanner.nextLine(), "10.12");
			assertEquals(scanner.nextLine(), "30.48");
			assertFalse(scanner.hasNext());
		}
	}

	@Test
	public void testWithSolutionNotIncludingBiggestAndSmallestInput()
			throws IOException {
		final File inputFile = temp.newFile();
		try (PrintWriter pw = new PrintWriter(new FileWriter(inputFile))) {
			pw.println("37.50");

			pw.println("10.12");
			pw.println("20.10");
			pw.println("150.65");
			pw.println("30.48");
			pw.println("7.28");
			pw.print("0.09");
		}
		final File outputFile = temp.newFile();
		final Main main = new Main(inputFile.toPath(), outputFile.toPath());
		main.execute();
		try (Scanner scanner = new Scanner(outputFile.toPath())) {
			assertEquals(scanner.nextLine(), "7.28");
			assertEquals(scanner.nextLine(), "10.12");
			assertEquals(scanner.nextLine(), "20.10");
			assertFalse(scanner.hasNext());
		}
	}

	@Test
	public void testNoSolution() throws IOException {
		final File inputFile = temp.newFile();
		try (PrintWriter pw = new PrintWriter(new FileWriter(inputFile))) {
			pw.println("70.76");

			pw.println("10.12");
			pw.println("20.10");
			pw.println("30.48");
			pw.println("40.09");
			pw.print("50.65");
		}
		final File outputFile = temp.newFile();
		final Main main = new Main(inputFile.toPath(), outputFile.toPath());
		main.execute();
		try (Scanner scanner = new Scanner(outputFile.toPath())) {
			assertEquals(scanner.nextLine(), "NO SOLUTION");
		}
	}

}
