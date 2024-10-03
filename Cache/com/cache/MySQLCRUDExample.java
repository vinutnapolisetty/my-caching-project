package com.cache;
import java.sql.*;
public class MySQLCRUDExample {
    private static final String URL = "jdbc:mysql://localhost:3306/employeemanagement";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    //Generate comment for all the instance variables
    
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static PreparedStatement preparedStatement;

    public static void main(String[] args) {
        try {
             connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
             createRecord();
             //readRecord();
             //updateRecord();
             //deleteRecord();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
                if(preparedStatement != null){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //following is my create table statement
    /*create table employees(
id int auto_increment primary key,
first_name varchar(50),
last_name varchar(50),
email varchar(100),
hire_date DATE,
salary decimal(10,2)
); */
    private static void createRecord(){
        String query = "INSERT INTO employees (first_name, last_name, email, hire_date, salary) VALUES (?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "Vinutna");
            preparedStatement.setString(2, "Doe");
            preparedStatement.setString(3, "john.doe@example.com");
            preparedStatement.setDate(4, new Date(System.currentTimeMillis()));
            preparedStatement.setDouble(5, 50000.00);
            preparedStatement.executeUpdate();
            System.out.println("Record created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}