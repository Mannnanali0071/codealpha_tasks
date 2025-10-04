import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeManager {
    private static ArrayList<String> studentNames = new ArrayList<>();
    private static ArrayList<Double> studentGrades = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            printMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1:
                    addStudentGrade();
                    break;
                case 2:
                    displaySummaryReport();
                    break;
                case 3:
                    showStatistics();
                    break;
                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println(" Student Grade Manager ");
        System.out.println("1. Add Student and Grade");
        System.out.println("2. Display Summary Report");
        System.out.println("3. Show Statistics (Average, Highest, Lowest)");
        System.out.println("4. Exit");
    }

    private static void addStudentGrade() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        double grade = getDoubleInput("Enter grade (0-100): ");
        if (grade < 0 || grade > 100) {
            System.out.println("Invalid grade. Must be between 0 and 100.");
            return;
        }

        studentNames.add(name);
        studentGrades.add(grade);

        System.out.println("Student and grade added.");
    }

    private static void displaySummaryReport() {
        if (studentNames.isEmpty()) {
            System.out.println("No student data to display.");
            return;
        }

        System.out.println(" Summary Report ");
        System.out.printf("%-20s | %-10s\n", "Student Name", "Grade");
        System.out.println("------------------------------");

        for (int i = 0; i < studentNames.size(); i++) {
            System.out.printf("%-20s | %-10.2f\n", studentNames.get(i), studentGrades.get(i));
        }
    }

    private static void showStatistics() {
        if (studentGrades.isEmpty()) {
            System.out.println("No grades available to calculate statistics.");
            return;
        }

        double sum = 0, highest = Double.MIN_VALUE, lowest = Double.MAX_VALUE;

        for (double grade : studentGrades) {
            sum += grade;
            if (grade > highest) highest = grade;
            if (grade < lowest) lowest = grade;
        }

        double average = sum / studentGrades.size();

        System.out.println("\n--- Grade Statistics ---");
        System.out.printf("Average Grade: %.2f\n", average);
        System.out.printf("Highest Grade: %.2f\n", highest);
        System.out.printf("Lowest Grade : %.2f\n", lowest);
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter an integer.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a valid number.");
            }
        }
    }
}
