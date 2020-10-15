package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class Comment {
    Long id;
    User author;
    Timestamp timestamp;
    Post post;
    Comment answerTo;
    String text;
}
