package br.ifsp.demo.domain.user;

import br.ifsp.demo.domain.user.entity.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private List<Rating> allRating;

    public User(UUID id, String name, List<Rating> allRating) {
        this.id = id;
        this.name = name;
        this.allRating = allRating;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Rating> getAllRating() {
        return new ArrayList<>(this.allRating);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", allRating=" + allRating +
                '}';
    }
}
