package CatsAndOwners.model.dto.cat.request;

import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.dto.owner.OwnerShortDto;
import CatsAndOwners.model.enums.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateCatDto {
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private CatBreed breed;
    private CatColor color;
    private OwnerShortDto owner;
    private List<CatShortDto> friends;

    public CreateCatDto(String name, Gender gender, LocalDate birthDate,
                        CatBreed breed, CatColor color, List<CatShortDto> friends) {
        this.setName(name);
        this.setGender(gender);
        this.setBirthDate(birthDate);
        this.setBreed(breed);
        this.setColor(color);
        this.setFriends(friends);
    }
}