package CatsAndOwners.model.dto.cat;

import CatsAndOwners.model.enums.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CatShortDto {
    private UUID id;
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private CatBreed breed;
    private CatColor color;

    public CatShortDto(UUID id) {
        this.setId(id);
    }

    public CatShortDto(String name, Gender gender, LocalDate birthDate, CatBreed breed, CatColor color) {
        this.setName(name);
        this.setGender(gender);
        this.setBirthDate(birthDate);
        this.setBreed(breed);
        this.setColor(color);
    }

    @Override
    public String toString() {
        return "(id=" + id + ", name=" + name + ")";
    }
}