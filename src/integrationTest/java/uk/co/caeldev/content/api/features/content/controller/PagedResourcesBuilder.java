package uk.co.caeldev.content.api.features.content.controller;

import com.google.common.collect.Lists;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import java.util.List;
import java.util.Map;

public class PagedResourcesBuilder<T> {

    private List<T> resources = Lists.newArrayList();
    private PagedResources.PageMetadata pageMetadata;

    private PagedResourcesBuilder() {
    }

    public static <T> PagedResourcesBuilder<T> pagedResourcesBuilder() {
        return new PagedResourcesBuilder<>();
    }

    public PagedResourcesBuilder<T> content(List<T> resources) {
        this.resources = resources;
        return this;
    }

    public PagedResourcesBuilder<T> page(Map<String, Integer> page) {
        pageMetadata = new PagedResources.PageMetadata(page.get("size"), page.get("number"), page.get("totalElements"), page.get("totalPages"));
        return this;
    }

    public PagedResources<T> build() {
        return new PagedResources<>(resources, pageMetadata, Lists.<Link>newArrayList());
    }
}
