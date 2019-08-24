package com.myflx.validation.payload.dto;

import com.myflx.validation.annotation.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

public class Book {
    @NotEmpty(groups = {/*FirstLevelCheck.class, */Default.class})
    private String title;
    @Valid
    @NotNull
    private Author author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
