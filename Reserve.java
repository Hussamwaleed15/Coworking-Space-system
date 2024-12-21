import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Reserve {
    private static List<User> userList = new ArrayList<>();
    private String date;
    private String time;
    public Reserve() {
        this.date = date;
        this.time = time;
    }
    public String toString() {
        return date + "," + time;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }

public static void reservation(ArrayList<Slot_or_rooms> rooms) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter your ID: ");
    int userId = scanner.nextInt();

    User currentUser = null;
    for (User user : AddingUser.getUserList()) {
        if (user.getUserid() == userId) {
            currentUser = user;
            break;
        }
    }

    if (currentUser == null) {
        System.out.println("User ID not found. Reservation cannot be made.");
        return;
    }

    String userType = currentUser.getType();
    String roomType;
    int roomValue;

    switch (userType.toLowerCase()) {
        case "general":
            roomType = "general";
            roomValue = 5;
            break;
        case "formal":
            roomType = "formal";
            roomValue = 10;
            break;
        case "instructor":
            roomType = "teaching";
            roomValue = 15;
            break;
        default:
            System.out.println("Invalid user type. Reservation cannot be made.");
            return;
    }

    System.out.print("Enter the date for reservation (1-7): ");
    int date = scanner.nextInt();
    while (date < 1 || date > 7) {
        System.out.print("Invalid date. Please enter a date between 1 and 7: ");
        date = scanner.nextInt();
    }

    System.out.println("Available " + roomType + " rooms for date " + date + ":");
    boolean availableRoomsFound = false;
    for (Slot_or_rooms room : rooms) {
        if (room.roomType.equalsIgnoreCase(roomType)) {
            System.out.print("Room ID: " + room.Room_Id + " | Available Hours: ");
            boolean hasSlot = false;
            for (int hour = 0; hour < 12; hour++) {
                if (room.Availability[date - 1][hour]) {
                    System.out.print((hour + 13) + ":00 ");
                    hasSlot = true;
                }
            }
            if (hasSlot) {
                availableRoomsFound = true;
                System.out.println();
            } else {
                System.out.println("No slots available.");
            }
        }
    }

    if (!availableRoomsFound) {
        System.out.println("No rooms available.");
        return;
    }

    System.out.print("Enter Room ID: ");
    String roomId = scanner.next();
    Slot_or_rooms selectedRoom = null;
    for (Slot_or_rooms room : rooms) {
        if (room.Room_Id.equals(roomId) && room.roomType.equalsIgnoreCase(roomType)) {
            selectedRoom = room;
            break;
        }
    }

    if (selectedRoom == null) {
        System.out.println("Invalid Room ID.");
        return;
    }

    System.out.print("Enter start time (13-24): ");
    int start = scanner.nextInt();
    System.out.print("Enter end time (14-24): ");
    int end = scanner.nextInt();

    for (int i = start - 13; i < end - 13; i++) {
        if (!selectedRoom.Availability[date - 1][i]) {
            System.out.println("Time slot unavailable.");
            return;
        }
    }

    // Mark the room slots as unavailable
    for (int i = start - 13; i < end - 13; i++) {
        selectedRoom.Availability[date - 1][i] = false;
    }

    // Create a new reservation entry for the user
    User newReservation = new User(
            currentUser.getFirstname(),
            currentUser.getLastname(),
            currentUser.getUserid(),
            currentUser.getEmail(),
            currentUser.getPassword(),
            currentUser.getType(),
            "RES" + new Random().nextInt(1000), // New Reserve ID
            (end - start), // Hours for the new reservation
            String.valueOf(date), // Reservation date
            start + ":00 - " + end + ":00",
            String.valueOf(start),
            String.valueOf(end),
            (end - start) * roomValue, // Cost of new reservation
            roomId // Room ID for new reservation
    );

    // Add the new reservation as a new entry in the user list
    AddingUser.getUserList().add(newReservation);

    // Update the original user with cumulative totals
    currentUser.setTotalHours(currentUser.getTotalHours() + (end - start));
    currentUser.setValue((end - start) * roomValue);

    AddingUser.saveUsersToFile();
    System.out.println("Reservation saved.");
}

    public static void cancelReserve() {
        Scanner s = new Scanner(System.in);
        System.out.println("Cancelling a reservation...");
        System.out.print("Enter your ID: ");
        int id = s.nextInt();
        int counter = 0;
        boolean validReservation = false;

        // Loop through the user list to find the user's reservations
        for (User user : AddingUser.getUserList()) {
            if (id == user.getUserid()) {
                counter++;
                System.out.println("Reservation ID: " + user.getReserve_id());
            }
        }

        if (counter == 0) {
            System.out.println("No reservations found for this user.");
            return; // Exit if no reservations found
        }

        // Allow the user to cancel multiple reservations
        while (true) {
            System.out.print("Enter the Reserve ID to cancel (or type 'exit' to stop): ");
            String rID = s.next();

            if (rID.equalsIgnoreCase("exit")) {
                break; // Exit if the user chooses to stop
            }

            validReservation = false;

            // Search for the matching reservation and cancel it
            for (int i = 0; i < AddingUser.getUserList().size(); i++) {
                User user = AddingUser.getUserList().get(i);

                if (rID.equals(user.getReserve_id()) && id == user.getUserid()) {
                    // Cancel reservation and apply fees
                    int fees = (counter > 1) ? 50 : 100;
                    System.out.println("Reservation cancelled. Fees: " + fees);

                    // Clear the reservation data
                    user.setReserve_id("N/A");
                    user.setDate("N/A");
                    user.setTime("N/A");
                    user.setTimeStart("N/A");
                    user.setTimeEnd("N/A");
                    user.setValue(0);
                    user.setRoomId("N/A");

                    AddingUser.getUserList().set(i, user); // Update the user list

                    // Update the file after each cancellation
                    AddingUser.saveUsersToFile();

                    validReservation = true;
                    counter--; // Decrease the counter if a reservation is cancelled
                    break; // Exit after successful cancellation
                }
            }

            if (!validReservation) {
                System.out.println("Invalid Reserve ID. Please try again.");
            }

            // Optionally, allow the user to cancel another reservation
            System.out.print("Do you want to cancel another reservation? (y/n): ");
            String choice = s.next();
            if (choice.equalsIgnoreCase("n")) {
                break; // Exit if the user does not want to cancel another reservation
            }
        }
    }

    public static void update(ArrayList<Slot_or_rooms> rooms) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Step 1: Ask for User ID
            System.out.print("Please enter your User ID: ");
            int inputId = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline character

            // Step 2: Validate the User ID


            boolean userFound = false;
            for (User user : AddingUser.getUserList()) {
            if (inputId == user.getUserid()) {
                userFound = true;
                break;
            }
           }

        if (!userFound) {
            System.out.println("User ID not found.");
            return;
        }

            // Step 3: Display all reservations for the User ID
            System.out.println("These are your reservations:");
            User targetUser = null;
            for (User user : AddingUser.getUserList()) {
                if (user.getUserid() == inputId) {
                    System.out.println("Reservation ID: " + user.getReserve_id());
                    targetUser = user;
                }
            }

            if (targetUser == null) {
                System.out.println("No reservations found for this user ID.");
                return;
            }

            // Step 4: Ask for specific reservation ID to update
            System.out.println("Which reservation ID do you want to update?");
            String rID = scanner.nextLine();

            if (!targetUser.getReserve_id().equals(rID)) {
                System.out.println("Invalid reservation ID or it doesn't belong to your account.");
                return;
            }

            // Step 5: Display available rooms based on user type
            String userType = targetUser.getType();
            System.out.println("Displaying available rooms for user type: " + userType);

            boolean hasAvailableRooms = false;
            for (Slot_or_rooms room : rooms) {
                if (room.roomType.equals(userType)) {
                    System.out.println("Room ID: " + room.Room_Id);

                    for (int day = 0; day < room.Availability.length; day++) {
                        System.out.print("Day " + (day + 1) + ": ");
                        boolean slotAvailable = false;

                        for (int hour = 0; hour < room.Availability[day].length; hour++) {
                            if (room.Availability[day][hour]) {
                                slotAvailable = true;
                                int time = 13 + hour;
                                System.out.print(time + ":00 ");
                            }
                        }

                        if (!slotAvailable) {
                            System.out.print("No slots available.");
                        }
                        System.out.println(); // New line after each day
                    }
                    hasAvailableRooms = true;
                }
            }

            if (!hasAvailableRooms) {
                System.out.println("No available rooms found for your user type.");
                return;
            }

            // Step 6: Allow the user to choose a room ID, date, and time
            System.out.println("Enter the Room ID you want to reserve:");
            String selectedRoomId = scanner.nextLine();

            System.out.println("Choose a new date (1-7): ");
            int newDate = scanner.nextInt();
            System.out.println("Choose a start time (hour, 13 to 24): ");
            int startHour = scanner.nextInt();
            System.out.println("Choose an end time (hour, 13 to 24): ");
            int endHour = scanner.nextInt();

            if (newDate < 1 || newDate > 7 || startHour < 13 || endHour > 24 || startHour > endHour) {
                System.out.println("Invalid date or time selected. Please try again.");
                return;
            }

            // Step 7: Update the availability array and the user's reservation
            boolean updated = false;
            for (Slot_or_rooms room : rooms) {
                if (room.Room_Id.equals(selectedRoomId) && room.roomType.equals(userType)) {
                    boolean slotsAvailable = true;
                    for (int hour = startHour; hour <= endHour; hour++) {
                        if (!room.Availability[newDate - 1][hour - 13]) {
                            slotsAvailable = false;
                            break;
                        }
                    }

                    if (slotsAvailable) {
                        // Mark the selected slots as unavailable
                        for (int hour = startHour; hour <= endHour; hour++) {
                            room.Availability[newDate - 1][hour - 13] = false;
                        }

                        // Update the user's reservation
                        targetUser.setRoomId(selectedRoomId);
                        targetUser.setDate(String.valueOf(newDate));
                        targetUser.setTime(startHour + ":00 to " + endHour + ":00");
                        targetUser.setTimeStart(String.valueOf(startHour)); // Update time_start
                        targetUser.setTimeEnd(String.valueOf(endHour));     // Update time_end

                        System.out.println("Reservation updated successfully.");

                        // Save the updated user list to file
                        AddingUser.saveUsersToFile();
                        updated = true;
                        break;
                    }
                }
            }

            if (!updated) {
                System.out.println("The selected slots are not available. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    public void bonus() {
        Scanner scanner = new Scanner(System.in);
        AddingUser.loadUsersFromFile(); // Load the users from the file
        ArrayList<User> userList = AddingUser.getUserList(); // Use the userList from AddingUser

        // Prompt for User ID
        System.out.print("Enter User ID: ");
        String inputUserId = scanner.nextLine().trim();

        // Check and calculate bonus for the user
        calculateBonus(userList, inputUserId);
    }

    private void calculateBonus(ArrayList<User> userList, String userId) {
        User targetUser = null;
        double totalHours = 0;

        // Find all reservations by User ID and calculate total hours
        for (User user : userList) {
            if (String.valueOf(user.getUserid()).equals(userId)) {
                totalHours += user.getTotalHours();
                if (targetUser == null) {
                    targetUser = user; // Use the first matching user as the target
                }
            }
        }

        // If user is not found
        if (targetUser == null) {
            System.out.println("Error: User ID " + userId + " not found!");
            return;
        }

        // Update the target user's total hours
        targetUser.setTotalHours(totalHours);

        // Get user details
        String userType = targetUser.getType().toLowerCase();

        // Determine bonus eligibility based on user type
        double bonusThreshold;
        switch (userType) {
            case "general":
            case "formal":
                bonusThreshold = 6;
                break;
            case "instructor":
                bonusThreshold = 15;
                break;
            default:
                System.out.println("Error: Unknown user type \"" + targetUser.getType() + "\" for user ID " + userId);
                return;
        }

        // Check if the user qualifies for a bonus
        if (totalHours >= bonusThreshold) {
            targetUser.setTotalHours(totalHours + 1); // Add bonus hour to total
            System.out.println("🎉 Congratulations " + targetUser.getFirstname() + " " + targetUser.getLastname()
                    + "! You've earned 1 bonus hour.");
            System.out.println("✅ Your total hours are now: " + targetUser.getTotalHours());
        } else {
            double hoursNeeded = bonusThreshold - totalHours;
            System.out.println("⏳ Hi " + targetUser.getFirstname() + " " + targetUser.getLastname()
                    + ", you need " + hoursNeeded + " more hour(s) to qualify for a bonus.");
        }

        // Save the updated user list back to the file
        AddingUser.saveUsersToFile();
    }
}
