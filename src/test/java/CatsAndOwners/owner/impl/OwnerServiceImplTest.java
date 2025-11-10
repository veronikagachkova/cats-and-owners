package CatsAndOwners.owner.impl;

import CatsAndOwners.dao.cat.CatDao;
import CatsAndOwners.dao.owner.OwnerDao;
import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;
import CatsAndOwners.model.entity.Cat;
import CatsAndOwners.model.entity.Owner;
import CatsAndOwners.model.enums.Gender;
import CatsAndOwners.service.owner.impl.OwnerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OwnerServiceImplTest {
    private OwnerDao ownerDao;
    private CatDao catDao;
    private OwnerServiceImpl ownerService;

    @BeforeEach
    public void setUp() {
        ownerDao = mock(OwnerDao.class);
        catDao = mock(CatDao.class);
        ownerService = new OwnerServiceImpl(ownerDao, catDao);
    }

    @Test
    public void createOwner_ShouldSaveOwnerWithoutCats_WhenValidDto() {
        // given
        CreateOwnerDto dto = new CreateOwnerDto(
                "Owner1",
                Gender.FEMALE,
                LocalDate.of(1997, 1, 30),
                null
        );

        // when
        ownerService.createOwner(dto);

        // then
        ArgumentCaptor<Owner> captor = ArgumentCaptor.forClass(Owner.class);
        verify(ownerDao, times(1)).create(captor.capture());

        Owner savedOwner = captor.getValue();
        assertEquals("Owner1", savedOwner.getName());
        assertEquals(Gender.FEMALE, savedOwner.getGender());
        assertEquals("1997-01-30", savedOwner.getBirthDate().toString());
        assertTrue(savedOwner.getCats().isEmpty());
    }

    @Test
    public void createOwner_ShouldSaveOwnerWithCats_WhenDtoHasCats() {
        // given
        CatShortDto catDto1 = new CatShortDto(
                "Cat1",
                Gender.FEMALE,
                null,
                null,
                null
        );
        CatShortDto catDto2 = new CatShortDto(
                "Cat2",
                null,
                null,
                null,
                null
        );
        List<CatShortDto> cats = List.of(catDto1, catDto2);

        CreateOwnerDto dto = new CreateOwnerDto(
                "Owner2",
                Gender.MALE,
                LocalDate.of(1985, 3, 15),
                cats
        );

        // when
        ownerService.createOwner(dto);

        // then
        ArgumentCaptor<Owner> captor = ArgumentCaptor.forClass(Owner.class);
        verify(ownerDao, times(1)).create(captor.capture());

        Owner savedOwner = captor.getValue();
        assertEquals("Owner2", savedOwner.getName());
        assertEquals(Gender.MALE, savedOwner.getGender());
        assertEquals("1985-03-15", savedOwner.getBirthDate().toString());
        assertEquals(2, savedOwner.getCats().size());
        assertEquals("Cat1", savedOwner.getCats().get(0).getName());
        assertEquals("Cat2", savedOwner.getCats().get(1).getName());

        assertEquals(savedOwner, savedOwner.getCats().get(0).getOwner());
        assertEquals(savedOwner, savedOwner.getCats().get(1).getOwner());
    }

    @Test
    public void getOwnerById_ShouldReturnOwnerDto_WhenOwnerExists() {
        // given
        UUID id = UUID.randomUUID();
        Owner owner = new Owner();
        owner.setId(id);
        owner.setName("Андрей");
        owner.setGender(Gender.MALE);
        owner.setBirthDate(LocalDate.of(1990, 1, 1));
        owner.setCats(Collections.emptyList());

        when(ownerDao.findOne(id)).thenReturn(owner);

        // when
        OwnerResponseDto result = ownerService.getOwnerById(id);

        // then
        assertEquals(id, result.id());
        assertEquals("Андрей", result.name());
        assertEquals(Gender.MALE, result.gender());
        assertEquals(LocalDate.of(1990, 1, 1), result.birthDate());
        assertTrue(result.cats().isEmpty());
    }

    @Test
    public void getAllOwners_ShouldReturnListOfDtos() {
        // given
        Owner owner = new Owner();
        owner.setId(UUID.randomUUID());
        owner.setName("Ирина");
        owner.setGender(Gender.FEMALE);
        owner.setBirthDate(LocalDate.of(1991, 2, 2));
        owner.setCats(Collections.emptyList());

        when(ownerDao.findMany()).thenReturn(List.of(owner));

        // when
        List<OwnerResponseDto> result = ownerService.getAllOwners();

        // then
        assertEquals(1, result.size());
        assertEquals("Ирина", result.get(0).name());
    }

    @Test
    public void updateOwner_ShouldCallUpdateWithCorrectEntity() {
        // given
        UUID ownerId = UUID.randomUUID();
        UpdateOwnerDto dto = new UpdateOwnerDto();
        dto.setId(ownerId);
        dto.setCats(Collections.emptyList());

        Owner existingOwner = new Owner();
        existingOwner.setId(ownerId);
        existingOwner.setCats(Collections.emptyList());

        when(ownerDao.findOne(ownerId)).thenReturn(existingOwner);

        // when
        ownerService.updateOwner(dto);

        // then
        ArgumentCaptor<Owner> captor = ArgumentCaptor.forClass(Owner.class);
        verify(ownerDao).update(captor.capture());
        assertEquals(ownerId, captor.getValue().getId());
        assertTrue(captor.getValue().getCats().isEmpty());
    }

    @Test
    public void updateOwner_ShouldUpdateOwnerWithCats() {
        // given
        UUID ownerId = UUID.randomUUID();
        UUID catId = UUID.randomUUID();

        CatShortDto catDto = new CatShortDto();
        catDto.setId(catId);

        UpdateOwnerDto dto = new UpdateOwnerDto();
        dto.setId(ownerId);
        dto.setCats(List.of(catDto));

        Owner existingOwner = new Owner();
        existingOwner.setId(ownerId);

        Cat existingCat = new Cat();
        existingCat.setId(catId);

        when(ownerDao.findOne(ownerId)).thenReturn(existingOwner);
        when(catDao.findOne(catId)).thenReturn(existingCat);

        // when
        ownerService.updateOwner(dto);

        // then
        ArgumentCaptor<Owner> captor = ArgumentCaptor.forClass(Owner.class);
        verify(ownerDao).update(captor.capture());

        Owner updated = captor.getValue();
        assertEquals(ownerId, updated.getId());
        assertEquals(1, updated.getCats().size());
        assertEquals(catId, updated.getCats().get(0).getId());
        assertEquals(existingOwner, updated.getCats().get(0).getOwner()); // связь проверяем!
    }

    @Test
    public void deleteOwner_ShouldCallDeleteById() {
        // given
        UUID ownerId = UUID.randomUUID();

        // when
        ownerService.deleteOwner(ownerId);

        // then
        verify(ownerDao, times(1)).delete(ownerId);
    }
}