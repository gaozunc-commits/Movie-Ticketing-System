package model;

public class Movie {
    private String title;
    private String genre;
    private int duration;
    private String ageRating;

    public Movie(String title, String genre, int duration, String ageRating) {
        setTitle(title);
        setGenre(genre);
        setDuration(duration);
        setAgeRating(ageRating);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty.");
        }
        this.title = title.trim();
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty.");
        }
        this.genre = genre.trim();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
        this.duration = duration;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        if (ageRating == null || ageRating.trim().isEmpty()) {
            throw new IllegalArgumentException("Age rating cannot be empty.");
        }
        this.ageRating = ageRating.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return title + " | " + genre + " | " + duration + " mins | " + ageRating;
    }
}