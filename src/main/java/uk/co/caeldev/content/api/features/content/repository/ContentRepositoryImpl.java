package uk.co.caeldev.content.api.features.content.repository;

import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.content.Content;

@Component
public class ContentRepositoryImpl implements ContentRepositoryBase {

    @Override
    public Content findOneByUUID(String uuid) {
        return null;
    }
}
