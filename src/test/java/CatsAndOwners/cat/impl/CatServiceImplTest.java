package CatsAndOwners.cat.impl;

import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;
import CatsAndOwners.model.entity.Cat;
import CatsAndOwners.model.entity.Owner;
import CatsAndOwners.model.enums.*;
import CatsAndOwners.repository.cat.CatRepository;
import CatsAndOwners.service.cat.impl.CatServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatServiceImplTest {

    @Mock
    private CatRepository catRepository;

    @InjectMocks
    private CatServiceImpl catService;

    @Test
    void givenCreateCatDto_whenCreateCat_thenCallsDaoCreateWithEntity() {
        // given
        CreateCatDto dto = new CreateCatDto();
        dto.setName("Tom");

        // when
        catService.createCat(dto);

        // then
        verify(catRepository).save(any(Cat.class));
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

        when(catRepository.findById(catId)).thenReturn(Optional.of(cat));

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
    void givenUnknownId_whenGetCatById_thenThrowsException() {
        UUID unknownId = UUID.randomUUID();
        when(catRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> catService.getCatById(unknownId));
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
        when(catRepository.findFriends(catId)).thenReturn(friends);

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
        when(catRepository.findAll()).thenReturn(cats);

        // when
        List<CatResponseDto> result = catService.getAllCats();

        // then
        assertEquals(1, result.size());
        assertEquals("Tom", result.get(0).name());
    }

    @Test
    void givenColor_whenGetCatsByColor_thenReturnsFilteredCats() {
        // given
        CatColor color = CatColor.GRAY;
        Cat cat = new Cat();
        cat.setId(UUID.randomUUID());
        cat.setName("GrayCat");
        cat.setColor(color);
        List<Cat> cats = List.of(cat);

        when(catRepository.findByColor(color)).thenReturn(cats);

        // when
        List<CatResponseDto> result = catService.getCatsByColor(color);

        // then
        assertEquals(1, result.size());
        assertEquals("GrayCat", result.get(0).name());
        assertEquals(CatColor.GRAY, result.get(0).color());
    }

    @Test
    void givenBreed_whenGetCatsByBreed_thenReturnsFilteredCats() {
        // given
        String breed = "british";
        CatBreed catBreed = CatBreed.BRITISH;
        Cat cat = new Cat();
        cat.setId(UUID.randomUUID());
        cat.setName("BritishCat");
        cat.setBreed(catBreed);
        List<Cat> cats = List.of(cat);

        when(catRepository.findByBreed(catBreed)).thenReturn(cats);

        // when
        List<CatResponseDto> result = catService.getCatsByBreed(breed);

        // then
        assertEquals(1, result.size());
        assertEquals("BritishCat", result.get(0).name());
        assertEquals(CatBreed.BRITISH, result.get(0).breed());
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

        when(catRepository.findById(id)).thenReturn(Optional.of(existing));

        // when
        catService.updateCat(dto);

        // then
        verify(catRepository).save(any(Cat.class));
    }

    @Test
    void givenCatId_whenDeleteCat_thenCallsDaoDelete() {
        // given
        UUID id = UUID.randomUUID();

        // when
        catService.deleteCat(id);

        // then
        verify(catRepository).deleteById(id);
    }
}