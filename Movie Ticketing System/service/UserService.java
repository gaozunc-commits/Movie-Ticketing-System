package service;

import model.Admin;
import model.Customer;
import model.Staff;
import model.User;
import util.FileHandler;

public class UserService {
    private static final String FILE_PATH = "data/users.txt";
    private User[] users;
    private int userCount;

    public UserService() {
        users = new User[0];
        userCount = 0;
        load();
        if (userCount == 0) {
            users = appendUser(users, new Admin("admin", "123", "System Admin"));
            userCount++;
            users = appendUser(users, new Staff("staff", "123", "Staff One", 1001));
            userCount++;
            users = appendUser(users, new Customer("customer", "123", "Guest Customer"));
            userCount++;
            save();
        }
    }

    public void createUser(User user) {
        users = appendUser(users, user);
        userCount++;
        save();
    }

    public User[] readAllUsers() {
        User[] copied = new User[userCount];
        for (int i = 0; i < userCount; i++) {
            copied[i] = users[i];
        }
        return copied;
    }

    public User readUserByIndex(int index) {
        if (index < 0 || index >= userCount) {
            throw new ArrayIndexOutOfBoundsException("User index out of range.");
        }
        return users[index];
    }

    public void updateUserName(int index, String newName) {
        User user = readUserByIndex(index);
        user.setName(newName);
        save();
    }

    public void deleteUser(int index) {
        if (index < 0 || index >= userCount) {
            throw new ArrayIndexOutOfBoundsException("User index out of range.");
        }
        users = removeUserAt(users, index);
        userCount--;
        save();
    }

    public User authenticate(String username, String password) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].login(username, password)) {
                return users[i];
            }
        }
        throw new ArrayIndexOutOfBoundsException("Invalid username or password.");
    }

    private void save() {
        String[] lines = new String[userCount];
        for (int i = 0; i < userCount; i++) {
            User user = users[i];
            if (user instanceof Admin) {
                lines[i] = "ADMIN|" + user.getUsername() + "|" + user.getPassword() + "|" + user.getName();
            } else if (user instanceof Staff) {
                Staff staff = (Staff) user;
                lines[i] = "STAFF|" + user.getUsername() + "|" + user.getPassword() + "|" + user.getName() + "|" + staff.getStaffId();
            } else if (user instanceof Customer) {
                Customer customer = (Customer) user;
                lines[i] = "CUSTOMER|" + user.getUsername() + "|" + user.getPassword() + "|" + user.getName();
            }
        }
        FileHandler.overwriteFile(FILE_PATH, lines);
    }

    private void load() {
        users = new User[0];
        userCount = 0;
        String[] lines = FileHandler.readFromFile(FILE_PATH);
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts[0].equals("ADMIN") && parts.length >= 4) {
                    users = appendUser(users, new Admin(parts[1], parts[2], parts[3]));
                    userCount++;
                } else if (parts[0].equals("STAFF") && parts.length >= 5) {
                    users = appendUser(users, new Staff(parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
                    userCount++;
                } else if (parts[0].equals("CUSTOMER") && parts.length >= 6) {
                    Customer customer = new Customer(parts[1], parts[2], parts[3]);
                    users = appendUser(users, customer);
                    userCount++;
                }
            } catch (Exception e) {
                System.out.println("Skipping invalid user record: " + line);
            }
        }
    }

    private User[] appendUser(User[] source, User user) {
        User[] expanded = new User[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = user;
        return expanded;
    }

    private User[] removeUserAt(User[] source, int index) {
        User[] reduced = new User[source.length - 1];
        int target = 0;
        for (int i = 0; i < source.length; i++) {
            if (i == index) {
                continue;
            }
            reduced[target++] = source[i];
        }
        return reduced;
    }

    public void displayUsers() {
        User[] allUsers = readAllUsers();
        System.out.println("\n--------------------------------------------------------------------------");
        System.out.printf("%-5s %-18s %-12s %-25s%n", "No.", "Username", "Role", "Name");
        System.out.println("--------------------------------------------------------------------------");
        if (allUsers.length == 0) {
            System.out.println("No users available.");
            System.out.println("--------------------------------------------------------------------------");
            return;
        }
        for (int i = 0; i < allUsers.length; i++) {
            User user = allUsers[i];
            System.out.printf("%-5d %-18s %-12s %-25s%n", i + 1, user.getUsername(), user.getRole(), user.getName());
        }
        System.out.println("--------------------------------------------------------------------------");
    }
}
