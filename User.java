import java.util.List;
import java.util.ArrayList;
import java.util.UUID;


public class User {
    private UUID uuid;
    private List<Link> links;

    public User(UUID uuid) {
        this.uuid = uuid;
        this.links = new ArrayList<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<Link> getUserLinks() {
        return links;
    }
}
