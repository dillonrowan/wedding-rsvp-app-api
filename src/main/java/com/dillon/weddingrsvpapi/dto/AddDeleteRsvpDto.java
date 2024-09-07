package com.dillon.weddingrsvpapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class AddDeleteRsvpDto {
    @NotEmpty
    @NotNull
    private List<String> names;
}
