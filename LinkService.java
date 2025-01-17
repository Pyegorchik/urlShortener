import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.UUID;

public class LinkService {
    private final ConfigLoader configLoader;

    public LinkService(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    private final Map<String, Link> linkStorage = new HashMap<>();

    public String createShortLink(String originalUrl, UUID userUuid, int maxClicks, long expiryTime) {
        String shortUrl = generateShortUrl();

        expiryTime = Math.min(expiryTime, configLoader.getValue("expiryTime"));
        maxClicks = Math.max(maxClicks, configLoader.getValue("maxClicks"));

        Link link = new Link(originalUrl, shortUrl, userUuid, maxClicks, expiryTime);
        linkStorage.put(shortUrl, link);
        return shortUrl;
    }

    public void isLinkValid(Link link) {
        if (link == null || link.isExpired() || !link.canClick()) {
            throw new IllegalArgumentException("URL is expired, invalid, or max clicks reached.");
        };
    }

    public String getOriginalUrl(String shortUrl) {
        Link link = linkStorage.get(shortUrl);

        try {
            isLinkValid(link);
        } catch (IllegalArgumentException e) {
            deleteLink(shortUrl);
            throw new IllegalArgumentException("URL is expired or max cliks amount is reached. \n URL was deleted.");
        }

        link.incrementClickCount();
        return link.getOriginalUrl();
    }

    private String generateShortUrl() {
        return "clck.ru/" + UUID.randomUUID().toString().substring(0, 8);
    }

    public Link getLinkByShortUrl(String shortUrl) {
        return linkStorage.get(shortUrl);
    }

    public boolean updateLink(Link link, int newMaxClicks, long newExpirationSeconds) {      
        if (link != null) {
            link.setMaxClicks(newMaxClicks);
            link.setExpirationTime(newExpirationSeconds);
            return true;
        }
        return false;
    }

    public boolean deleteLink(String shortUrl) {
        return linkStorage.remove(shortUrl) != null;
    }

    public List<Link> getLinksByUser(UUID userUuid) {
        return linkStorage.values().stream()
                .filter(link -> link.getUserUuid().equals(userUuid))
                .collect(Collectors.toList());
    }

}
