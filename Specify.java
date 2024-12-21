import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Specify {
    private final AddingUser addingUser = new AddingUser();
    public String reserve_id;

    public void displayWelcomeBanner() {
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("*                                                WELCOME                                                *");
        System.out.println("*                                                  TO                                                   *");
        System.out.println("*                                      Basha Elbald COWORKING SPACE                                     *");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public String determine(Scanner scanner) {
        System.out.println("Welcome! Are you logging in as Admin or User?");
        String role = scanner.nextLine().trim().toLowerCase();

        if (role.equals("admin")) {
            if (loginAdmin()) {
                System.out.println("Welcome, Admin!");
                return "admin";
            } else {
                System.out.println("Invalid Admin credentials.");
                return determine(scanner);
            }
        } else if (role.equals("user")) {
            System.out.println("Do you want to Login or Sign Up? (login/signup)");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("login")) {
                if (loginUser()) {
                    System.out.println("Welcome back, User!");
                    return "user";
                } else {
                    System.out.println("Invalid User credentials.");
                    return determine(scanner);
                }
            } else if (choice.equals("signup")) {
                addingUser.signupUser();
                System.out.println("Sign up successful! Welcome to the system.");
                return "user";
            } else {
                System.out.println("Invalid choice. Please try again.");
                return determine(scanner);
            }
        } else {
            System.out.println("Invalid role. Please choose Admin or User.");
            return determine(scanner);
        }
    }

    private boolean loginAdmin() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Admin.txt"))) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter admin email: ");
            String email = scanner.nextLine();
            System.out.print("Enter admin password: ");
            String password = scanner.nextLine();

            String adminEmail = reader.readLine();
            String adminPassword = reader.readLine();

            return email.equals(adminEmail) && password.equals(adminPassword);
        } catch (IOException e) {
            System.out.println("Error reading admin file.");
            return false;
        }
    }

    private boolean loginUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();
        System.out.print("Enter user password: ");
        String password = scanner.nextLine();

        return addingUser.authenticateUser(email, password);
    }
}

class AddingUser {
    public static final ArrayList<User> userList = new ArrayList<>();
//    private final String fileName = "User.txt";

    // Constructor to load existing users and set up the shutdown hook
    public AddingUser() {
        loadUsersFromFile();
        //setupShutdownHook();
    }

    public static ArrayList<User> getUserList() {
        return userList;
    }

    // Load users from the file
    public static void loadUsersFromFile() {
        userList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("User.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 14) {
                    try {
                        String firstname = data[0].trim();
                        String lastname = data[1].trim();
                        int userid = Integer.parseInt(data[2].trim());
                        String email = data[3].trim();
                        String password = data[4].trim();
                        String type = data[5].trim();
                        String reserve_id = data[6].trim();
                        double totalHours = Double.parseDouble(data[7].trim());
                        String date = data[8].trim();
                        String time = data[9].trim();
                        String time_start = data[10].trim();
                        String time_end = data[11].trim();
                        double value = Double.parseDouble(data[12].trim());
                        String roomId = data[13].trim();
                        userList.add(new User(firstname, lastname, userid, email, password, type, reserve_id, totalHours, date, time, time_start, time_end, value , roomId));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format in line: " + line);
                    }
                } else {
                    System.out.println("Invalid data format in line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file.");
            e.printStackTrace();
        }
    }

    // Save the ArrayList data to the file

    public static void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("User.txt"))) {
            for (User user : userList) {
                // Make sure to include all 13 fields as per your file format
                writer.write(user.getFirstname() + "," +
                        user.getLastname() + "," +
                        user.getUserid() + "," +
                        user.getEmail() + "," +
                        user.getPassword() + "," +
                        user.getType() + "," +
                        user.getReserve_id() + "," +
                        user.getTotalHours() + "," +
                        user.getDate() + "," +
                        user.getTime() + "," +
                        user.getTimeStart() + "," +
                        user.getTimeEnd() + "," +
                        user.getValue() + "," +
                        user.getRoomId());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users to file.");
            e.printStackTrace();
        }
    }


    // Method for user signup
    public void signupUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter user first name: ");
        String fname = scanner.nextLine();

        System.out.print("Enter user last name: ");
        String lname = scanner.nextLine();

        System.out.print("Enter user Type (general, formal, instructor): ");
        String type = scanner.nextLine();
        while (!type.equalsIgnoreCase("general") && !type.equalsIgnoreCase("formal") && !type.equalsIgnoreCase("instructor")) {
            System.out.println("Invalid type. Please enter one of (general, formal, instructor).\nRe-enter user type: ");
            type = scanner.nextLine();
        }

        System.out.print("Enter user ID (4 digits): ");
        int id = scanner.nextInt();
        while (id < 1000 || id > 9999 || isIdExists(id)) {
            if (id < 1000 || id > 9999) {
                System.out.println("Invalid ID! It must be 4 digits.");
            } else {
                System.out.println("This ID is already used!");
            }
            System.out.print("Re-enter a new ID: ");
            id = scanner.nextInt();
        }
        scanner.nextLine(); // Consume leftover newline

        System.out.print("Enter user email: ");
        String email = scanner.nextLine();

        System.out.print("Enter user password: ");
        String password = scanner.nextLine();

        String reserve_id = "N/A";
        double totalHours = 0.0;

        String date = "N/A";
        String time = "N/A";
        String time_start = "N/A";
        String time_end = "N/A";
        double value = 0.0;
        String roomId = "N/A" ;
        User newUser = new User(fname, lname, id, email, password, type, reserve_id, totalHours, date, time, time_start, time_end, value , roomId);
        userList.add(newUser);

        System.out.println("User registered successfully!");

        // Save the updated user list to the file
        saveUsersToFile();
    }

    // Authenticate a user
    public boolean authenticateUser(String email, String password) {
        for (User user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Check if a user ID already exists
    private boolean isIdExists(int userId) {
        for (User user : userList) {
            if (user.getUserid() == userId) {
                return true;
            }
        }
        return false;
    }
}