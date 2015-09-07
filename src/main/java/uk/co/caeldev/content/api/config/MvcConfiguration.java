package uk.co.caeldev.content.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import uk.co.caeldev.content.api.features.content.ContentResourceInterceptor;
import uk.co.caeldev.content.api.features.content.ContentService;
import uk.co.caeldev.content.api.features.publisher.PublisherResourceInterceptor;
import uk.co.caeldev.content.api.features.publisher.PublisherService;

@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    @Lazy
    private ContentService contentService;

    @Autowired
    @Lazy
    private PublisherService publisherService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ContentResourceInterceptor(contentService)).addPathPatterns("/publisher/**/contents", "/publisher/**/contents/**");
        registry.addInterceptor(new PublisherResourceInterceptor(publisherService)).addPathPatterns("/publishers", "/publishers/**");
        super.addInterceptors(registry);
    }
}
