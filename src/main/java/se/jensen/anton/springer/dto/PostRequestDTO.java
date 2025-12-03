package se.jensen.anton.springer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record PostRequestDTO(@NotBlank String text,
                             @NotNull @PastOrPresent LocalDateTime created) {
}



