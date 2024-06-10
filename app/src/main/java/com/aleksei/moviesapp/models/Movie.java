package com.aleksei.moviesapp.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Movie {
    private String title;
    private String posterUrl;
    private String genre;
    private int year;
}
