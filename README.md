# Cinema Seat Reservation System

This project is a Spring Boot REST application that manages seat reservations for cinema movie screenings.  
Customers can view available movies and screenings, select seats by row and number, create reservations, and cancel them.  
The system prevents double booking and persists all data in a relational database.

## Business Requirements

1. The system must manage cinemas with one or more halls.
2. Each hall must have a predefined set of seats identified by row and number.
3. The system must manage movies with details such as title, duration, and description.
4. A movie can be scheduled for one or more screenings.
5. A screening represents a movie shown in a specific hall at a specific date and time.
6. Each screening must have an associated ticket price.
7. Customers must be able to create reservations for a specific screening.
8. A reservation must contain one or more selected seats.
9. The system must prevent double booking of the same seat for the same screening.
10. Customers must be able to cancel a reservation, releasing the reserved seats.

## MVP Features

1. **Movie Management**  
   Create, read, update, and delete movies.

2. **Screening Management**  
   Create screenings and list screenings by movie or date.

3. **Seat Availability View**  
   Display all seats for a screening and indicate which are available or reserved.

4. **Create Reservation**  
   Allow a customer to select seats by row and number and create a reservation.

5. **Cancel Reservation**  
   Cancel an existing reservation and free the associated seats.

## Entities

1. Cinema
2. Hall
3. Seat
4. Movie
5. Screening
6. Customer
7. Reservation
8. ReservationSeat (Join Entity)

## Entity Relationships

- One **Cinema** has many **Halls**
- One **Hall** has many **Seats**
- One **Movie** has many **Screenings**
- One **Hall** has many **Screenings**
- One **Customer** has many **Reservations**
- One **Reservation** belongs to one **Screening**
- One **Reservation** has many **ReservationSeats**
- One **Seat** can appear in many **ReservationSeats** (for different screenings)

## Business Rules & Constraints

- Seat `(row, number)` must be unique within a hall.
- A seat can be reserved only once per screening.
- ReservationSeat enforces a unique constraint on `(screening_id, seat_id)`.
- Canceling a reservation changes its status to `CANCELLED` and removes associated ReservationSeat records.
- Email address of a customer must be unique.

## REST API Overview

- `GET /movies`
- `POST /movies`
- `GET /screenings`
- `GET /screenings/{id}/seats`
- `POST /reservations`
- `PUT /reservations/{id}/cancel`
