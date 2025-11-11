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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
public class OwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCallCreateOwner_whenCreateOwnerIsInvoked() throws Exception {
        // Arrange
        CreateOwnerDto dto = new CreateOwnerDto();
        dto.setName("Alice");
        dto.setBirthDate(LocalDate.of(1990, 1, 1));
        dto.setGender(Gender.FEMALE);

        // Act & Assert
        mockMvc.perform(post("/api/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(ownerService, times(1)).createOwner(any(CreateOwnerDto.class));
    }

    @Test
    public void shouldReturnOwnerById_whenGetOwnerIsCalled() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Bob";
        Gender gender = Gender.MALE;
        LocalDate birthDate = LocalDate.of(1980, 5, 5);
        List<CatShortDto> cats = Collections.emptyList();

        OwnerResponseDto expected = new OwnerResponseDto(id, name, gender, birthDate, cats);

        when(ownerService.getOwnerById(id)).thenReturn(expected);

        // Act & Assert
        mockMvc.perform(get("/api/owners/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.birthDate").value("1980-05-05"))
                .andExpect(jsonPath("$.cats").isArray())
                .andExpect(jsonPath("$.cats").isEmpty());

        verify(ownerService).getOwnerById(id);
    }

    @Test
    public void shouldReturnEmptyList_whenNoOwnersExist() throws Exception {
        // Arrange
        when(ownerService.getAllOwners()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(ownerService).getAllOwners();
    }

    @Test
    public void shouldUpdateOwner_whenUpdateOwnerIsCalled() throws Exception {
        // Arrange
        UUID ownerId = UUID.randomUUID();

        CatShortDto cat = new CatShortDto();
        cat.setId(UUID.randomUUID());
        cat.setName("Tom");
        cat.setGender(Gender.MALE);
        cat.setBirthDate(LocalDate.of(2023, 5, 23));
        cat.setBreed(CatBreed.RAGDOLL);
        cat.setColor(CatColor.BROWN);

        UpdateOwnerDto dto = new UpdateOwnerDto();
        dto.setId(ownerId);
        dto.setCats(Collections.singletonList(cat));

        // Act & Assert
        mockMvc.perform(put("/api/owners/{id}", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(ownerService).updateOwner(any(UpdateOwnerDto.class));
    }

    @Test
    public void shouldDeleteOwner_whenDeleteOwnerIsCalled() throws Exception {
        // Arrange
        UUID expectedId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(delete("/api/owners/{id}", expectedId))
                .andExpect(status().isOk());

        verify(ownerService, times(1)).deleteOwner(expectedId);
    }
}