package CatsAndOwners.owner.controller;

import CatsAndOwners.controller.OwnerController;
import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;
import CatsAndOwners.model.enums.CatBreed;
import CatsAndOwners.model.enums.CatColor;
import CatsAndOwners.model.enums.Gender;
import CatsAndOwners.service.owner.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OwnerControllerTest {
    private OwnerService ownerService;
    private OwnerController ownerController;

    @BeforeEach
    public void setup() {
        ownerService = mock(OwnerService.class);
        ownerController = new OwnerController(ownerService);
    }

    @Test
    public void shouldCallCreateOwner_whenCreateOwnerIsInvoked() {
        CreateOwnerDto dto = new CreateOwnerDto();
        dto.setName("Owner1");
        dto.setBirthDate(LocalDate.of(1990, 1, 1));
        dto.setGender(Gender.FEMALE);

        ownerController.createOwner(dto);

        verify(ownerService, times(1)).createOwner(dto);
    }

    @Test
    public void shouldReturnOwnerById_whenGetOwnerIsCalled() {
        UUID id = UUID.randomUUID();
        String name = "Owner2";
        Gender gender = Gender.MALE;
        LocalDate birthDate = LocalDate.of(1980, 5, 5);
        List<CatShortDto> cats = Collections.emptyList();

        OwnerResponseDto expected = new OwnerResponseDto(id, name, gender, birthDate, cats);

        when(ownerService.getOwnerById(id)).thenReturn(expected);

        OwnerResponseDto result = ownerController.getOwner(id);

        assertEquals(expected, result);
        verify(ownerService).getOwnerById(id);
    }

    @Test
    public void shouldReturnEmptyList_whenNoOwnersExist() {
        when(ownerService.getAllOwners()).thenReturn(Collections.emptyList());

        List<OwnerResponseDto> result = ownerController.getAllOwners();

        assertTrue(result.isEmpty());
        verify(ownerService).getAllOwners();
    }

    @Test
    public void shouldUpdateOwner_whenUpdateOwnerIsCalled() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        CatShortDto cat = new CatShortDto(
                "Owner3",
                Gender.MALE,
                LocalDate.of(2023, 5, 23),
                CatBreed.RAGDOLL,
                CatColor.CALICO
        );

        UpdateOwnerDto dto = new UpdateOwnerDto();
        dto.setId(ownerId);
        dto.setCats(Collections.singletonList(cat));

        // Act
        ownerController.updateOwner(dto);

        // Assert
        ArgumentCaptor<UpdateOwnerDto> captor = ArgumentCaptor.forClass(UpdateOwnerDto.class);
        verify(ownerService).updateOwner(captor.capture());

        UpdateOwnerDto captured = captor.getValue();
        assertEquals(ownerId, captured.getId());
        assertEquals(1, captured.getCats().size());
        assertEquals("Owner3", captured.getCats().get(0).getName());
        assertEquals(CatBreed.RAGDOLL, captured.getCats().get(0).getBreed());
    }

    @Test
    public void shouldDeleteOwner_whenDeleteOwnerIsCalled() {
        // Arrange
        UUID expectedId = UUID.randomUUID();

        // Act
        ownerController.deleteOwner(expectedId);

        // Assert
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(ownerService).deleteOwner(captor.capture());

        UUID actualId = captor.getValue();
        assertEquals(expectedId, actualId);
    }
}