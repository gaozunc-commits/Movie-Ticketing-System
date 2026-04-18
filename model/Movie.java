package model;

public class Movie {
    private String title;
    private String genre;
    private int duration;
    private String ageRating;

    // Parameterized constructor to initialize a movie with validated values.
    public Movie(String title, String genre, int duration, String ageRating) {
        setTitle(title);
        setGenre(genre);
        setDuration(duration);
        setAgeRating(ageRating);
    }

    // Getter for movie title.
    public String getTitle() {
        return title;
    }

    // Setter for movie title with basic validation.
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Movie title cannot be empty.");
        }
        this.title = title.trim();
    }

    // Getter for movie genre.
    public String getGenre() {
        return genre;
    }

    // Setter for movie genre with basic validation.
    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Genre cannot be empty.");
        }
        this.genre = genre.trim();
    }

    // Getter for movie duration in minutes.
    public int getDuration() {
        return duration;
    }

    // Setter for movie duration with basic validation.
    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new ArrayIndexOutOfBoundsException("Duration must be positive.");
        }
        this.duration = duration;
    }

    // Getter for movie age rating.
    public String getAgeRating() {
        return ageRating;
    }

    // Setter for movie age rating with basic validation.
    public void setAgeRating(String ageRating) {
        if (ageRating == null || ageRating.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Age rating cannot be empty.");
        }
        this.ageRating = ageRating.trim().toUpperCase();
    }

    public String toString() {
        return title + " | " + genre + " | " + duration + " mins | " + ageRating;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Movie)) {
            return false;
        }
        Movie other = (Movie) obj;
        if (duration != other.duration) {
            return false;
        }
        if (title == null ? other.title != null : !title.equals(other.title)) {
            return false;
        }
        if (genre == null ? other.genre != null : !genre.equals(other.genre)) {
            return false;
        }
        return ageRating == null ? other.ageRating == null : ageRating.equals(other.ageRating);
    }

    public int hashCode() {
        int result = 17;
        result = 31 * result + (title == null ? 0 : title.hashCode());
        result = 31 * result + (genre == null ? 0 : genre.hashCode());
        result = 31 * result + duration;
        result = 31 * result + (ageRating == null ? 0 : ageRating.hashCode());
        return result;
    }
}