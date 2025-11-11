package CatsAndOwners.cat.controller;

import CatsAndOwners.controller.CatController;
import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;
import CatsAndOwners.model.enums.*;
import CatsAndOwners.service.cat.CatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatController.class)
public class CatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatService catService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCallCreateCat_whenCreateCatIsInvoked() throws Exception {
        // Arrange
        CreateCatDto dto = new CreateCatDto();
        dto.setName("Tom");
        dto.setBreed(CatBreed.RAGDOLL);
        dto.setColor(CatColor.GRAY);
        dto.setBirthDate(LocalDate.of(2020, 1, 1));
        dto.setGender(Gender.MALE);

        // Act & Assert
        mockMvc.perform(post("/api/cats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(catService, times(1)).createCat(any(CreateCatDto.class));
    }

    @Test
    public void shouldReturnCatById_whenGetCatIsCalled() throws Exception {
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

        // Act & Assert
        mockMvc.perform(get("/api/cats/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Tom"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.breed").value("Рэгдолл"))
                .andExpect(jsonPath("$.color").value("Серый"));

        verify(catService).getCatById(id);
    }

    @Test
    public void shouldReturnCatFriends_whenGetCatFriendsIsCalled() throws Exception {
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

        // Act & Assert
        mockMvc.perform(get("/api/cats/{id}/friends", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Jerry"))
                .andExpect(jsonPath("$[0].gender").value("FEMALE"))
                .andExpect(jsonPath("$[0].breed").value("Мейн-кун"));

        verify(catService).getFriends(id);
    }

    @Test
    public void shouldReturnAllCats_whenGetAllCatsIsCalled() throws Exception {
        // Arrange
        when(catService.getAllCats()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/cats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(catService).getAllCats();
    }

    @Test
    public void shouldReturnCatsByColor_whenGetCatsByColorIsCalled() throws Exception {
        // Arrange
        CatColor color = CatColor.GRAY;
        List<CatResponseDto> expected = List.of(
                new CatResponseDto(
                        UUID.randomUUID(),
                        "GrayCat",
                        Gender.MALE,
                        LocalDate.of(2020, 1, 1),
                        CatBreed.BRITISH,
                        CatColor.GRAY,
                        null,
                        null
                )
        );
        when(catService.getCatsByColor(color)).thenReturn(expected);

        // Act & Assert
        mockMvc.perform(get("/api/cats/color/{color}", color))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("GrayCat"))
                .andExpect(jsonPath("$[0].color").value("Серый"));

        verify(catService).getCatsByColor(color);
    }

    @Test
    public void shouldReturnCatsByBreed_whenGetCatsByBreedIsCalled() throws Exception {
        // Arrange
        String breed = "british";
        List<CatResponseDto> expected = List.of(
                new CatResponseDto(
                        UUID.randomUUID(),
                        "BritishCat",
                        Gender.MALE,
                        LocalDate.of(2020, 1, 1),
                        CatBreed.BRITISH,
                        CatColor.GRAY,
                        null,
                        null
                )
        );
        when(catService.getCatsByBreed(breed)).thenReturn(expected);

        // Act & Assert
        mockMvc.perform(get("/api/cats/breed/{breed}", breed))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("BritishCat"))
                .andExpect(jsonPath("$[0].breed").value("Британская"));

        verify(catService).getCatsByBreed(breed);
    }

    @Test
    public void shouldUpdateCat_whenUpdateCatIsCalled() throws Exception {
        // Arrange
        UUID catId = UUID.randomUUID();
        UUID friendId = UUID.randomUUID();

        CatShortDto friendDto = new CatShortDto();
        friendDto.setId(UUID.randomUUID());
        friendDto.setName("Jerry");
        friendDto.setGender(Gender.FEMALE);
        friendDto.setBirthDate(LocalDate.of(2021, 5, 20));
        friendDto.setBreed(CatBreed.MAINE_COON);
        friendDto.setColor(CatColor.WHITE);

        UpdateCatDto dto = new UpdateCatDto();
        dto.setId(catId);
        dto.setFriends(List.of(friendDto));

        // Act & Assert
        mockMvc.perform(put("/api/cats/{id}", catId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(catService).updateCat(any(UpdateCatDto.class));
    }

    @Test
    public void shouldDeleteCat_whenDeleteCatIsCalled() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(delete("/api/cats/{id}", id))
                .andExpect(status().isOk());

        verify(catService, times(1)).deleteCat(id);
    }
}