package com.optymyze.codingchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int statusCode;
    private Date timestamp;
    private String message;
}
