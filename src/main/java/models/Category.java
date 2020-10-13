package models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Category {
    String name;
    Category inherit;
}
