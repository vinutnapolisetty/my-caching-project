package com.cache.miniproject;

import java.util.concurrent.*;
import java.io.Serializable;
import java.util.*;

class Flight implements Serializable {
    private String flightNumber;
    private String destination;
    private String departure;
    private int availableSeats;

    public Flight(String flightNumber, String destination, String departure, int availableSeats) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departure = departure;
        this.availableSeats = availableSeats;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDestination() {
        return destination;
    }

    public String getDeparture() {
        return departure;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", destination='" + destination + '\'' +
                ", departure='" + departure + '\'' +
                ", availableSeats=" + availableSeats +
                '}';
    }
}

// Flight Reservation System
public class FlightReservationSystem {

    private final ConcurrentHashMap<String, Flight> flightCache = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final Map<String, Flight> flightData = new HashMap<>();

    public FlightReservationSystem() {
        // Initialize flight data
        flightData.put("AA101", new Flight("AA101", "New York", "Los Angeles", 150));
        flightData.put("AA102", new Flight("AA102", "Chicago", "Miami", 200));
        flightData.put("AA103", new Flight("AA103", "San Francisco", "New York", 100));
        flightData.put("AA104", new Flight("AA104", "Houston", "Las Vegas", 50));
        flightData.put("AA105", new Flight("AA105", "Seattle", "Chicago", 75));
    }

    // Simulate flight data retrieval (e.g., from a database)
    private Flight loadFlightFromDatabase(String flightNumber) {
        // Simulated delay to mimic database lookup
        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Retrieve flight from the flightData map
        return flightData.get(flightNumber);
    }

    // Search for a flight (using cache)
    public Future<Flight> searchFlightAsync(String flightNumber) {
        return executorService.submit(() -> {
            Flight flight = flightCache.get(flightNumber);
            if (flight == null) {
                System.out.println("Flight not found in cache, fetching from database...");
                flight = loadFlightFromDatabase(flightNumber);
                if (flight != null) {
                    flightCache.put(flightNumber, flight);  // Add to cache
                }
            } else {
                System.out.println("Flight found in cache.");
            }
            return flight;
        });
    }

    // Book a flight asynchronously
    public Future<Boolean> bookFlightAsync(String flightNumber) {
        return executorService.submit(() -> {
            Flight flight = flightCache.get(flightNumber);
            if (flight == null) {
                System.out.println("Flight not found in cache, cannot book.");
                return false;
            } else if (flight.getAvailableSeats() > 0) {
                flight.bookSeat();
                System.out.println("Seat booked on flight: " + flightNumber);
                return true;
            } else {
                System.out.println("No seats available on flight: " + flightNumber);
                return false;
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FlightReservationSystem system = new FlightReservationSystem();

        // Search for a flight
        Future<Flight> searchFlight1 = system.searchFlightAsync("AA101");
        Flight flight1 = searchFlight1.get();
        System.out.println(flight1 != null ? flight1 : "Flight not found");

        // Book a seat on a flight
        Future<Boolean> bookFlight1 = system.bookFlightAsync("AA101");
        boolean bookingStatus1 = bookFlight1.get();
        System.out.println("Booking status for AA101: " + (bookingStatus1 ? "Success" : "Failed"));

        // Search for another flight
        Future<Flight> searchFlight2 = system.searchFlightAsync("AA102");
        Flight flight2 = searchFlight2.get();
        System.out.println(flight2 != null ? flight2 : "Flight not found");

        // Book a seat on another flight
        Future<Boolean> bookFlight2 = system.bookFlightAsync("AA102");
        boolean bookingStatus2 = bookFlight2.get();
        System.out.println("Booking status for AA102: " + (bookingStatus2 ? "Success" : "Failed"));

        // Search and book a flight with no available seats
        Future<Flight> searchFlight3 = system.searchFlightAsync("AA104");
        Flight flight3 = searchFlight3.get();
        System.out.println(flight3 != null ? flight3 : "Flight not found");

        // Attempt to book a seat on the flight with no seats available
        Future<Boolean> bookFlight3 = system.bookFlightAsync("AA104");
        boolean bookingStatus3 = bookFlight3.get();
        System.out.println("Booking status for AA104: " + (bookingStatus3 ? "Success" : "Failed"));

        // Shutdown the executor service
        system.shutdown();
    }
}
