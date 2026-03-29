package service;

import model.Hall;
import model.Movie;
import util.FileHandler;
import java.util.ArrayList;
import java.util.List;

public class MovieManager {
    private List<Movie> movies;
    private final String FILE_PATH = "data/movie.txt";

    public MovieManager() {
        this.movies = new ArrayList<>();
        loadMovies();
    }

    // CREATE
    public void addMovie(Movie movie) {
        movies.add(movie);
        saveMovies();
    }

    // READ
    public void displayMovies() {
        if (movies.isEmpty()) {
            System.out.println("No movies currently showing.");
        } else {
            System.out.println("\n--- NOW SHOWING ---");
            for (int i = 0; i < movies.size(); i++) {
                System.out.println((i + 1) + ". " + movies.get(i).toString());
            }
        }
    }

    // UPDATE (Example: change title)
    public void updateMovieTitle(int index, String newTitle) {
        if (index >= 0 && index < movies.size()) {
            movies.get(index).setTitle(newTitle);
            saveMovies();
        }
    }

    // DELETE
    public void removeMovie(int index) {
        if (index >= 0 && index < movies.size()) {
            movies.remove(index);
            saveMovies();
        }
    }

    // Data Persistence Logic
    private void saveMovies() {
        List<String> data = new ArrayList<>();
        for (Movie m : movies) {
            // Format: Title|Genre|Duration|Rating
            data.add(m.getTitle() + "|" + m.getGenre() + "|" + 120 + "|" + "G"); 
        }
        FileHandler.saveToFile(FILE_PATH, data);
    }

    private void loadMovies() {
        List<String> lines = FileHandler.readFromFile(FILE_PATH);

        for (String line : lines) {
            // Step 1: Split the raw text line into its parts
            String[] data = line.split("\\|");
            
            if (data.length == 4) {
                // Step 2: Give the parts clear names (No more data[0], data[1] confusion)
                String name     = data[0];
                String category = data[1];
                int length      = Integer.parseInt(data[2]);
                String rating   = data[3];
                
                // Step 3: Create a Hall object (using dummy data for now)
                Hall movieHall = new Hall(101, 5, 5); 

                // Step 4: Combine everything    into a single Movie object
                Movie newMovie = new Movie(name, category, length, rating, movieHall);

                // Step 5: Add it to our master list
                movies.add(newMovie);
            }
        }
}

    public Movie getMovie(int index) {
    if (index >= 0 && index < movies.size()) {
        return movies.get(index);
    }
    return null;
}
}