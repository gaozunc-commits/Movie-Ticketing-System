package model;

public abstract class User {

    private String username;
    private String password;
    private String name;
    private String role;

    protected User(String username, String password, String name, String role) {
        setUsername(username);
        setPassword(password);
        setName(name);
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.username = username.trim();
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.password = password;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name.trim();
    }

    public boolean login(String inputUser, String inputPass) {
        return this.username.equals(inputUser) && this.password.equals(inputPass);
    }

    public abstract void displayMenu();

    @Override
    public String toString() {
        return username + " | " + role + " | " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        if (username == null ? other.username != null : !username.equals(other.username)) {
            return false;
        }
        return role == null ? other.role == null : role.equals(other.role);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (username == null ? 0 : username.hashCode());
        result = 31 * result + (role == null ? 0 : role.hashCode());
        return result;
    }
}
