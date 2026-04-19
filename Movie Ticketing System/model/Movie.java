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

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty.");
        }
        this.title = title.trim();
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty.");
        }
        this.genre = genre.trim();
    }

    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
        this.duration = duration;
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

    @Override
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

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (title == null ? 0 : title.hashCode());
        result = 31 * result + (genre == null ? 0 : genre.hashCode());
        result = 31 * result + duration;
        result = 31 * result + (ageRating == null ? 0 : ageRating.hashCode());
        return result;
    }
}
