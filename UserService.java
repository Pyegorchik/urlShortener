import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class UserService {
    private final Map<UUID, User> users = new HashMap<>();

    public UUID getOrCreateUserUuid() {
        UUID uuid = UUID.randomUUID();
        users.putIfAbsent(uuid, new User(uuid));
        return uuid;
    }

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public boolean isUserUuidValid(UUID uuid) {
        return users.containsKey(uuid);
    }
}
