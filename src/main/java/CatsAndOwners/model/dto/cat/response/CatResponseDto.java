package CatsAndOwners.model.dto.cat.response;

import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.dto.owner.OwnerShortDto;
import CatsAndOwners.model.enums.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CatResponseDto(
        UUID id,
        String name,
        Gender gender,
        LocalDate birthDate,
        CatBreed breed,
        CatColor color,
        OwnerShortDto owner,
        List<CatShortDto> friends
) {}