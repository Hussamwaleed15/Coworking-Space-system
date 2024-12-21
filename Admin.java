import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Admin {

    private String logFileName = "Admin.txt"; // Log file to save admin actions

    // Main menu for admin
    public void deleteEntity(ArrayList<User> userList,ArrayList<Slot_or_rooms> rooms ) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1 - Delete User");
            System.out.println("2 - Delete Room");
            System.out.println("3 - Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    deleteUser(userList);  // Call the deleteUser function
                    break;
                case 2:
                    deleteRoom(rooms);  // Call the deleteRoom function
                    break;
                case 3:
                    System.out.println("Exiting admin menu...");
                    return; // Exit the menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private void deleteUser(ArrayList<User> userList) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Collect and display unique user IDs
        HashSet<Integer> uniqueIds = new HashSet<>();
        for (User user : userList) {
            uniqueIds.add(user.getUserid());
        }

        System.out.println("List of Unique User IDs:");
        for (Integer id : uniqueIds) {
            System.out.println("ID: " + id);
        }

        // Step 2: Prompt admin to enter a User ID to delete
        System.out.print("Enter the ID of the user to delete: ");
        int userIdToDelete = scanner.nextInt();

        // Step 3: Remove the user from the list
        boolean userFound = userList.removeIf(user -> user.getUserid() == userIdToDelete);

        // Step 4: Provide feedback to the admin
        if (userFound) {
            System.out.println("User with ID " + userIdToDelete + " has been deleted.");
            // After deleting from the list, update the file
            AddingUser.saveUsersToFile();  // Rewrites the file with the updated list
        } else {
            System.out.println("User ID " + userIdToDelete + " not found.");
}
}
    public static void deleteRoom(ArrayList<Slot_or_rooms> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No rooms available to delete.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // Display only the room names (IDs) with their indices
        System.out.println("Available rooms:");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println((i + 1) + ". " + rooms.get(i).Room_Id);
        }

        // Prompt the admin to select a room to delete
        System.out.println("Enter the number of the room to delete:");
        int choice = scanner.nextInt();

        // Validate the choice
        if (choice < 1 || choice > rooms.size()) {
            System.out.println("Invalid choice. Please try again.");
            return;
        }

        // Remove the selected room
        Slot_or_rooms removedRoom = rooms.remove(choice - 1);

        System.out.println("Room " + removedRoom.Room_Id + " has been successfully deleted.");

}

    private void saveRoomsToFile(Slot_or_rooms[] rooms) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Slots.txt"))) {
            for (Slot_or_rooms room : rooms) {
                if (room != null) {
                    writer.write(room.Room_Id + "," +
                            room.roomType + "," +
                            room.Num_Of_Visitors);
                    writer.newLine();
                    // Save availability (28 days, 12 slots each day)
                    for (int i = 0; i < 28; i++) {
                        for (int j = 0; j < 12; j++) {
                            writer.write(room.Availability[i][j] ? "1" : "0");
                            if (j < 11) writer.write(",");
                        }
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving rooms to file.");
            e.printStackTrace();
        }
    }

    void displayVisitors() {
        System.out.println("Displaying all visitors with total hours aggregated:");

        // Get the user list from the AddingUser class
        ArrayList<User> userList = AddingUser.getUserList();

        // Check if the list is empty
        if (userList.isEmpty()) {
            System.out.println("No visitors found.");
            return;
        }

        // Map to store total hours grouped by user ID
        Map<Integer, Double> totalHoursMap = new HashMap<>();
        Map<Integer, String> nameMap = new HashMap<>();

        for (User user : userList) {
            int userId = user.getUserid();
            double totalHours = user.getTotalHours();

            // Update total hours in the map
            totalHoursMap.put(userId, totalHoursMap.getOrDefault(userId, 0.0) + totalHours);

            // Store the first and last name of the user for display
            if (!nameMap.containsKey(userId)) {
                nameMap.put(userId, "First Name: " + user.getFirstname() + ", Last Name: " + user.getLastname());
            }
        }

        // Convert the map to a list of entries for sorting
        List<Map.Entry<Integer, Double>> sortedEntries = new ArrayList<>(totalHoursMap.entrySet());
        sortedEntries.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())); // Sort by total hours descending

        // Display the aggregated result
        for (Map.Entry<Integer, Double> entry : sortedEntries) {
            int userId = entry.getKey();
            double totalHours = entry.getValue();
            System.out.println(nameMap.get(userId) + ", Total Hours: " + totalHours);
        }
    }


    public void most_hours() {
        if (AddingUser.userList.isEmpty()) {
            System.out.println("User list is empty. Please load users from file first.");
            return;
        }

        // Find the user with the maximum total hours
        User maxHoursUser = AddingUser.userList.get(0);
        for (User user : AddingUser.userList) {
            if (user.getTotalHours() > maxHoursUser.getTotalHours()) {
                maxHoursUser = user;
            }
        }

        // Add 1 hour to the total hours of the user with the maximum hours
        maxHoursUser.setTotalHours(maxHoursUser.getTotalHours() + 1);

        // Print the updated information of the user
        System.out.println("User with maximum hours (after adding 1 hour):");
        System.out.println("Name: " + maxHoursUser.getFirstname() + " " + maxHoursUser.getLastname());
        System.out.println("Total Hours: " + maxHoursUser.getTotalHours());

        // Save the updated user list to file
        saveUsersToFile();
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("User.txt"))) {
            for (User user : AddingUser.userList) {
                writer.write(user.getFirstname() + "," + user.getLastname() + "," + user.getUserid() + "," +
                        user.getEmail() + "," + user.getPassword() + "," + user.getType() + "," +
                        user.getReserve_id() + "," + user.getTotalHours() + "," + user.getDate() + "," +
                        user.getTime() + "," + user.getTimeStart() + "," + user.getTimeEnd() + "," +
                        user.getValue());
                writer.newLine();
            }
            System.out.println("User data updated in file.");
        } catch (IOException e) {
            System.out.println("Error writing user file.");
            e.printStackTrace();
        }
    }

    public void displayTotalAmount(Scanner scanner) {
        System.out.println("Select the user type to calculate the total amount (general, formal, instructor):");
        String selectedType = scanner.nextLine().trim().toLowerCase();

        while (!selectedType.equals("general") && !selectedType.equals("formal") && !selectedType.equals("instructor")) {
            System.out.println("Invalid type. Please enter one of (general, formal, instructor).");
            selectedType = scanner.nextLine().trim().toLowerCase();
        }

        double totalAmount = 0.0;

        for (User user : AddingUser.getUserList()) {
            if (user.getType().equalsIgnoreCase(selectedType)) {
                totalAmount += user.getValue();
            }
        }
    }

    public void updateEntity() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose what you want to update:");
        System.out.println("1 - Update User");
        System.out.println("2 - Update Reservation");
        System.out.println("3 - Update Slot");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                updateUser();
                break;
            case 2:
                updateReservation();
                break;
            case 3:
                updateSlot();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void updateUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the user ID you want to update:");
        int userId = scanner.nextInt();
        //scanner.nextLine(); // Consume the newline character

        boolean userFound = false;



        // Iterate through all users to find matches by user ID
        for (User user : AddingUser.userList) {
            if (user.getId() == userId) {
                userFound = true;

                System.out.println("What do you want to update?");
                System.out.println("1 - First Name");
                System.out.println("2 - Last Name");
                System.out.println("3 - ID");
                System.out.println("4 - Password");

                int updateChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (updateChoice) {
                    case 1:
                        System.out.println("Enter new first name:");
                        String newFirstName = scanner.nextLine();
                        // Update all users with the same ID
                        for (User u : AddingUser.userList) {
                            if (u.getId() == userId) {
                                u.setFirstName(newFirstName);
                            }
                        }
                        break;
                    case 2:
                        System.out.println("Enter new last name:");
                        String newLastName = scanner.nextLine();
                        for (User u : AddingUser.userList) {
                            if (u.getId() == userId) {
                                u.setLastName(newLastName);
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Enter new ID:");
                        int newId = scanner.nextInt();
                        // Update all users with the same ID
                        for (User u : AddingUser.userList) {
                            if (u.getId() == userId) {
                                u.setId(newId);
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Enter new password:");
                        String newPassword = scanner.nextLine();
                        for (User u : AddingUser.userList) {
                            if (u.getId() == userId) {
                                u.setPassword(newPassword);
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        return;
                }

                System.out.println("User updated successfully.");
                AddingUser.saveUsersToFile();
                break;
            }
        }

        if (!userFound) {
            System.out.println("User ID not found.");
        }
    }

    private void updateReservation() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Current reservations:");
        for (User user : AddingUser.userList) {
            System.out.println("Reservation ID: " + user.getReserve_id() +
                    ", Date: " + user.getDate() +
                    ", Time: " + user.getTime() +
                    ", Room ID: " + user.getRoomId());
        }

        System.out.println("Enter the reservation ID to update:");
        String reservationId = scanner.nextLine();

        boolean reservationFound = false;

        for (User user : AddingUser.userList) {
            if (user.getReserve_id().equals(reservationId)) {
                reservationFound = true;

                // Find the corresponding room
                Slot_or_rooms currentRoom = null;
                for (Slot_or_rooms room : Slot_or_rooms.Slots) {
                    if (room.Room_Id.equals(user.getRoomId())) {
                        currentRoom = room;
                        break;
                    }
                }

                if (currentRoom == null) {
                    System.out.println("Room not found. Update aborted.");
                    return;
                }

                // Revert old reservation slots
                int oldDate = Integer.parseInt(user.getDate()) - 1;
                int oldStart = Integer.parseInt(user.getTimeStart()) - 13;
                int oldEnd = Integer.parseInt(user.getTimeEnd()) - 13;

                for (int i = oldStart; i < oldEnd; i++) {
                    currentRoom.Availability[oldDate][i] = true; // Mark the slot as available
                }

                // Get new reservation details
                System.out.println("Enter new date (1-7):");
                int newDate = scanner.nextInt() - 1;

                System.out.println("Enter new start time (13-24):");
                int newStart = scanner.nextInt() - 13;

                System.out.println("Enter new end time (14-24):");
                int newEnd = scanner.nextInt() - 13;

                // Check if new slots are available
                for (int i = newStart; i < newEnd; i++) {
                    if (!currentRoom.Availability[newDate][i]) {
                        System.out.println("Time slot unavailable. Update aborted.");
                        return;
                    }
                }

                // Mark new slots as unavailable
                for (int i = newStart; i < newEnd; i++) {
                    currentRoom.Availability[newDate][i] = false; // Mark the slot as unavailable
                }

                // Update user reservation details
                user.setDate(String.valueOf(newDate + 1));
                user.setTimeStart(String.valueOf(newStart + 13));
                user.setTimeEnd(String.valueOf(newEnd + 13));
                user.setTime((newStart + 13) + ":00 - " + (newEnd + 13) + ":00");

                System.out.println("Reservation updated successfully.");

                // Save updated room availability back to file
                Slot_or_rooms.saveSlotsToFile(Slot_or_rooms.Slots);

                // Save updated user list to file
                AddingUser.saveUsersToFile();
                break;
            }
        }

        if (!reservationFound) {
            System.out.println("Reservation ID not found.");
        }
    }

    private void updateSlot() {
        Scanner scanner = new Scanner(System.in);

        // Display all available room IDs
        System.out.println("Available rooms:");
        for (Slot_or_rooms room : Slot_or_rooms.Slots) {
            System.out.println("Room ID: " + room.Room_Id + ", Type: " + room.roomType + ", Current Capacity: " + room.Num_Of_Visitors);
        }

        System.out.println("Enter the room ID to update the capacity:");
        String roomId = scanner.nextLine();

        // Find the room by ID
        Slot_or_rooms roomToUpdate = null;
        for (Slot_or_rooms room : Slot_or_rooms.Slots) {
            if (room.Room_Id.equalsIgnoreCase(roomId)) {
                roomToUpdate = room;
                break;
            }
        }

        if (roomToUpdate == null) {
            System.out.println("Room with ID " + roomId + " not found.");
            return;
        }

        System.out.println("Current capacity for room " + roomId + ": " + roomToUpdate.Num_Of_Visitors);
        System.out.println("Enter the new capacity:");
        int newCapacity = scanner.nextInt();

        // Update the capacity
        roomToUpdate.Num_Of_Visitors = newCapacity;

        System.out.println("Capacity updated successfully for room " + roomId + ".");

        // Save the updated room data back to the file
        Slot_or_rooms.saveSlotsToFile(Slot_or_rooms.Slots);
    }

}