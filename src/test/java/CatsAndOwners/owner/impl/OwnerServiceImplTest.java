package CatsAndOwners.owner.impl;

import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;
import CatsAndOwners.model.entity.Owner;
import CatsAndOwners.model.enums.Gender;
import CatsAndOwners.repository.owner.OwnerRepository;
import CatsAndOwners.service.owner.impl.OwnerServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceImplTest {
    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerServiceImpl ownerService;

    @Test
    public void createOwner_ShouldSaveOwnerWithoutCats_WhenValidDto() {
        // given
        CreateOwnerDto dto = new CreateOwnerDto();
        dto.setName("Alice");
        dto.setGender(Gender.FEMALE);
        dto.setBirthDate(LocalDate.of(1997, 1, 30));
        dto.setCats(null);

        // when
        ownerService.createOwner(dto);

        // then
        verify(ownerRepository, times(1)).save(any(Owner.class));
    }

    @Test
    public void createOwner_ShouldSaveOwnerWithCats_WhenDtoHasCats() {
        // given
        CatShortDto catDto1 = new CatShortDto();
        catDto1.setName("Барсик");

        CatShortDto catDto2 = new CatShortDto();
        catDto2.setName("Альфред");

        List<CatShortDto> cats = List.of(catDto1, catDto2);

        CreateOwnerDto dto = new CreateOwnerDto();
        dto.setName("Bob");
        dto.setGender(Gender.MALE);
        dto.setBirthDate(LocalDate.of(1985, 3, 15));
        dto.setCats(cats);

        // when
        ownerService.createOwner(dto);

        // then
        verify(ownerRepository, times(1)).save(any(Owner.class));
    }

    @Test
    public void getOwnerById_ShouldReturnOwnerDto_WhenOwnerExists() {
        // given
        UUID id = UUID.randomUUID();
        Owner owner = new Owner();
        owner.setId(id);
        owner.setName("Andrey");
        owner.setGender(Gender.MALE);
        owner.setBirthDate(LocalDate.of(1990, 1, 1));
        owner.setCats(Collections.emptyList());

        when(ownerRepository.findById(id)).thenReturn(Optional.of(owner));

        // when
        OwnerResponseDto result = ownerService.getOwnerById(id);

        // then
        assertEquals(id, result.id());
        assertEquals("Andrey", result.name());
        assertEquals(Gender.MALE, result.gender());
        assertEquals(LocalDate.of(1990, 1, 1), result.birthDate());
        assertTrue(result.cats().isEmpty());
    }

    @Test
    public void getOwnerById_ShouldThrowException_WhenOwnerNotFound() {
        // given
        UUID id = UUID.randomUUID();
        when(ownerRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> ownerService.getOwnerById(id));
    }

    @Test
    public void getAllOwners_ShouldReturnListOfDtos() {
        // given
        Owner owner = new Owner();
        owner.setId(UUID.randomUUID());
        owner.setName("Owner1");
        owner.setGender(Gender.FEMALE);
        owner.setBirthDate(LocalDate.of(1991, 2, 2));
        owner.setCats(Collections.emptyList());

        when(ownerRepository.findAll()).thenReturn(List.of(owner));

        // when
        List<OwnerResponseDto> result = ownerService.getAllOwners();

        // then
        assertEquals(1, result.size());
        assertEquals("Owner1", result.get(0).name());
        assertEquals(Gender.FEMALE, result.get(0).gender());
        assertEquals(LocalDate.of(1991, 2, 2), result.get(0).birthDate());
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

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(existingOwner));

        // when
        ownerService.updateOwner(dto);

        // then
        verify(ownerRepository).save(any(Owner.class));
    }

    @Test
    public void updateOwner_ShouldThrowException_WhenOwnerNotFound() {
        // given
        UUID ownerId = UUID.randomUUID();
        UpdateOwnerDto dto = new UpdateOwnerDto();
        dto.setId(ownerId);

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> ownerService.updateOwner(dto));
    }

    @Test
    public void deleteOwner_ShouldCallDeleteById() {
        // given
        UUID ownerId = UUID.randomUUID();

        // when
        ownerService.deleteOwner(ownerId);

        // then
        verify(ownerRepository, times(1)).deleteById(ownerId);
    }
}