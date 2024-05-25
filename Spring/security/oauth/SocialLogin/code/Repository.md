# Social Login - 코드 구현 - Repository

```java
public interface UserRepository {

    User findByUsername(String username);
    void register(User user);
}
```
```java
@Repository
public class UserRepositoryImpl implements UserRepository{

    private Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User findByUsername(String username) {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        return null;
    }

    @Override
    public void register(User user) {
        if (users.containsKey(user.getUsername())) {
            return;
        }
        users.put(user.getUsername(), user);
    }
}
```
