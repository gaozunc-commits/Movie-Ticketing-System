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
            throw new ArrayIndexOutOfBoundsException("Movie title cannot be empty.");
        }
        this.title = title.trim();
    }

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

    public int getDuration() {
        return duration;
    }


    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new ArrayIndexOutOfBoundsException("Duration must be positive.");
        }
        this.duration = duration;
    }

    public String getAgeRating() {
        return ageRating;
    }

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