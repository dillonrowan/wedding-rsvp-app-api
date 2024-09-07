package com.dillon.weddingrsvpapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddDeleteRsvpDto {
    @NotEmpty
    @NotNull
    private List<String> names;
}
