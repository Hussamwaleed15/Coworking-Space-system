import java.util.ArrayList;
import java.util.Scanner;
public class Choosing {

    public void displayMenuAdmin(Scanner scanner,ArrayList<Slot_or_rooms> rooms) {
        boolean continueAdmin = true; // Flag to control loop

        do {
            System.out.println("Welcome Admin! Choose an option:");
            System.out.println("1- Add Slots");
            System.out.println("2- Delete any entity");
            System.out.println("3- Display all available slots for all rooms");
            System.out.println("4- Display all rooms' data");
            System.out.println("5- Display all visitors");
            System.out.println("6- Total amount");
            System.out.println("7- Update any data of any entity");
            System.out.println("8- most hours");
            System.out.println("9- Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            Admin admin = new Admin();
            AddingUser addingUser = new AddingUser(); // Create instance to save data

            switch (choice) {
                case 1:
                    Slot_or_rooms.addRoom(rooms);
                    break;
                case 2:
                    admin.deleteEntity(AddingUser.userList, rooms);
                    break;
                case 3:
                    Slot_or_rooms.displayAdminAvailableSlotsForAdmin(rooms);
                    break;
                case 4:
                    Slot_or_rooms.displayRoomsData(rooms);
                    break;
                case 5:
                    admin.displayVisitors();
                    break;
                case 6:
                    admin.displayTotalAmount(scanner);
                    break;
                case 7:
                    admin.updateEntity();
                    break;
                case 8:
                   admin.most_hours();
                    break;
                case 9:
                    continueAdmin = false; // Exit loop
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            if (continueAdmin) {
                System.out.print("Do you want to perform another action? (yes/no): ");
                String response = scanner.nextLine();
                if (!response.equalsIgnoreCase("yes")) {
                    continueAdmin = false;
                }
            }
        } while (continueAdmin);
    }

    public void displayMenuUser(Scanner scanner, User user, ArrayList<Slot_or_rooms> rooms, Admin admin) {
        boolean continueUser = true;

        do {
            System.out.println("Welcome User! Choose an option:");
            System.out.println("1- Reserve");
            System.out.println("2- Cancel Reserve");
            System.out.println("3- Update");
            System.out.println("4- Display Available Slots");
            System.out.println("5- Bonus");
            System.out.println("6- Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    Reserve.reservation(rooms);
                    break;
                case 2:
                    Reserve.cancelReserve();
                    break;
                case 3:
                    Reserve.update(rooms);
                    break;
                case 4:
                    Slot_or_rooms.displayUserAvailableSlots(rooms);
                    break;
                case 5:
                    Reserve reserve = new Reserve();
                    reserve.bonus();
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            if (continueUser) {
                System.out.print("Do you want to perform another action? (yes/no): ");
                String response = scanner.nextLine();
                if (!response.equalsIgnoreCase("yes")) {
                    continueUser = false;
                }
            }
        } while (continueUser);

        System.out.println("Thank You And See You Soon");
    }
}