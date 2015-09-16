package uk.co.caeldev.content.api.features.common;

import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.List;

public class PageBuilder<T> {

    private List<T> entities = Lists.newArrayList();

    private PageBuilder() {
    }

    public static <T> PageBuilder<T> pageBuilder() {
        return new PageBuilder<>();
    }

    public PageBuilder<T> page(T... entities) {
        this.entities.addAll(Arrays.asList(entities));
        return this;
    }

    public Page<T> build() {
        return new PageImpl<>(entities);
    }
}
