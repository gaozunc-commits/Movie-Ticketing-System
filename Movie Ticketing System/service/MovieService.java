package service;

import model.Movie;
import util.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class MovieService {
    private static final String FILE_PATH = "data/movie.txt";
    private final List<Movie> movies;

    public MovieService() {
        this.movies = new ArrayList<>();
        load();
    }

    public void createMovie(Movie movie) {
        movies.add(movie);
        save();
    }

    public List<Movie> readAllMovies() {
        return new ArrayList<>(movies);
    }

    public Movie readMovieByIndex(int index) {
        if (index < 0 || index >= movies.size()) {
            throw new IllegalArgumentException("Movie index out of range.");
        }
        return movies.get(index);
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
        if (index < 0 || index >= movies.size()) {
            throw new IllegalArgumentException("Movie index out of range.");
        }
        movies.remove(index);
        save();
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Movie movie : movies) {
            lines.add(movie.getTitle() + "|" + movie.getGenre() + "|" + movie.getDuration() + "|" + movie.getAgeRating());
        }
        FileHandler.overwriteFile(FILE_PATH, lines);
    }

    private void load() {
        movies.clear();
        List<String> lines = FileHandler.readFromFile(FILE_PATH);
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length != 4) {
                    continue;
                }
                movies.add(new Movie(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]));
            } catch (Exception ex) {
                System.out.println("Skipping invalid movie record: " + line);
            }
        }
    }
}
