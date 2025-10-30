package br.ifsp.demo;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.security.auth.Role;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class AuraMoviesTddApi {

    public static void main(String[] args) {
        SpringApplication.run(AuraMoviesTddApi.class, args);
    }

    @Bean
    public CommandLineRunner initData(JpaMovieRepository movieRepository, 
                                     JpaUserRepository userRepository,
                                     PasswordEncoder passwordEncoder) {
        return args -> {
            // Only initialize if database is empty
            if (movieRepository.count() == 0 && userRepository.count() == 0) {
                initializeMockData(movieRepository, userRepository, passwordEncoder);
            }
        };
    }

    private void initializeMockData(JpaMovieRepository movieRepository, 
                                   JpaUserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
        
        // Create sample movies
        List<Movie> movies = createSampleMovies();
        movieRepository.saveAll(movies);
        
        // Create sample users
        List<User> users = createSampleUsers(passwordEncoder);
        userRepository.saveAll(users);
        
        // Add ratings to users
        addRatingsToUsers(users, movies, userRepository);
        
        System.out.println("Mock data initialized successfully!");
        System.out.println("Created " + movies.size() + " movies and " + users.size() + " users with ratings.");
    }

    private List<Movie> createSampleMovies() {
        List<Movie> movies = new ArrayList<>();
        
        // Action movies
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Dark Knight", Genre.ACTION));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Mad Max: Fury Road", Genre.ACTION));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "John Wick", Genre.ACTION));
        
        // Comedy movies
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Superbad", Genre.COMEDY));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Hangover", Genre.COMEDY));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Deadpool", Genre.COMEDY));
        
        // Drama movies
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Shawshank Redemption", Genre.DRAMA));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Forrest Gump", Genre.DRAMA));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Godfather", Genre.DRAMA));
        
        // Sci-Fi movies
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Blade Runner 2049", Genre.SCI_FI));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Interstellar", Genre.SCI_FI));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Matrix", Genre.SCI_FI));
        
        // Horror movies
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Conjuring", Genre.HORROR));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Hereditary", Genre.HORROR));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Get Out", Genre.HORROR));
        
        // Romance movies
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Notebook", Genre.ROMANCE));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Casablanca", Genre.ROMANCE));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "La La Land", Genre.ROMANCE));
        
        // Thriller movies
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Gone Girl", Genre.THRILLER));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Prisoners", Genre.THRILLER));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Se7en", Genre.THRILLER));
        
        return movies;
    }

    private List<User> createSampleUsers(PasswordEncoder passwordEncoder) {
        List<User> users = new ArrayList<>();
        
        // Admin user
        User admin = User.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .lastname("User")
                .email("admin@auramovies.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .ratings(new ArrayList<>())
                .build();
        users.add(admin);
        
        // Regular users
        User lucas = User.builder()
                .id(UUID.randomUUID())
                .name("Lucas")
                .lastname("Greatest of All Time (G.O.A.T.)")
                .email("lucas@gmail.com")
                .password(passwordEncoder.encode("senha"))
                .role(Role.USER)
                .ratings(new ArrayList<>())
                .build();
        users.add(lucas);

        User jane = User.builder()
                .id(UUID.randomUUID())
                .name("Jane")
                .lastname("Smith")
                .email("jane.smith@email.com")
                .password(passwordEncoder.encode("senha"))
                .role(Role.USER)
                .ratings(new ArrayList<>())
                .build();
        users.add(jane);
        
        User mike = User.builder()
                .id(UUID.randomUUID())
                .name("Mike")
                .lastname("Johnson")
                .email("mike.johnson@email.com")
                .password(passwordEncoder.encode("senha"))
                .role(Role.USER)
                .ratings(new ArrayList<>())
                .build();
        users.add(mike);
        
        User sarah = User.builder()
                .id(UUID.randomUUID())
                .name("Sarah")
                .lastname("Wilson")
                .email("sarah.wilson@email.com")
                .password(passwordEncoder.encode("senha"))
                .role(Role.USER)
                .ratings(new ArrayList<>())
                .build();
        users.add(sarah);


        User turing = User.builder()
                .id(UUID.randomUUID())
                .name("Alan")
                .lastname("Turing")
                .email("turing@gmail.com")
                .password(passwordEncoder.encode("senha"))
                .role(Role.USER)
                .ratings(new ArrayList<>())
                .build();
        users.add(turing);

        return users;
    }

    private void addRatingsToUsers(List<User> users, List<Movie> movies, JpaUserRepository userRepository) {
        // Lucas' ratings (likes action and comedy)
        User john = users.get(1);
        john.addRating(movies.get(0).getMovieId(), new Grade(5)); // The Dark Knight
        john.addRating(movies.get(1).getMovieId(), new Grade(4)); // Mad Max: Fury Road
        john.addRating(movies.get(3).getMovieId(), new Grade(4)); // Superbad
        john.addRating(movies.get(4).getMovieId(), new Grade(3)); // The Hangover
        john.addRating(movies.get(6).getMovieId(), new Grade(5)); // The Shawshank Redemption
        john.addRating(movies.get(9).getMovieId(), new Grade(4)); // Blade Runner 2049
        john.addRating(movies.get(12).getMovieId(), new Grade(2)); // The Conjuring (doesn't like horror)
        john.addRating(movies.get(15).getMovieId(), new Grade(3)); // The Notebook
        john.addRating(movies.get(18).getMovieId(), new Grade(4)); // Gone Girl
        
        // Jane's ratings (likes drama and romance)
        User jane = users.get(2);
        jane.addRating(movies.get(6).getMovieId(), new Grade(5)); // The Shawshank Redemption
        jane.addRating(movies.get(7).getMovieId(), new Grade(5)); // Forrest Gump
        jane.addRating(movies.get(8).getMovieId(), new Grade(4)); // The Godfather
        jane.addRating(movies.get(15).getMovieId(), new Grade(5)); // The Notebook
        jane.addRating(movies.get(16).getMovieId(), new Grade(4)); // Casablanca
        jane.addRating(movies.get(17).getMovieId(), new Grade(4)); // La La Land
        jane.addRating(movies.get(3).getMovieId(), new Grade(3)); // Superbad
        jane.addRating(movies.get(0).getMovieId(), new Grade(3)); // The Dark Knight
        jane.addRating(movies.get(9).getMovieId(), new Grade(2)); // Blade Runner 2049
        
        // Mike's ratings (likes sci-fi and thriller)
        User mike = users.get(3);
        mike.addRating(movies.get(9).getMovieId(), new Grade(5)); // Blade Runner 2049
        mike.addRating(movies.get(10).getMovieId(), new Grade(5)); // Interstellar
        mike.addRating(movies.get(11).getMovieId(), new Grade(4)); // The Matrix
        mike.addRating(movies.get(18).getMovieId(), new Grade(5)); // Gone Girl
        mike.addRating(movies.get(19).getMovieId(), new Grade(4)); // Prisoners
        mike.addRating(movies.get(20).getMovieId(), new Grade(4)); // Se7en
        mike.addRating(movies.get(0).getMovieId(), new Grade(4)); // The Dark Knight
        mike.addRating(movies.get(1).getMovieId(), new Grade(3)); // Mad Max: Fury Road
        mike.addRating(movies.get(6).getMovieId(), new Grade(3)); // The Shawshank Redemption
        
        // Sarah's ratings (mixed preferences)
        User sarah = users.get(4);
        sarah.addRating(movies.get(0).getMovieId(), new Grade(4)); // The Dark Knight
        sarah.addRating(movies.get(3).getMovieId(), new Grade(4)); // Superbad
        sarah.addRating(movies.get(6).getMovieId(), new Grade(5)); // The Shawshank Redemption
        sarah.addRating(movies.get(9).getMovieId(), new Grade(3)); // Blade Runner 2049
        sarah.addRating(movies.get(12).getMovieId(), new Grade(4)); // The Conjuring
        sarah.addRating(movies.get(15).getMovieId(), new Grade(4)); // The Notebook
        sarah.addRating(movies.get(18).getMovieId(), new Grade(3)); // Gone Girl
        sarah.addRating(movies.get(2).getMovieId(), new Grade(3)); // John Wick
        sarah.addRating(movies.get(5).getMovieId(), new Grade(4)); // Deadpool
        
        // Save all users with their ratings
        userRepository.saveAll(users);
    }
}
