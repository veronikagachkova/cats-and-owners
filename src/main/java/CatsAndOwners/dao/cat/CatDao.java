package CatsAndOwners.dao.cat;

import CatsAndOwners.model.entity.Cat;

import java.util.List;
import java.util.UUID;

public interface CatDao {
    void create(Cat cat);
    Cat findOne(UUID id);
    List<Cat> findFriends(UUID id);
    List<Cat> findMany();
    List<Cat> findAllByIds(List<UUID> ids);
    void update(Cat cat);
    void delete(UUID id);
}