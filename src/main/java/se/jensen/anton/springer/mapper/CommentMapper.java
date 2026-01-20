package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.CommentRequestDTO;
import se.jensen.anton.springer.dto.CommentResponseDTO;
import se.jensen.anton.springer.model.Comment;

@Component
public class CommentMapper {

    public CommentResponseDTO toDto(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getText(),
                comment.getCreated(),
                comment.getUser().getId(),
                comment.getUser().getUsername()
        );
    }

    public Comment toEntity(CommentRequestDTO dto) {
        Comment comment = new Comment();
        updateEntity(dto, comment);

        return comment;
    }

    public void updateEntity(CommentRequestDTO dto, Comment comment) {
        comment.setText(dto.text());
        comment.setCreated(dto.created());
    }
}
