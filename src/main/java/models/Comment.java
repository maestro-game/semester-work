package models;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class Comment {
    Long id;
    User author;
    Timestamp timestamp;
    Post post;
    Comment answerTo;
    String text;
}
