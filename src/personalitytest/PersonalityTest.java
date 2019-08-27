// @author Daniel Hart
// This program will accept Keirsey Temperament Sorter personality test answers 
// from an input file and generate in a text file personality types for each 
// given individual. The user must input a file in the format outlined in
// the README for this program to work.

package personalitytest;

import java.io.*;
import java.util.*;

public class PersonalityTest {
    public static final int DIM = 4;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        intro();
        System.out.print("input file name? ");
        Scanner input = new Scanner(new File(console.next()));
        System.out.print("output filename? ");
        PrintStream output = new PrintStream(new File(console.next()));
        processFile(input, output);
    }
    
    //introduces the program to the user
    public static void intro() {
        System.out.println("This program processes a file of answers to the");
        System.out.println("Keirsey Temperament Sorter. It converts the");
        System.out.println("various A and B answers for each person into");
        System.out.println("a sequence of B-percentages and then into a");
        System.out.println("four letter personality type.");
        System.out.println();
    }

    // Converts each person's answers from the input file into a personality 
    // type, and prints the types into the output file
    public static void processFile(Scanner input, PrintStream output) {
        while (input.hasNextLine()) {
            String name = input.nextLine();
            int[] percent = processLine(input.nextLine());
            String type = typeCalc(percent);
            output.println(name + ": " + Arrays.toString(percent) + " = " + type);
        }
    }

    // Processes the string of answers and returns an array of the percentage of b answers
    public static int[] processLine(String line) {
        int[] aCount = new int[DIM];
        int[] bCount = new int[DIM];
        for (int j = 0; j < DIM; j++) {
            for (int i = 0; i < 10; i++) {
                // There are 10 sets of seven answers. The first answer in each set
                // applies to the first personality type letter, the next two answers
                // apply to the next letter, and the next two pairs of answers are each 
                // associated with one letter. The if (j != 0) codeblock exists to
                // handle the letters that have two answers associated with them in
                // each set.
                if (Character.toLowerCase(line.charAt(7*i + 2*j)) == 'a') {
                    aCount[j]++;
                } else if (Character.toLowerCase(line.charAt(7*i + 2*j)) == 'b') {
                    bCount[j]++;
                }
                if (j != 0) {
                    if (Character.toLowerCase(line.charAt(7*i + (2*j-1))) == 'a') {
                        aCount[j]++;
                    } else if (Character.toLowerCase(line.charAt(7*i + (2*j-1))) == 'b') {
                        bCount[j]++;
                    }
                }
            }
        }
        return percentCalc(aCount, bCount);
    }

    // Takes A-count and B-count arrays and returns a percentage-of-B array
    public static int[] percentCalc(int[] aCount, int[] bCount) {
        int[] percent = new int[DIM];
        for (int i = 0; i < DIM; i++) {
            percent[i] = (int)((bCount[i]*100.0)/(aCount[i]+bCount[i]) + .5);
        }
        return percent;
    }

    // Takes the percentage-of-B array and returns the correct personality type.
    public static String typeCalc(int[] percent) {
        String type = "";
        String aAnswer = "ESTJ";
        String bAnswer = "INFP";
        for (int i = 0; i < DIM; i++) {
            if (percent[i] > 50) {
                type += bAnswer.charAt(i);
            } else if (percent[i] < 50) {
                type += aAnswer.charAt(i);
            } else {
                type += "X";
            }
        }
        return type;
    }
}
