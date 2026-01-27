package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * This DTO is used for creating or updating a post
 * It consists of content of the post and the timestamp when the post was created.
 *
 * @param text    Text of the post. This must not be blank
 * @param created Timestamp when the post was created.
 */
public record PostRequestDTO(
        @NotBlank String text,
        LocalDateTime created) {
}



