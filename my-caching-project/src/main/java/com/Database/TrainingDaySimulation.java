package com.Database;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Trainee class implementing Runnable to allow task execution in threads
class Trainee implements Runnable {
    private String name;
    private int points;  // Tracks points earned by the trainee
    private boolean completedMorningTask;
    private boolean completedAfternoonTask;

    // Constructor
    public Trainee(String name) {
        this.name = name;
        this.points = 0;  // Starts with 0 points
        this.completedMorningTask = false;
        this.completedAfternoonTask = false;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    // Task completion for the morning
    public void completeMorningTask() {
        Random random = new Random();
        completedMorningTask = random.nextBoolean(); // Random task completion
        if (completedMorningTask) {
            points += 10;  // Earn points for completing morning task
        }
    }

    // Task completion for the afternoon
    public void completeAfternoonTask() {
        Random random = new Random();
        completedAfternoonTask = random.nextBoolean(); // Random task completion
        if (completedAfternoonTask) {
            points += 10;  // Earn points for completing afternoon task
        }
    }

    // Print trainee status at the end of the day
    public void printStatus() {
        System.out.println(name + " - Points: " + points + ", Morning Task: " + (completedMorningTask ? "Completed" : "Missed")
                + ", Afternoon Task: " + (completedAfternoonTask ? "Completed" : "Missed"));
    }

    // Save task results to a file (cache)
    public void cacheTaskResults() {
        try (FileWriter writer = new FileWriter(name + "_task_results.txt")) {
            writer.write("Trainee: " + name + "\n");
            writer.write("Morning Task: " + (completedMorningTask ? "Completed" : "Missed") + "\n");
            writer.write("Afternoon Task: " + (completedAfternoonTask ? "Completed" : "Missed") + "\n");
            writer.write("Points: " + points + "\n");
            System.out.println("Task results cached for " + name + " in " + name + "_task_results.txt");
        } catch (IOException e) {
            System.out.println("Error saving task results for " + name);
            e.printStackTrace();
        }
    }

    // Override run method to simulate task completion
    @Override
    public void run() {
        // Each trainee will complete both tasks in their own thread
        completeMorningTask();
        completeAfternoonTask();
        cacheTaskResults(); // Cache task results after completing tasks
    }
}

class TrainingDay {
    private List<Trainee> trainees;
    private LocalTime startTime;
    private LocalTime lunchBreakStart;
    private LocalTime lunchBreakEnd;
    private LocalTime endTime;

    // Constructor
    public TrainingDay(int numTrainees) {
        trainees = new ArrayList<>();
        for (int i = 1; i <= numTrainees; i++) {
            trainees.add(new Trainee("Trainee " + i));
        }
        startTime = LocalTime.of(9, 0);  // 9:00 AM
        lunchBreakStart = LocalTime.of(13, 0);  // 1:00 PM
        lunchBreakEnd = LocalTime.of(14, 0);  // 2:00 PM
        endTime = LocalTime.of(17, 0);   // 5:00 PM
    }

    // Display schedule
    public void displaySchedule() {
        System.out.println("Training starts at: " + startTime);
        System.out.println("Lunch break from: " + lunchBreakStart + " to " + lunchBreakEnd);
        System.out.println("Training ends at: " + endTime);
    }

    // Simulate the training day
    public void startTrainingDay() {
        System.out.println("\n** Training Day Begins **");

        System.out.println("\n--- Morning Session: Assigning Tasks ---");
        assignTasks();

        System.out.println("\n--- Lunch Break ---");
        System.out.println("All trainees are having lunch.");

        System.out.println("\n--- Afternoon Session: Continuing Tasks ---");

        System.out.println("\n--- Training Day Ends ---");

        System.out.println("\n** Final Results **");
        displayTraineeResults();
    }

    // Assign tasks to trainees using threads
    private void assignTasks() {
        System.out.println("Assigning tasks to trainees...");

        List<Thread> taskThreads = new ArrayList<>();
        for (Trainee trainee : trainees) {
            Thread thread = new Thread(trainee);  // Create a new thread for each trainee
            taskThreads.add(thread);
            thread.start();  // Start the task (run method will be executed)
        }

        // Wait for all threads to finish
        for (Thread thread : taskThreads) {
            try {
                thread.join();  // Ensure that main thread waits for all task threads to complete
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Display the final results for all trainees
    private void displayTraineeResults() {
        for (Trainee trainee : trainees) {
            trainee.printStatus();
        }

        // Find the top performer
        Trainee topPerformer = trainees.stream()
                .max((t1, t2) -> Integer.compare(t1.getPoints(), t2.getPoints()))
                .orElse(null);

        if (topPerformer != null) {
            System.out.println("\nTop Performer of the Day: " + topPerformer.getName() + " with " + topPerformer.getPoints() + " points!");
        }
    }
}

public class TrainingDaySimulation {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/traineemanagement"; // Replace with your database URL
    private static final String DB_USER = "root"; // Replace with your database username
    private static final String DB_PASSWORD = "root"; // Replace with your database password

    public static void main(String[] args) {
        // Insert initial trainee details into the database
        insertInitialTraineeDetails();

        // Insert initial attendance records into the database
        insertInitialAttendanceRecords();

        // Create a dynamic training session with 10 trainees
        TrainingDay trainingDay = new TrainingDay(10);

        // Display the schedule for the day
        trainingDay.displaySchedule();

        // Start the training day simulation
        trainingDay.startTrainingDay();
    }

    // Method to insert initial trainee details into the Trainees table
    private static void insertInitialTraineeDetails() {
        String insertQuery = "INSERT INTO Trainees (first_name, last_name, email) VALUES (?, ?, ?)";

        // Sample data to be inserted into the Trainees table
        String[][] traineesData = {
            {"John", "Doe", "john.doe@example.com"},
            {"Jane", "Smith", "jane.smith@example.com"},
            {"Alice", "Johnson", "alice.johnson@example.com"},
            {"Bob", "Brown", "bob.brown@example.com"},
            {"Charlie", "Davis", "charlie.davis@example.com"},
            {"David", "Wilson", "david.wilson@example.com"},
            {"Eve", "Taylor", "eve.taylor@example.com"},
            {"Frank", "Thomas", "frank.thomas@example.com"},
            {"Grace", "Moore", "grace.moore@example.com"},
            {"Henry", "White", "henry.white@example.com"}
        };

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Loop through sample data and insert each record
            for (String[] trainee : traineesData) {
                preparedStatement.setString(1, trainee[0]); // first_name
                preparedStatement.setString(2, trainee[1]); // last_name
                preparedStatement.setString(3, trainee[2]); // email
                preparedStatement.executeUpdate();
            }

            System.out.println("Initial trainee details inserted into the database.");
        } catch (SQLException e) {
            System.out.println("Error inserting initial trainee details.");
            e.printStackTrace();
        }
    }

    // Method to insert initial attendance records into the Attendance table
    private static void insertInitialAttendanceRecords() {
        String insertQuery = "INSERT INTO Attendance (trainee_id, forenoon_status, afternoon_status, date) VALUES (?, ?, ?, ?)";
        LocalDate today = LocalDate.now(); // Get today's date

        // Sample attendance records for trainees
        String[][] attendanceData = {
            {"1", "Present", "Present"},
            {"2", "Present", "Absent"},
            {"3", "Absent", "Present"},
            {"4", "Present", "Present"},
            {"5", "Present", "Present"},
            {"6", "Absent", "Absent"},
            {"7", "Present", "Present"},
            {"8", "Present", "Absent"},
            {"9", "Present", "Present"},
            {"10", "Absent", "Present"}
        };

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Loop through sample data and insert each record
            for (String[] record : attendanceData) {
                preparedStatement.setInt(1, Integer.parseInt(record[0])); // trainee_id
                preparedStatement.setString(2, record[1]); // forenoon_status
                preparedStatement.setString(3, record[2]); // afternoon_status
                preparedStatement.setDate(4, java.sql.Date.valueOf(today)); // date
                preparedStatement.executeUpdate();
            }

            System.out.println("Initial attendance records inserted into the database.");
        } catch (SQLException e) {
            System.out.println("Error inserting initial attendance records.");
            e.printStackTrace();
        }
    }
}
