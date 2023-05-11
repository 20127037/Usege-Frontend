package com.group_1.usege.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadFileRequestDto {
    private int limit;
    private String[] attributes;
    Map<String, String> lastKey;
}
