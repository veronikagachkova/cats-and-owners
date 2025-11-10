package CatsAndOwners.controller;

import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;
import CatsAndOwners.service.cat.CatService;

import java.util.List;
import java.util.UUID;

public class CatController {
    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    public void createCat(CreateCatDto dto) {
        catService.createCat(dto);
        System.out.println("Кот создан");
    }

    public CatResponseDto getCat(UUID id) {
        System.out.println("Получен Кот(" + id + ")");
        return catService.getCatById(id);
    }

    public List<CatResponseDto> getCatFriends(UUID id) {
        System.out.println("Получены друзья Кота(" + id + ")");
        return catService.getFriends(id);
    }

    public List<CatResponseDto> getAllCats() {
        System.out.println("Получены все коты");
        return catService.getAllCats();
    }

    public void updateCat(UpdateCatDto dto) {
        catService.updateCat(dto);
        System.out.println("Кот обновлён");
    }

    public void deleteCat(UUID id) {
        catService.deleteCat(id);
        System.out.println("Кот удалён");
    }
}