package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class Post {
    Long id;
    User author;
    Timestamp timestamp;
    String image;
    String description;
}
