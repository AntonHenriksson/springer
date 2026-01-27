package se.jensen.anton.springer.mapper;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.dto.CommentRequestDTO;
import se.jensen.anton.springer.dto.CommentResponseDTO;
import se.jensen.anton.springer.model.Comment;

@Component
public class CommentMapper {

    // Isn't this a method to get an existing comment from DB and then comvert the data to DTO? /Natsuki

    /**
     * This method converts {@link Comment} entity to a {@link CommentResponseDTO}
     *
     * @param comment {@link Comment} entity instance to be converted
     * @return {@link CommentResponseDTO} containing comment data
     */
    public CommentResponseDTO toDto(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getText(),
                comment.getCreated(),
                comment.getUser().getId(),
                comment.getUser().getUsername()
        );
    }

    /**
     *
     * @param dto {@link CommentRequestDTO}
     * @return
     */
    public Comment toEntity(CommentRequestDTO dto) {
        Comment comment = new Comment();
        updateEntity(dto, comment);

        return comment;
    }

    /* Method to create a new comment? /Natsuki
    public Comment fromDto(CommentRequestDTO dto){
        Comment comment = new Comment();
        createEntity(dto, comment);
        return comment;
    }
    */


    /**
     * This method updates a {@link Comment} entity with values from {@link CommentRequestDTO} values
     *
     * @param dto     {@link CommentRequestDTO} containing updated comment data
     * @param comment the existing {@link Comment} entity to which updated values are set
     */
    public void updateEntity(CommentRequestDTO dto, Comment comment) {
        comment.setText(dto.text());
        comment.setCreated(dto.created());
    }
}
