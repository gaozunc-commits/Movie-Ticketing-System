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

    private User[] users = new User[0];
    private int userCount = 0;

    public UserService() {
        load();

        // default users if empty
        if (userCount == 0) {
            createUser(new Admin("admin", "123", "System Admin"));
            createUser(new Staff("staff", "1234567", "Staff One", 1001));
            createUser(new Customer("customer", "123", "Guest Customer"));
        }
    }

    // ================= LOGIN =================

    public User authenticate(String username, String password) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].login(username, password)) {
                return users[i];
            }
        }
        return null;
    }

    public boolean usernameExists(String username) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    // ================= CREATE =================

    public void createUser(User user) {
        if (usernameExists(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        users = append(users, user);
        userCount++;
        save();
    }

    // ================= READ =================

    public User[] readAllUsers() {
        User[] copy = new User[userCount];
        for (int i = 0; i < userCount; i++) {
            copy[i] = users[i];
        }
        return copy;
    }

    public User readUserByIndex(int index) {
        if (index < 0 || index >= userCount) {
            throw new RuntimeException("Invalid index");
        }
        return users[index];
    }

    // ================= UPDATE =================

    public void updateUserName(int index, String newName) {
        User u = readUserByIndex(index);
        u.setName(newName);
        save();
    }

    // ================= DELETE =================

    public void deleteUser(int index) {
        User[] newArr = new User[userCount - 1];

        int j = 0;
        for (int i = 0; i < userCount; i++) {
            if (i != index) {
                newArr[j++] = users[i];
            }
        }

        users = newArr;
        userCount--;
        save();
    }

    // ================= SAVE =================

    private void save() {

        String[] adminLines = new String[userCount];
        String[] staffLines = new String[userCount];
        String[] customerLines = new String[userCount];

        int a = 0, s = 0, c = 0;

        for (int i = 0; i < userCount; i++) {

            User u = users[i];

            if (u instanceof Admin) {
                adminLines[a++] =
                        "ADMIN|" +
                        u.getUsername() + "|" +
                        u.getPassword() + "|" +
                        u.getName();

            } else if (u instanceof Staff) {
                Staff st = (Staff) u;
                staffLines[s++] =
                        "STAFF|" +
                        st.getUsername() + "|" +
                        st.getPassword() + "|" +
                        st.getName() + "|" +
                        st.getStaffId();

            } else if (u instanceof Customer) {
                customerLines[c++] =
                        "CUSTOMER|" +
                        u.getUsername() + "|" +
                        u.getPassword() + "|" +
                        u.getName();
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
            String[] p = line.split("\\|");

            if (p.length >= 4) {
                users = append(users,
                        new Admin(
                                p[1], // username
                                p[2], // password
                                p[3]  // name
                        ));
                userCount++;
            }
        }
    }

    private void loadStaff() {
        String[] lines = FileHandler.readFromFile(STAFF_FILE);

        for (String line : lines) {
            String[] p = line.split("\\|");

            if (p.length >= 5) {
                users = append(users,
                        new Staff(
                                p[1],
                                p[2],
                                p[3],
                                Integer.parseInt(p[4])
                        ));
                userCount++;
            }
        }
    }

    private void loadCustomers() {
        String[] lines = FileHandler.readFromFile(CUSTOMER_FILE);

        for (String line : lines) {
            String[] p = line.split("\\|");

            if (p.length >= 4) {
                users = append(users,
                        new Customer(
                                p[1],
                                p[2],
                                p[3]
                        ));
                userCount++;
            }
        }
    }

    // ================= HELPERS =================

    private User[] append(User[] arr, User u) {
        User[] newArr = new User[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }
        newArr[arr.length] = u;
        return newArr;
    }

    private String[] trim(String[] arr, int size) {
        String[] res = new String[size];
        for (int i = 0; i < size; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    // ================= DISPLAY =================

    public void displayUsers() {
        System.out.println("\nNo | Username | Role | Name");
        System.out.println("--------------------------------");

        for (int i = 0; i < userCount; i++) {
            User u = users[i];
            System.out.printf("%d | %s | %s | %s%n",
                    i + 1,
                    u.getUsername(),
                    u.getRole(),
                    u.getName());
        }
    }
}