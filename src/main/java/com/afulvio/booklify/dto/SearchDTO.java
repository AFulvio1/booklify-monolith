package com.afulvio.booklify.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchDTO {
    private String keyword;
}
