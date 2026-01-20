package se.jensen.anton.springer.dto;


import jakarta.validation.constraints.PastOrPresent;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

public record CommentRequestDTO(
        @NonNull String text,
        @NonNull @PastOrPresent LocalDateTime created
) {
}
