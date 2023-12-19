package DTO;

import Model.Recipe;
import Model.Tag;

import java.util.Set;

public class TagDTO {
    public int id;
    public String name;

    public TagDTO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    public TagDTO() {
    }
}
