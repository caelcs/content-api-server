package uk.co.caeldev.content.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    /*@Autowired
    private ContentService contentService;
*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new ContentResourceInterceptor(contentService)).addPathPatterns("/contents", "/contents/**");
        super.addInterceptors(registry);
    }
}
