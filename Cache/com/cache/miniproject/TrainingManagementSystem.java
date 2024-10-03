package com;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Trainee class implementing Runnable to allow task execution in threads
class Trainee implements Runnable {
    private String name;
    private int points;  // Tracks points earned by the trainee
    private boolean completedMorningTask;
    private boolean completedAfternoonTask;
    private boolean morningAttendance; // Tracks morning attendance
    private boolean afternoonAttendance; // Tracks afternoon attendance

    // Constructor
    public Trainee(String name) {
        this.name = name;
        this.points = 0;  // Starts with 0 points
        this.completedMorningTask = false;
        this.completedAfternoonTask = false;
        this.morningAttendance = false; // Default to not present for morning
        this.afternoonAttendance = false; // Default to not present for afternoon
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public boolean isMorningPresent() {
        return morningAttendance;
    }

    public boolean isAfternoonPresent() {
        return afternoonAttendance;
    }

    // Method to mark morning attendance
    public void markMorningAttendance(boolean isPresent) {
        this.morningAttendance = isPresent;
    }

    // Method to mark afternoon attendance
    public void markAfternoonAttendance(boolean isPresent) {
        this.afternoonAttendance = isPresent;
    }

    // Task completion for the morning
    public void completeMorningTask() {
        if (morningAttendance) { // Only perform task if trainee is present in the morning
            Random random = new Random();
            completedMorningTask = random.nextBoolean(); // Random task completion
            if (completedMorningTask) {
                points += 10;  // Earn points for completing morning task
            }
        }
    }

    // Task completion for the afternoon
    public void completeAfternoonTask() {
        if (afternoonAttendance) { // Only perform task if trainee is present in the afternoon
            Random random = new Random();
            completedAfternoonTask = random.nextBoolean(); // Random task completion
            if (completedAfternoonTask) {
                points += 10;  // Earn points for completing afternoon task
            }
        }
    }

    // Print trainee status at the end of the day
    public void printStatus() {
        System.out.println(name + " - Points: " + points + 
            ", Morning Task: " + (morningAttendance ? (completedMorningTask ? "Completed" : "Missed") : "Absent") +
            ", Afternoon Task: " + (afternoonAttendance ? (completedAfternoonTask ? "Completed" : "Missed") : "Absent") +
            ", Morning Attendance: " + (morningAttendance ? "Present" : "Absent") +
            ", Afternoon Attendance: " + (afternoonAttendance ? "Present" : "Absent"));
    }

    // Save task results to a file (cache)
    public void cacheTaskResults() {
        try (FileWriter writer = new FileWriter(name + "_task_results.txt")) {
            writer.write("Trainee: " + name + "\n");
            writer.write("Morning Task: " + (morningAttendance ? (completedMorningTask ? "Completed" : "Missed") : "Absent") + "\n");
            writer.write("Afternoon Task: " + (afternoonAttendance ? (completedAfternoonTask ? "Completed" : "Missed") : "Absent") + "\n");
            writer.write("Points: " + points + "\n");
            writer.write("Morning Attendance: " + (morningAttendance ? "Present" : "Absent") + "\n");
            writer.write("Afternoon Attendance: " + (afternoonAttendance ? "Present" : "Absent") + "\n");
            System.out.println("Task results cached for " + name + " in " + name + "_task_results.txt");
        } catch (IOException e) {
            System.out.println("Error saving task results for " + name);
            e.printStackTrace();
        }
    }

    // Override run method to simulate task completion
    @Override
    public void run() {
        // Each trainee completes their tasks based on attendance
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

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/traineemanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

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

    // Method to fetch attendance from the database
    private void fetchAttendanceFromDatabase(LocalDate inputDate) {
        String attendanceQuery = "SELECT trainee_id, forenoon_status, afternoon_status FROM Attendance WHERE date = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(attendanceQuery)) {

            preparedStatement.setDate(1, java.sql.Date.valueOf(inputDate));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int traineeId = resultSet.getInt("trainee_id");
                String forenoonStatus = resultSet.getString("forenoon_status");
                String afternoonStatus = resultSet.getString("afternoon_status");

                boolean isMorningPresent = "Present".equalsIgnoreCase(forenoonStatus);
                boolean isAfternoonPresent = "Present".equalsIgnoreCase(afternoonStatus);

                // Mark attendance for the trainee
                Trainee trainee = trainees.get(traineeId - 1); // Assuming trainee IDs start from 1
                trainee.markMorningAttendance(isMorningPresent);
                trainee.markAfternoonAttendance(isAfternoonPresent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Display schedule
    public void displaySchedule() {
        System.out.println("Training starts at: " + startTime);
        System.out.println("Lunch break from: " + lunchBreakStart + " to " + lunchBreakEnd);
        System.out.println("Training ends at: " + endTime);
    }

    // Simulate the training day
    public void startTrainingDay(LocalDate inputDate) {
        // Fetch attendance from the database for the specified date
        fetchAttendanceFromDatabase(inputDate);

        System.out.println("\n** Training Day Begins **");

        // Take morning attendance
        takeMorningAttendance();

        System.out.println("\n--- Morning Session: Assigning Tasks ---");
        assignMorningTasks();

        System.out.println("\n--- Lunch Break ---");
        System.out.println("All trainees are having lunch.");

        // Take afternoon attendance
        takeAfternoonAttendance();

        System.out.println("\n--- Afternoon Session: Assigning Tasks ---");
        assignAfternoonTasks();

        System.out.println("\n--- Training Day Ends ---");

        System.out.println("\n** Final Results **");
        displayTraineeResults();
    }

    // Take morning attendance
    private void takeMorningAttendance() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Taking Morning Attendance ---");
        for (Trainee trainee : trainees) {
            System.out.print("Is " + trainee.getName() + " present in the morning? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            boolean isPresent = input.equals("y");
            trainee.markMorningAttendance(isPresent);
        }
    }

    // Take afternoon attendance
    private void takeAfternoonAttendance() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Taking Afternoon Attendance ---");
        for (Trainee trainee : trainees) {
            System.out.print("Is " + trainee.getName() + " present in the afternoon? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            boolean isPresent = input.equals("y");
            trainee.markAfternoonAttendance(isPresent);
        }
    }

    // Assign tasks for morning session only to present trainees
    private void assignMorningTasks() {
        System.out.println("Assigning morning tasks to trainees...");
    
        List<Thread> taskThreads = new ArrayList<>();
        for (Trainee trainee : trainees) {
            if (trainee.isMorningPresent()) {  // Assign tasks only if present
                Thread taskThread = new Thread(trainee);
                taskThreads.add(taskThread);
                taskThread.start();
            } else {
                System.out.println(trainee.getName() + " is absent, no morning task assigned.");
            }
        }

        // Wait for all threads to complete
        for (Thread taskThread : taskThreads) {
            try {
                taskThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Assign tasks for afternoon session only to present trainees
    private void assignAfternoonTasks() {
        System.out.println("Assigning afternoon tasks to trainees...");

        List<Thread> taskThreads = new ArrayList<>();
        for (Trainee trainee : trainees) {
            if (trainee.isAfternoonPresent()) {  // Assign tasks only if present
                Thread taskThread = new Thread(trainee);
                taskThreads.add(taskThread);
                taskThread.start();
            } else {
                System.out.println(trainee.getName() + " is absent, no afternoon task assigned.");
            }
        }

        // Wait for all threads to complete
        for (Thread taskThread : taskThreads) {
            try {
                taskThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Display results for all trainees
    private void displayTraineeResults() {
        for (Trainee trainee : trainees) {
            trainee.printStatus();
        }
    }
}

public class TrainingManagementSystem {
    public static void main(String[] args) {
        int numTrainees = 5; // Example number of trainees
        TrainingDay trainingDay = new TrainingDay(numTrainees);
        
        LocalDate trainingDate = LocalDate.now(); // Set to current date
        trainingDay.displaySchedule();
        trainingDay.startTrainingDay(trainingDate);
    }
}
