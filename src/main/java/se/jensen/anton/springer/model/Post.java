package se.jensen.anton.springer.model;

import java.time.LocalDateTime;


public class Post {
    private Long id = 0L;
    private String text;
    private LocalDateTime created;

    public Post(String text, LocalDateTime created) {
        this.text = text;
        this.created = created;
    }

    public Post() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
