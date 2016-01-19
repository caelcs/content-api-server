package uk.co.caeldev.content.api.features.content.repository;

import uk.co.caeldev.content.api.features.content.Content;
import uk.co.caeldev.content.api.features.content.ContentStatus;

public interface ContentRepositoryBase {
    Content findOneByUUID(String uuid);

    Content updateStatus(String contentUUID, ContentStatus contentStatusNew);
}
