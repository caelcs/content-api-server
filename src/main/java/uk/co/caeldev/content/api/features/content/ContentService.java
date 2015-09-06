package uk.co.caeldev.content.api.features.content;

public interface ContentService {
    Content publish(String content, String publisherId);

    Content findOneByUUID(String uuid);
}
