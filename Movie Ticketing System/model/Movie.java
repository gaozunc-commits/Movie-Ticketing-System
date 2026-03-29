package model;

public class Movie {
    private String title;
    private String genre;
    private int durationMinutes;
    private String ageRating;
    private Hall hall;

    public Movie(String title, String genre, int durationMinutes, String ageRating, Hall hall) {
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.ageRating = ageRating;
        this.hall = hall; // This will be set when the movie is assigned to a hall 
    }

    // Getters and Setters 
    public Hall getHall() { return hall; }

    public String getTitle() { return title; }
    
    public String getGenre() { return genre; }

    public int getDurationMinutes() { return durationMinutes; }

    public String getAgeRating() { return ageRating; }

    public void setTitle(String title) { this.title = title; }

    public void setGenre(String genre) { this.genre = genre; }

    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }
    
    @Override
    public String toString() {
        return String.format("%s [%s] - %d mins", title, genre, durationMinutes);
    }
}