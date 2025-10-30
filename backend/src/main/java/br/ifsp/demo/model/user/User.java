package br.ifsp.demo.model.user;

import br.ifsp.demo.model.movie.Grade;
import br.ifsp.demo.model.movie.MovieId;
import br.ifsp.demo.exception.ReviewNotFoundException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User implements UserDetails {
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_ratings", joinColumns = @JoinColumn(name = "user_id"))
    private List<Rating> ratings;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public Rating addRating(MovieId movieId, Grade grade) {
        boolean alreadyExists = this.ratings.stream().anyMatch(rating ->
            rating.getMovieId().equals(movieId)
        );

        if (alreadyExists) return null;

        Rating rating = new Rating(movieId, grade, LocalDateTime.now());

        this.ratings.add(rating);
        return rating;
    }

    public Rating updateRating(MovieId movieId, Grade newGrade) {
        Optional<Rating> ratingToUpdate = this.ratings.stream()
                .filter(rating -> rating.getMovieId().equals(movieId))
                .findFirst();

        if (ratingToUpdate.isEmpty()) throw new EntityNotFoundException("Rating for this movie not found.");

        Rating foundRating = ratingToUpdate.get();
        foundRating.setGrade(newGrade);
        return foundRating;
    }

    public void deleteRating(MovieId movieId) {
        boolean removed = this.ratings.removeIf(rating -> rating.getMovieId().equals(movieId));
        if (!removed) {
            throw new ReviewNotFoundException("Rating for this movie not found.");
        }
    }
}
