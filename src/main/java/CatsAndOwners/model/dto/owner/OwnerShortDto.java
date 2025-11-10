package CatsAndOwners.model.dto.owner;

import CatsAndOwners.model.enums.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OwnerShortDto {
    private UUID id;
    private String name;
    private Gender gender;
    private LocalDate birthDate;

    public OwnerShortDto(UUID id) {
        this.setId(id);
    }

    public OwnerShortDto(String name, Gender gender, LocalDate birthDate) {
        this.setName(name);
        this.setGender(gender);
        this.setBirthDate(birthDate);
    }

    @Override
    public String toString() {
        return "(id=" + id + ", name=" + name + ")";
    }
}