package com.example.video_chat.common;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollUtils {
    public static <U, T>List<T> toList(Collection<U> t, Function<U, T> function) {
        if(CollectionUtils.isEmpty(t)) {
            return Collections.emptyList();
        }
        return t.stream().map(function).toList();
    }
    public static <U, T> Set<T> toSet(Collection<U> t, Function<U, T> function) {
        if(CollectionUtils.isEmpty(t)) {
            return Collections.emptySet();
        }
        return t.stream().map(function).collect(Collectors.toSet());
    }
}
