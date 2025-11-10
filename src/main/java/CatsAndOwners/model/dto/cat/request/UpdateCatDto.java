package CatsAndOwners.model.dto.cat.request;

import CatsAndOwners.model.dto.cat.CatShortDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UpdateCatDto {
    private UUID id;
    private List<CatShortDto> friends;

    public UpdateCatDto(UUID id) {
        this.setId(id);
    }
}