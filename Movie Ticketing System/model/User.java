package model;

public class User {
    protected String username;
    protected String password;
    protected String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // --- YOUR ORIGINAL LOGIN METHOD ---
    public boolean login(String inputUser, String inputPass) {
        return this.username.equals(inputUser) && this.password.equals(inputPass);
    }

    // --- GETTERS (For Encapsulation Marks) ---
    public String getRole() { return role; }
    
    public String getUsername() { return username; }

    // --- POLYMORPHISM OPPORTUNITY ---
    // Adding a generic method that Staff and Admin will change (Override)
    public void displayMenu() {
        System.out.println("Welcome, " + username + ". Access level: " + role);
    }
}