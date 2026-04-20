package service;

import util.Config;
import util.ConsoleUi;
import util.FileHandler;

public class MovieService {
    private static final String FILE_PATH = Config.MOVIE_FILE;
    private Movie[] movies;
    private int movieCount;

    public MovieService() {
        this.movies = new Movie[0];
        this.movieCount = 0;
        load();
    }

    public void createMovie(Movie movie) {
        movies = appendMovie(movies, movie);
        movieCount++;
        save();
    }

    public Movie[] readAllMovies() {
        Movie[] copied = new Movie[movieCount];
        for (int i = 0; i < movieCount; i++) {
            copied[i] = movies[i];
        }
        return copied;
    }

    public Movie readMovieByIndex(int index) {
        if (index < 0 || index >= movieCount) {
            throw new ArrayIndexOutOfBoundsException("Movie index out of range.");
        }
        return movies[index];
    }

    public void updateMovie(int index, String title, String genre, int duration, String ageRating) {
        Movie movie = readMovieByIndex(index);
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setDuration(duration);
        movie.setAgeRating(ageRating);
        save();
    }

    public void deleteMovie(int index) {
        if (index < 0 || index >= movieCount) {
            throw new ArrayIndexOutOfBoundsException("Movie index out of range.");
        }
        movies = removeMovieAt(movies, index);
        movieCount--;
        save();
    }

    private void save() {
        String[] lines = new String[movieCount];
        for (int i = 0; i < movieCount; i++) {
            Movie movie = movies[i];
            lines[i] = movie.getTitle() + "|" + movie.getGenre() + "|" + movie.getDuration() + "|" + movie.getAgeRating();
        }
        FileHandler.overwriteFile(FILE_PATH, lines);
    }

    private void load() {
        movies = new Movie[0];
        movieCount = 0;
        String[] lines = FileHandler.readFromFile(FILE_PATH);
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length != 4) {
                    continue;
                }
                movies = appendMovie(movies, new Movie(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]));
                movieCount++;
            } catch (Exception e) {
                System.out.println("Skipping invalid movie record: " + line);
            }
        }
    }

    private Movie[] appendMovie(Movie[] source, Movie movie) {
        Movie[] expanded = new Movie[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = movie;
        return expanded;
    }

    private Movie[] removeMovieAt(Movie[] source, int index) {
        Movie[] reduced = new Movie[source.length - 1];
        int target = 0;
        for (int i = 0; i < source.length; i++) {
            if (i == index) {
                continue;
            }
            reduced[target++] = source[i];
        }
        return reduced;
    }

    public void displayMovies() {
        Movie[] allMovies = readAllMovies();
        ConsoleUi.banner("MOVIES");

        System.out.printf("%-5s %-25s %-15s %-10s %-20s%n",
                "No.", "Title", "Genre", "Duration", "Movie ID");
        System.out.println("---");

        if (allMovies.length == 0) {
            System.out.println("No movies available.");
            return;
        }

        for (int i = 0; i < allMovies.length; i++) {
            Movie m = allMovies[i];

            System.out.printf("%-5d %-25s %-15s %-10s %-20s%n",
                    i + 1,
                    m.getTitle(),
                    m.getGenre(),
                    m.getDuration(),
                    m.getAgeRating());
        }
    }

    public static final class Movie {

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
}
