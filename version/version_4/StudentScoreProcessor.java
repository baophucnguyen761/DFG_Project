import java.util.Scanner;
import java.util.ArrayList;

public class StudentScoreProcessor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> scores = new ArrayList<>();
        int tempScore; // Variable for tracking scores
        
        System.out.println("Welcome to the Student Score Processor!");
        
        // Input loop
        while (true) {
            System.out.print("Enter a student's score (or -1 to stop): ");
            tempScore = scanner.nextInt();

            if (tempScore == -1) {
                break;
            }
            
            if (tempScore >= 0 && tempScore <= 100) {
                scores.add(tempScore);
                System.out.println("Score " + tempScore + " added!");
            } else {
                System.out.println("Invalid score. Please enter a number between 0 and 100.");
            }
        }
        
        if (scores.isEmpty()) {
            System.out.println("No scores entered. Exiting program.");
            return;
        }
        
        // Calculate the average score
        int total = 0;
        for (int score : scores) {
            tempScore = score; // Assign to temp variable for tracking
            total += tempScore;
        }
        double average = total / (double) scores.size();
        System.out.println("The average score is: " + average);
        
        // Find the highest score
        tempScore = scores.get(0);
        for (int score : scores) {
            if (score > tempScore) {
                tempScore = score; // Update temp variable
            }
        }
        System.out.println("The highest score is: " + tempScore);
        
        // Display scores above the average
        System.out.println("Scores above average:");
        for (int score : scores) {
            if (score > average) {
                tempScore = score; // Assign to temp variable for use
                System.out.println(tempScore);
            }
        }
        
        // Count scores in ranges
        int above90 = 0, between70And89 = 0, below70 = 0;
        for (int score : scores) {
            tempScore = score; // Use temp variable for classification
            if (tempScore >= 90) {
                above90++;
            } else if (tempScore >= 70) {
                between70And89++;
            } else {
                below70++;
            }
        }
        
        System.out.println("Number of scores >= 90: " + above90);
        System.out.println("Number of scores between 70 and 89: " + between70And89);
        System.out.println("Number of scores < 70: " + below70);

        scanner.close();
        System.out.println("Processing complete. Thank you!");
    }
}
