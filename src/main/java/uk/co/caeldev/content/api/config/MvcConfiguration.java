package uk.co.caeldev.content.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import uk.co.caeldev.content.api.features.common.ContentResourceInterceptor;
import uk.co.caeldev.content.api.features.content.ContentService;

@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ContentService contentService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new ContentResourceInterceptor(contentService)).addPathPatterns("/publisher/**/contents", "/publisher/**/contents/**");
        super.addInterceptors(registry);
    }
}
