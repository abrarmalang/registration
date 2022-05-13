package com.school.registration.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;


@NoArgsConstructor
@Data
public class FilterParam {
    Map<String, String> filter = new HashMap<>();
}