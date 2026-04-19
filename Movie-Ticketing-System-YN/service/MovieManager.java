package service;

import model.Movie;

public class MovieManager {
    private final MovieService movieService;

    public MovieManager() {
        this.movieService = new MovieService();
    }

    public void addMovie(Movie movie) {
        movieService.createMovie(movie);
    }

    public void displayMovies() {
        Movie[] movies = movieService.readAllMovies();
        if (movies.length == 0) {
            System.out.println("No movies currently showing.");
        } else {
            System.out.println("\n--- NOW SHOWING ---");
            for (int i = 0; i < movies.length; i++) {
                System.out.println((i + 1) + ". " + movies[i]);
            }
        }
    }

    public void updateMovieTitle(int index, String newTitle) {
        Movie movie = movieService.readMovieByIndex(index);
        movieService.updateMovie(index, newTitle, movie.getGenre(), movie.getDuration(), movie.getAgeRating());
    }

    public void removeMovie(int index) {
        movieService.deleteMovie(index);
    }

    public Movie getMovie(int index) {
        return movieService.readMovieByIndex(index);
    }
}