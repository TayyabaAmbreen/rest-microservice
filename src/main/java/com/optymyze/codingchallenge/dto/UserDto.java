package com.optymyze.codingchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    Long id;
    @NotNull
    String firstName;
    String surName;
    @NotNull
    String position;
    String gitHubProfileUrl;
}
