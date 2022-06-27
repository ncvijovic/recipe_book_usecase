package com.valcon.recipebook.exceptions;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
public class ErrorObject {

    private final String message;

    private final List<String> errors;

}
