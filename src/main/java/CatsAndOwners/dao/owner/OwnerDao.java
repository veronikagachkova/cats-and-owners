package CatsAndOwners.dao.owner;

import CatsAndOwners.model.entity.Owner;

import java.util.List;
import java.util.UUID;

public interface OwnerDao {
    void create(Owner owner);
    Owner findOne(UUID id);
    List<Owner> findMany();
    void update(Owner owner);
    void delete(UUID id);
}