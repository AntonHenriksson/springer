package se.jensen.anton.springer.dto;

import java.time.LocalDateTime;

/**
 * This DTO represents response for a post, which contains text, the timestamp when the post was created and id
 *
 * @param text    the text content of the post
 * @param created the timestamp when the post was created
 * @param id      ID of the post
 */
public record PostResponseDTO(String text, LocalDateTime created, Long id) {

}
