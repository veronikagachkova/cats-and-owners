package CatsAndOwners.repository.cat;

import CatsAndOwners.model.entity.Cat;
import CatsAndOwners.model.enums.CatBreed;
import CatsAndOwners.model.enums.CatColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatRepository extends JpaRepository<Cat, UUID> {
    @Query(value = "SELECT c.* FROM cats c " +
            "JOIN cat_friendships cf ON c.cat_id = cf.friend_id " +
            "WHERE cf.cat_id = :catId", nativeQuery = true)
    List<Cat> findFriends(@Param("catId") UUID catId);

    List<Cat> findByColor(CatColor color);
    List<Cat> findByBreed(CatBreed breed);
}