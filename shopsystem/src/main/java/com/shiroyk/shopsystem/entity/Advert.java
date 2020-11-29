package com.shiroyk.shopsystem.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "advert")
public class Advert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image = "[]";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Image[] getImage(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(image, Image[].class);
        } catch (JsonProcessingException ignore) {
            return null;
        }
    }

    public void setImage(String image) {
        this.image = image;
    }
}
