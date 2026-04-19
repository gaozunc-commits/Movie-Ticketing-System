package service;

import model.Movie;

public class MovieManager {

    private final MovieService movieService;

    public MovieManager(MovieService movieService) {
        this.movieService = movieService;
    }

    public MovieManager() {
        this(new MovieService());
    }

    public void addMovie(Movie movie) {
        movieService.createMovie(movie);
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
