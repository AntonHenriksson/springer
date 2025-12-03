package se.jensen.anton.springer.dto;

import java.time.LocalDateTime;

public record PostRespondDTO(String text, LocalDateTime created, Long id) {

}
