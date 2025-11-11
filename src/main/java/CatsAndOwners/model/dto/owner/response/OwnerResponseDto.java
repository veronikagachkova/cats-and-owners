package CatsAndOwners.model.dto.owner.response;

import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.enums.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OwnerResponseDto(
        UUID id,
        String name,
        Gender gender,
        LocalDate birthDate,
        List<CatShortDto> cats
) {}