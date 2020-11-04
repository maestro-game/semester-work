package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class Comment {
    private Long id;
    private User author;
    private Timestamp timestamp;
    private Post post;
    private Comment answerTo;
    private String text;
}
