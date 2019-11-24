package com.legendApi.core.enums;


import org.springframework.core.convert.converter.Converter;

public class EnumConverter implements Converter<String, SortType> {
    @Override
    public SortType convert(String source) {
        try {
            return SortType.valueOf(source);
        } catch(Exception e) {
            return null;
        }
    }
}
