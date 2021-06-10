package com.shiroyk.shopsystem.entity;

import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Data
public class Image {
    private Set<String> names;

    public Image() {
    }

    public Image(Set<String> names) {
        this.names = names;
    }

    public static Image empty() {
        return new Image(Collections.emptySet());
    }
}
