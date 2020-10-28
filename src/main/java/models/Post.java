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
    private Long id;
    private User author;
    private Timestamp timestamp;
    private String image;
    private String description;
}
