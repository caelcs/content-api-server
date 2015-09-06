package uk.co.caeldev.content.api.features.content.repository;

import uk.co.caeldev.content.api.features.content.Content;

public interface ContentRepositoryBase {
    Content findOneByUUID(String uuid);
}
