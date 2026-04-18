package service;

import model.Admin;
import model.Customer;
import model.Staff;
import model.User;
import util.FileHandler;

public class UserService {

    private static final String ADMIN_FILE = "data/admins.txt";
    private static final String STAFF_FILE = "data/staff.txt";
    private static final String CUSTOMER_FILE = "data/customers.txt";

    private User[] users;
    private int userCount;

    public UserService() {
        users = new User[0];
        userCount = 0;
        load();

        if (userCount == 0) {
            createUser(new Admin("admin", "123", "System Admin"));
            createUser(new Staff("staff", "123", "Staff One", 1001));
            createUser(new Customer("customer", "123", "Guest Customer"));
        }
    }

    // ================= CRUD =================

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
            throw new RuntimeException("User index out of range.");
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
            throw new RuntimeException("User index out of range.");
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
        throw new RuntimeException("Invalid username or password.");
    }

    // ================= SAVE =================

    private void save() {
        String[] adminLines = new String[userCount];
        String[] staffLines = new String[userCount];
        String[] customerLines = new String[userCount];

        int a = 0, s = 0, c = 0;

        for (int i = 0; i < userCount; i++) {
            User user = users[i];

            if (user instanceof Admin) {
                adminLines[a++] =
                        user.getUsername() + "|" +
                        user.getPassword() + "|" +
                        user.getName();

            } else if (user instanceof Staff) {
                Staff staff = (Staff) user;
                staffLines[s++] =
                        user.getUsername() + "|" +
                        user.getPassword() + "|" +
                        user.getName() + "|" +
                        staff.getStaffId();

            } else if (user instanceof Customer) {
                customerLines[c++] =
                        user.getUsername() + "|" +
                        user.getPassword() + "|" +
                        user.getName();
            }
        }

        FileHandler.overwriteFile(ADMIN_FILE, trim(adminLines, a));
        FileHandler.overwriteFile(STAFF_FILE, trim(staffLines, s));
        FileHandler.overwriteFile(CUSTOMER_FILE, trim(customerLines, c));
    }

    // ================= LOAD =================

    private void load() {
        loadAdmins();
        loadStaff();
        loadCustomers();
    }

    private void loadAdmins() {
    String[] lines = FileHandler.readFromFile(ADMIN_FILE);

    for (String line : lines) {
        try {
            String[] p = line.split("\\|");

            if (p.length >= 3) {
                users = appendUser(users,
                        new Admin(p[1], p[2], p[3]));
                userCount++;
            }
        } catch (Exception e) {
            System.out.println("Invalid admin record: " + line);
        }
    }
}

   private void loadStaff() {
    String[] lines = FileHandler.readFromFile(STAFF_FILE);

    for (String line : lines) {
        try {
            String[] p = line.split("\\|");

            if (p.length >= 4) {
                users = appendUser(users,
                        new Staff(p[1], p[2], p[3], Integer.parseInt(p[4])));
                userCount++;
            }
        } catch (Exception e) {
            System.out.println("Invalid staff record: " + line);
        }
    }
}

   private void loadCustomers() {
    String[] lines = FileHandler.readFromFile(CUSTOMER_FILE);

    for (String line : lines) {
        try {
            String[] p = line.trim().split("\\|");

            if (p.length >= 3) {
                users = appendUser(users,
                        new Customer(p[1], p[2], p[3]));
                userCount++;
            }
        } catch (Exception e) {
            System.out.println("Invalid customer record: " + line);
        }
    }
}

    // ================= HELPER =================

    private String[] trim(String[] arr, int size) {
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = arr[i];
        }
        return result;
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
            if (i != index) {
                reduced[target++] = source[i];
            }
        }
        return reduced;
    }

    // ================= DISPLAY =================

    public void displayUsers() {
        System.out.println("\n------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-12s %-20s%n", "No", "Username", "Role", "Name");
        System.out.println("------------------------------------------------------------");

        for (int i = 0; i < userCount; i++) {
            User u = users[i];
            System.out.printf("%-5d %-15s %-12s %-20s%n",
                    i + 1,
                    u.getUsername(),
                    u.getRole(),
                    u.getName());
        }

        System.out.println("------------------------------------------------------------");
    }
}