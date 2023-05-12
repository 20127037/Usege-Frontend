package com.group_1.usege.utilities.mappers;

public interface Mapper<From, To> {
    To map(From value);
}