package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class Post {
    private Long id;
    private User author;
    private Timestamp timestamp;
    private String description;
    private String image;
    private Category specie;
}
