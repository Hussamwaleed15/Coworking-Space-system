import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Slot_or_rooms> rooms;
        Slot_or_rooms.loadSlotsFromFile();
        if (Slot_or_rooms.Slots.isEmpty()) {
            rooms = Slot_or_rooms.initializeRooms();
            Slot_or_rooms.Slots.addAll(rooms);
        } else {
            rooms = Slot_or_rooms.Slots;
        }
        Scanner scanner = new Scanner(System.in);
        Specify specify = new Specify();
        specify.displayWelcomeBanner();
        String role = specify.determine(scanner);
        Admin admin = null;
        if (role.equalsIgnoreCase("admin")) {
            Choosing choose = new Choosing();
            admin = new Admin();
            choose.displayMenuAdmin(scanner , rooms);
        } else if (role.equalsIgnoreCase("user")) {
            // Capture the current date and time
            String date = LocalDate.now().toString();
            String time = LocalTime.now().toString();


            double totalHours = 0;
            String time_start = "";
            String time_end = "";
            double value = 0.0;
            String roomId = "";
            User user = new User("Name", "NAMe", 11234, "name@example.com", "password1234", "general", "RES123", totalHours, date, time, time_start, time_end, value , roomId);

            Choosing choose = new Choosing();
            choose.displayMenuUser(scanner, user, rooms, admin);
        } else {
            System.out.println("Invalid role. Exiting the program.");
        }


        AddingUser addingUser = new AddingUser();
        addingUser.saveUsersToFile();
        Slot_or_rooms.saveSlotsToFile(rooms);
    }
}
