import java.time.LocalDateTime;

import java.util.UUID;


public class Link {
    private final String shortUrl;
    private final UUID userUuid;
    private final LocalDateTime createdAt;

    private String originalUrl;
    private int maxClicks;
    private LocalDateTime expirationTime;
    private int clickCount;

    public Link(String originalUrl, String shortUrl, UUID userUuid, int maxClicks, long expirationTime) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.userUuid = userUuid;
        this.createdAt = LocalDateTime.now();
        this.maxClicks = maxClicks;
        this.expirationTime = LocalDateTime.now().plusSeconds(expirationTime);
        this.clickCount = 0;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getMaxClicks() {
        return maxClicks;
    }

    public int getClickCount() {
        return clickCount;
    }

    public LocalDateTime getExpiryTime() {
        return expirationTime;
    }

    public void setClickCount(int count) {
        this.clickCount = count;
    }

    public void setMaxClicks(int count) {
        this.maxClicks = count;
    }

    public void setOriginalUrl(String url) {
        this.originalUrl = url;
    }

    public void setExpirationTime(long expTime) {
        this.expirationTime = LocalDateTime.now().plusSeconds(expTime);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }

    public boolean canClick() {
        return this.clickCount < maxClicks;
    }

    public void incrementClickCount() {
        if (canClick()) {
            this.clickCount++;
        } else {
            throw new IllegalStateException("Link's is max clicks reached.");
        }
    }
}