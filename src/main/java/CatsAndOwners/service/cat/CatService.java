package CatsAndOwners.service.cat;

import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;

import java.util.List;
import java.util.UUID;

public interface CatService {
    void createCat(CreateCatDto cat);
    CatResponseDto getCatById(UUID id);
    List<CatResponseDto> getFriends(UUID id);
    List<CatResponseDto> getAllCats();
    void updateCat(UpdateCatDto cat);
    void deleteCat(UUID id);
}