package service;

import model.Admin;
import model.Customer;
import model.Staff;
import model.User;
import util.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String FILE_PATH = "data/users.txt";
    private final List<User> users;

    public UserService() {
        users = new ArrayList<>();
        load();
        if (users.isEmpty()) {
            users.add(new Admin("admin", "123", "System Admin"));
            users.add(new Staff("staff", "123", "Staff One", 1001));
            users.add(new Customer("customer", "123", "Guest Customer", true));
            save();
        }
    }

    public void createUser(User user) {
        users.add(user);
        save();
    }

    public List<User> readAllUsers() {
        return new ArrayList<>(users);
    }

    public User readUserByIndex(int index) {
        if (index < 0 || index >= users.size()) {
            throw new IllegalArgumentException("User index out of range.");
        }
        return users.get(index);
    }

    public void updateUserName(int index, String newName) {
        User user = readUserByIndex(index);
        user.setName(newName);
        save();
    }

    public void deleteUser(int index) {
        if (index < 0 || index >= users.size()) {
            throw new IllegalArgumentException("User index out of range.");
        }
        users.remove(index);
        save();
    }

    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.login(username, password)) {
                return user;
            }
        }
        throw new IllegalArgumentException("Invalid username or password.");
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Admin) {
                lines.add("ADMIN|" + user.getUsername() + "|" + user.getPassword() + "|" + user.getName());
            } else if (user instanceof Staff) {
                Staff staff = (Staff) user;
                lines.add("STAFF|" + user.getUsername() + "|" + user.getPassword() + "|" + user.getName() + "|" + staff.getStaffId());
            } else if (user instanceof Customer) {
                Customer customer = (Customer) user;
                lines.add("CUSTOMER|" + user.getUsername() + "|" + user.getPassword() + "|" + user.getName() + "|" + customer.isLoyaltyMember() + "|" + customer.getLoyaltyPoints());
            }
        }
        FileHandler.overwriteFile(FILE_PATH, lines);
    }

    private void load() {
        users.clear();
        List<String> lines = FileHandler.readFromFile(FILE_PATH);
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts[0].equals("ADMIN") && parts.length >= 4) {
                    users.add(new Admin(parts[1], parts[2], parts[3]));
                } else if (parts[0].equals("STAFF") && parts.length >= 5) {
                    users.add(new Staff(parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
                } else if (parts[0].equals("CUSTOMER") && parts.length >= 6) {
                    Customer customer = new Customer(parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]));
                    int points = Integer.parseInt(parts[5]);
                    customer.addLoyaltyPoints(points);
                    users.add(customer);
                }
            } catch (Exception ex) {
                System.out.println("Skipping invalid user record: " + line);
            }
        }
    }
}
