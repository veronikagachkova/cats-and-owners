package CatsAndOwners.model.dto.owner.request;

import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.enums.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateOwnerDto {
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private List<CatShortDto> cats;

    public CreateOwnerDto(String name, Gender gender, LocalDate birthDate,
                          List<CatShortDto> cats) {
        this.setName(name);
        this.setGender(gender);
        this.setBirthDate(birthDate);
        this.setCats(cats);
    }
}