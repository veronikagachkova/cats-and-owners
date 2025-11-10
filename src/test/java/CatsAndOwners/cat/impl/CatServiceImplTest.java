package CatsAndOwners.cat.impl;

import CatsAndOwners.dao.cat.CatDao;
import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;
import CatsAndOwners.model.entity.Cat;
import CatsAndOwners.model.entity.Owner;
import CatsAndOwners.model.enums.*;
import CatsAndOwners.service.cat.impl.CatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CatServiceImplTest {
    private CatDao catDao;
    private CatServiceImpl catService;

    @BeforeEach
    public void setUp() {
        catDao = mock(CatDao.class);
        catService = new CatServiceImpl(catDao);
    }

    @Test
    void givenCreateCatDto_whenCreateCat_thenCallsDaoCreateWithEntity() {
        // given
        CreateCatDto dto = new CreateCatDto();
        dto.setName("Tom");

        // when
        catService.createCat(dto);

        // then
        verify(catDao).create(any(Cat.class));
    }

    @Test
    void givenCatId_whenGetCatById_thenReturnsCorrectCatResponseDto() {
        // given
        UUID catId = UUID.randomUUID();
        Owner owner = new Owner();
        owner.setId(UUID.randomUUID());
        owner.setName("Alice");

        Cat friend = new Cat();
        friend.setId(UUID.randomUUID());
        friend.setName("Jerry");

        Cat cat = new Cat();
        cat.setId(catId);
        cat.setName("Tom");
        cat.setGender(Gender.MALE);
        cat.setBirthDate(LocalDate.of(2020, 1, 1));
        cat.setBreed(CatBreed.BRITISH);
        cat.setColor(CatColor.GRAY);
        cat.setOwner(owner);
        cat.setFriends(List.of(friend));

        when(catDao.findOne(catId)).thenReturn(cat);

        // when
        CatResponseDto result = catService.getCatById(catId);

        // then
        assertEquals(catId, result.id());
        assertEquals("Tom", result.name());
        assertEquals(Gender.MALE, result.gender());
        assertEquals(LocalDate.of(2020, 1, 1), result.birthDate());
        assertEquals(CatBreed.BRITISH, result.breed());
        assertEquals(CatColor.GRAY, result.color());
        assertNotNull(result.owner());
        assertEquals("Alice", result.owner().getName());
        assertEquals(1, result.friends().size());
        assertEquals("Jerry", result.friends().get(0).getName());
    }

    @Test
    void givenCatId_whenGetFriends_thenReturnsFriendDto() {
        // given
        UUID catId = UUID.randomUUID();
        Cat friend = new Cat();
        friend.setId(catId);
        friend.setName("Jerry");
        friend.setFriends(List.of());
        List<Cat> friends = List.of(friend);
        when(catDao.findFriends(catId)).thenReturn(friends);

        // when
        List<CatResponseDto> result = catService.getFriends(catId);

        // then
        assertEquals(1, result.size());
        assertEquals("Jerry", result.get(0).name());
    }

    @Test
    void whenGetAllCats_thenReturnsAllCatDto() {
        // given
        Cat cat = new Cat();
        cat.setId(UUID.randomUUID());
        cat.setName("Tom");
        cat.setGender(Gender.MALE);
        cat.setBirthDate(LocalDate.of(2020, 11, 1));
        cat.setFriends(List.of());
        List<Cat> cats = List.of(cat);
        when(catDao.findMany()).thenReturn(cats);

        // when
        List<CatResponseDto> result = catService.getAllCats();

        // then
        assertEquals(1, result.size());
        assertEquals("Tom", result.get(0).name());
    }

    @Test
    void givenUpdateCatDto_whenUpdateCat_thenCallsDaoUpdateWithMergedCat() {
        // given
        UUID id = UUID.randomUUID();
        UpdateCatDto dto = new UpdateCatDto();
        dto.setId(id);
        dto.setFriends(Collections.emptyList());

        Cat existing = new Cat();
        existing.setId(id);
        existing.setName("Old Tom");

        when(catDao.findOne(id)).thenReturn(existing);

        // when
        catService.updateCat(dto);

        // then
        verify(catDao).update(any(Cat.class));
    }

    @Test
    void givenCatId_whenDeleteCat_thenCallsDaoDelete() {
        // given
        UUID id = UUID.randomUUID();

        // when
        catService.deleteCat(id);

        // then
        verify(catDao).delete(id);
    }
}