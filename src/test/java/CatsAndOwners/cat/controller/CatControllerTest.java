package CatsAndOwners.cat.controller;

import CatsAndOwners.controller.CatController;
import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;
import CatsAndOwners.model.enums.*;
import CatsAndOwners.service.cat.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

public class CatControllerTest {
    private CatService catService;
    private CatController catController;

    @BeforeEach
    public void setup() {
        catService = mock(CatService.class);
        catController = new CatController(catService);
    }

    @Test
    public void shouldCallCreateCat_whenCreateCatIsInvoked() {
        // Arrange
        CreateCatDto dto = new CreateCatDto();
        dto.setName("Tom");
        dto.setBreed(CatBreed.RAGDOLL);
        dto.setColor(CatColor.GRAY);
        dto.setBirthDate(LocalDate.of(2020, 1, 1));
        dto.setGender(Gender.MALE);

        // Act
        catController.createCat(dto);

        // Assert
        verify(catService, times(1)).createCat(dto);
    }

    @Test
    public void shouldReturnCatById_whenGetCatIsCalled() {
        // Arrange
        UUID id = UUID.randomUUID();
        CatResponseDto expected = new CatResponseDto(
                id,
                "Tom",
                Gender.MALE,
                LocalDate.of(2020, 1, 1),
                CatBreed.RAGDOLL,
                CatColor.GRAY,
                null,
                null
        );

        when(catService.getCatById(id)).thenReturn(expected);

        // Act
        CatResponseDto result = catController.getCat(id);

        // Assert
        assertEquals(expected, result);
        verify(catService).getCatById(id);
    }

    @Test
    public void shouldReturnCatFriends_whenGetCatFriendsIsCalled() {
        // Arrange
        UUID id = UUID.randomUUID();
        List<CatResponseDto> expected = List.of(
                new CatResponseDto(
                        UUID.randomUUID(),
                        "Jerry",
                        Gender.FEMALE,
                        LocalDate.of(2021, 2, 2),
                        CatBreed.MAINE_COON,
                        CatColor.BLACK,
                        null,
                        null
                )
        );
        when(catService.getFriends(id)).thenReturn(expected);

        // Act
        List<CatResponseDto> result = catController.getCatFriends(id);

        // Assert
        assertEquals(expected, result);
        verify(catService).getFriends(id);
    }

    @Test
    public void shouldReturnAllCats_whenGetAllCatsIsCalled() {
        // Arrange
        when(catService.getAllCats()).thenReturn(Collections.emptyList());

        // Act
        List<CatResponseDto> result = catController.getAllCats();

        // Assert
        assertTrue(result.isEmpty());
        verify(catService).getAllCats();
    }

    @Test
    public void shouldUpdateCat_whenUpdateCatIsCalled() {
        // Arrange
        UUID catId = UUID.randomUUID();
        CatShortDto friendDto = new CatShortDto(
                "Jerry",
                Gender.FEMALE,
                LocalDate.of(2021, 5, 20),
                CatBreed.MAINE_COON,
                CatColor.WHITE
        );
        UpdateCatDto dto = new UpdateCatDto();
        dto.setId(catId);
        dto.setFriends(List.of(friendDto));

        ArgumentCaptor<UpdateCatDto> captor = ArgumentCaptor.forClass(UpdateCatDto.class);

        // Act
        catController.updateCat(dto);

        // Assert
        verify(catService).updateCat(captor.capture());
        UpdateCatDto capturedDto = captor.getValue();

        assertEquals(catId, capturedDto.getId());
        assertNotNull(capturedDto.getFriends());
        assertEquals(1, capturedDto.getFriends().size());

        CatShortDto capturedFriend = capturedDto.getFriends().get(0);
        assertEquals("Jerry", capturedFriend.getName());
        assertEquals(Gender.FEMALE, capturedFriend.getGender());
        assertEquals(LocalDate.of(2021, 5, 20), capturedFriend.getBirthDate());
        assertEquals(CatBreed.MAINE_COON, capturedFriend.getBreed());
        assertEquals(CatColor.WHITE, capturedFriend.getColor());
    }

    @Test
    public void shouldDeleteCat_whenDeleteCatIsCalled() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        catController.deleteCat(id);

        // Assert
        verify(catService, times(1)).deleteCat(id);
    }
}