import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Slot_or_rooms {

    //public static User[] userList;
    public String Room_Id;
    public String roomType;
    public int Num_Of_Visitors;
    public ArrayList<String> List_of_slots;
    public ArrayList<Integer> List_of_visitors;
    public boolean[][] Availability;

    public static int generalRooms=2;
    public static int formalRooms=3;
    public static int teachingRooms=3;

    public static ArrayList<Slot_or_rooms> Slots = new ArrayList<>();

    // Constructor
    public Slot_or_rooms(String Room_Id, String roomType, int Num_Of_Visitors, String Projector_type, String Board_type) {
        this.Room_Id = Room_Id;
        this.roomType = roomType;
        this.Num_Of_Visitors = Num_Of_Visitors;
        this.List_of_slots = new ArrayList<>();
        this.List_of_visitors = new ArrayList<>();
        this.Availability = new boolean[7][12]; // 7 days, 12 hours per day

        // Initialize availability to true
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 12; j++) {
                Availability[i][j] = true;
            }
        }
    }
    public Slot_or_rooms(String Room_Id, String roomType, int Num_Of_Visitors) {
        this.Room_Id = Room_Id;
        this.roomType = roomType;
        this.Num_Of_Visitors = Num_Of_Visitors;
        this.List_of_slots = new ArrayList<>();
        this.List_of_visitors = new ArrayList<>();
        this.Availability = new boolean[7][12]; // 7 days, 12 hours per day

        // Initialize availability to true
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 12; j++) {
                Availability[i][j] = true;
            }
        }
    }

    // Method to initialize predefined rooms
    public static ArrayList<Slot_or_rooms> initializeRooms() {
        ArrayList<Slot_or_rooms> rooms = new ArrayList<>();

        // Add 2 general rooms
        rooms.add(new Slot_or_rooms("G1", "general", 10, null, null));
        rooms.add(new Slot_or_rooms("G2", "general", 12, null, null));

        // Add 3 formal rooms
        rooms.add(new Slot_or_rooms("F1", "formal", 20, null, null));
        rooms.add(new Slot_or_rooms("F2", "formal", 15, null, null));
        rooms.add(new Slot_or_rooms("F3", "formal", 18, null, null));

        // Add 3 teaching rooms
        rooms.add(new Slot_or_rooms("T1", "teaching", 25, "4k Projector", "oversight board"));
        rooms.add(new Slot_or_rooms("T2", "teaching", 30, "Light Projector", "advisory board"));
        rooms.add(new Slot_or_rooms("T3", "teaching", 28, "Mini Ray Projector", "audit board"));

        return rooms;
    }

    // Method to load slots data from "Slots.txt"
    public static void loadSlotsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Slot.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    String roomId = data[0].trim();
                    String roomType = data[1].trim();
                    int numOfVisitors = Integer.parseInt(data[2].trim());
                    ArrayList<String> listOfSlots = parseList(data[3].trim());
                    ArrayList<Integer> listOfVisitors = parseIntegerList(data[4].trim());
                    boolean[][] availability = parseAvailability(data[5].trim());

                    Slot_or_rooms slot = new Slot_or_rooms(roomId, roomType, numOfVisitors, null, null);
                    slot.List_of_slots.addAll(listOfSlots);
                    slot.List_of_visitors.addAll(listOfVisitors);
                    slot.Availability = availability;

                    Slots.add(slot);
                } else {
                    System.out.println("Invalid slot data format. Skipping entry.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Slots file.");
            e.printStackTrace();
        }
    }

    // Method to save slots data to "Slots.txt"
    public static void saveSlotsToFile(ArrayList<Slot_or_rooms> rooms) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Slot.txt"))) {
            for (Slot_or_rooms slot : rooms) {
                writer.write(slot.Room_Id + "," +
                        slot.roomType + "," +
                        slot.Num_Of_Visitors + "," +
                        slot.List_of_slots + "," +
                        slot.List_of_visitors + "," +
                        formatAvailability(slot.Availability ) + "," +
                        slot.generalRooms + "," +
                        slot.formalRooms + "," +
                        slot.teachingRooms
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to Slots file.");
            e.printStackTrace();
        }
    }

    // Helper method to parse a list from a string
    private static ArrayList<String> parseList(String data) {
        ArrayList<String> list = new ArrayList<>();
        if (!data.equals("[]")) {
            String[] items = data.substring(1, data.length() - 1).split(";");
            for (String item : items) {
                list.add(item.trim());
            }
        }
        return list;
    }

    // Helper method to parse an integer list from a string
    private static ArrayList<Integer> parseIntegerList(String data) {
        ArrayList<Integer> list = new ArrayList<>();
        if (!data.equals("[]")) {
            String[] items = data.substring(1, data.length() - 1).split(";");
            for (String item : items) {
                list.add(Integer.parseInt(item.trim()));
            }
        }
        return list;
    }

    // Helper method to parse a 2D availability array from a string
    private static boolean[][] parseAvailability(String data) {
        boolean[][] availability = new boolean[7][12];
        String[] days = data.split(";");
        for (int i = 0; i < days.length; i++) {
            String[] slots = days[i].split(":");
            for (int j = 0; j < slots.length; j++) {
                availability[i][j] = slots[j].equals("1");
            }
        }
        return availability;
    }

    // Helper method to format the availability array to a string
    private static String formatAvailability(boolean[][] availability) {
        StringBuilder sb = new StringBuilder();
        for (boolean[] day : availability) {
            for (boolean slot : day) {
                sb.append(slot ? "1" : "0").append(":");
            }
            sb.deleteCharAt(sb.length() - 1).append(";");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void addRoom(ArrayList<Slot_or_rooms> rooms) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the type of room to add (general/formal/teaching):");
        String type = scanner.nextLine().toLowerCase();

        String roomId;
        int capacity;
        switch (type) {
            case "general":
                generalRooms++;  // Increment the static variable
                roomId = "G" + generalRooms;  // Use the incremented value
                System.out.println("Enter capacity for " + roomId + ":");
                capacity = scanner.nextInt();
                rooms.add(new Slot_or_rooms(roomId, "general", capacity, null, null));
                break;

            case "formal":
                formalRooms++;  // Increment the static variable
                roomId = "F" + formalRooms;  // Use the incremented value
                System.out.println("Enter capacity for " + roomId + ":");
                capacity = scanner.nextInt();
                rooms.add(new Slot_or_rooms(roomId, "formal", capacity, null, null));
                break;

            case "teaching":
                teachingRooms++;  // Increment the static variable
                roomId = "T" + teachingRooms;  // Use the incremented value
                System.out.println("Enter capacity for " + roomId + ":");
                capacity = scanner.nextInt();
                System.out.println("Enter Projector type for " + roomId + ":");
                String pType = scanner.next();
                System.out.println("Enter Board type for " + roomId + ":");
                String bType = scanner.next();
                rooms.add(new Slot_or_rooms(roomId, "teaching", capacity, pType , bType));
                break;

            default:
                System.out.println("Invalid room type.");
                return;
        }



        System.out.println("Room added successfully!");
    }

    public static void displayUserAvailableSlots(ArrayList<Slot_or_rooms> rooms) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your ID: ");
        int id = scanner.nextInt();

        // Check if the user exists
        String userType = null;
        boolean userFound = false;
        for (User user : AddingUser.getUserList()) {
            if (id == user.getUserid()) {
                userType = user.getType();
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            System.out.println("User ID not found.");
            return;
        }
        // Map user types to room types
        String roomType;
        switch (userType.toLowerCase()) {
            case "general":
                roomType = "general";
                break;
            case "teaching":
                roomType = "teaching";
                break;
            case "formal":
                roomType = "formal";
                break;
            default:
                System.out.println("No rooms available for this user type.");
                return;
        }

        // Prompt user for the desired date (day of the week)
        System.out.println("Enter the date : ");
        int date;
        try {
            date = scanner.nextInt();
            if (date < 1 || date > 7) {
                System.out.println("Invalid date. Please enter a number between 1 and 7.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 7.");
            return;
        }

        // Display available slots
        System.out.println("Available " + roomType + " rooms for date " + date + ":");
        boolean availableRoomsFound = false;

        for (Slot_or_rooms room : rooms) {
            if (room != null && room.roomType.equalsIgnoreCase(roomType)) {
                System.out.println("Room ID: " + room.Room_Id);

                boolean isAvailable = false;
                System.out.print("Available hours: ");

                for (int hour = 0; hour < 12; hour++) {
                    if (room.Availability[date - 1][hour]) {
                        int startTime = hour + 13; // Convert to 24-hour format
                        System.out.print(startTime + ":00 ");
                        isAvailable = true;
                    }
                }

                if (isAvailable) {
                    availableRoomsFound = true;
                    System.out.println(); // Print newline after the hours
                } else {
                    System.out.println("No available slots for this room.");
                }
            }
        }

        if (!availableRoomsFound) {
            System.out.println("No available " + roomType + " rooms for the selected date.");
        }
    }

    public static void displayAdminAvailableSlotsForAdmin(ArrayList<Slot_or_rooms> rooms) {
        Scanner scanner = new Scanner(System.in);

        // Prompt admin to enter the room type they want to view
        System.out.println("Enter the type of room to display (general/formal/teaching):");
        String roomType = scanner.nextLine().toLowerCase();

        // Validate the room type
        if (!roomType.equals("general") && !roomType.equals("formal") && !roomType.equals("teaching")) {
            System.out.println("Invalid room type. Please enter 'general', 'formal', or 'teaching'.");
            return;
        }

        // Prompt admin for the desired date (day of the week)
        System.out.println("Enter the date (between 1 and 7) : ");
        int date;
        try {
            date = scanner.nextInt();
            if (date < 1 || date > 7) {
                System.out.println("Invalid date. Please enter a number between 1 and 7.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 7.");
            return;
        }

        // Display available slots for the chosen room type
        System.out.println("Available " + roomType + " rooms for date " + date + ":");
        boolean availableRoomsFound = false;

        for (Slot_or_rooms room : rooms) {
            if (room != null && room.roomType.equalsIgnoreCase(roomType)) {
                System.out.println("Room ID: " + room.Room_Id);

                boolean isAvailable = false;
                System.out.print("Available hours: ");

                for (int hour = 0; hour < 12; hour++) {
                    if (room.Availability[date - 1][hour]) {
                        int startTime = hour + 13; // Convert to 24-hour format
                        System.out.print(startTime + ":00 ");
                        isAvailable = true;
                    }
                }

                if (isAvailable) {
                    availableRoomsFound = true;
                    System.out.println(); // Print newline after the hours
                } else {
                    System.out.println("No available slots for this room.");
                }
            }
        }

        if (!availableRoomsFound) {
            System.out.println("No available " + roomType + " rooms for the selected date.");
        }
    }

    public static void displayRoomsData(ArrayList<Slot_or_rooms> rooms) {
        Scanner scanner = new Scanner(System.in);

        // Prompt admin for the desired room type
        System.out.println("Enter the room type (e.g., general, formal, teaching):");
        String roomType = scanner.nextLine().toLowerCase();

        // Display matching rooms
        System.out.println("Rooms of type: " + roomType);
        boolean roomFound = false;

        for (Slot_or_rooms room : rooms) {
            if (room != null && room.roomType.equalsIgnoreCase(roomType)) {
                roomFound = true;

                // Display room details
                System.out.println("Room ID: " + room.Room_Id);
                System.out.println("Number of Visitors: " + room.Num_Of_Visitors);

                System.out.println("Availability:");
                for (int day = 0; day < 7; day++) {
                    System.out.print("Day " + (day + 1) + ": ");
                    for (int hour = 0; hour < 12; hour++) {
                        System.out.print((room.Availability[day][hour] ? "1" : "0") + " ");
                    }
                    System.out.println();
                }
                System.out.println("-----------------------");
            }
        }

        if (!roomFound) {
            System.out.println("No rooms found for the type: " + roomType);
        }
    }
}