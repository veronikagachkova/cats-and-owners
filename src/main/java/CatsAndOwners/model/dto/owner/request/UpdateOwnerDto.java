package CatsAndOwners.model.dto.owner.request;

import CatsAndOwners.model.dto.cat.CatShortDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UpdateOwnerDto {
    private UUID id;
    private List<CatShortDto> cats;

    public UpdateOwnerDto(UUID id) {
        this.setId(id);
    }
}