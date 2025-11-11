package CatsAndOwners.controller;

import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;
import CatsAndOwners.model.enums.CatColor;
import CatsAndOwners.service.cat.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cats")
public class CatController {
    private final CatService catService;

    @Autowired
    public CatController(CatService catService) {
        this.catService = catService;
    }

    @GetMapping
    public List<CatResponseDto> getAllCats() {
        System.out.println("Получены все коты");
        return catService.getAllCats();
    }

    @GetMapping("/{id}")
    public CatResponseDto getCat(@PathVariable UUID id) {
        System.out.println("Получен Кот(" + id + ")");
        return catService.getCatById(id);
    }

    @GetMapping("/{id}/friends")
    public List<CatResponseDto> getCatFriends(@PathVariable UUID id) {
        System.out.println("Получены друзья Кота(" + id + ")");
        return catService.getFriends(id);
    }

    @GetMapping("/color/{color}")
    public List<CatResponseDto> getCatsByColor(@PathVariable CatColor color) {
        System.out.println("Получены коты цвета: " + color);
        return catService.getCatsByColor(color);
    }

    @GetMapping("/breed/{breed}")
    public List<CatResponseDto> getCatsByBreed(@PathVariable String breed) {
        System.out.println("Получены коты породы: " + breed);
        return catService.getCatsByBreed(breed);
    }

    @PostMapping
    public void createCat(@RequestBody CreateCatDto dto) {
        catService.createCat(dto);
        System.out.println("Кот создан");
    }

    @PutMapping("/{id}")
    public void updateCat(@PathVariable UUID id, @RequestBody UpdateCatDto dto) {
        dto.setId(id);
        catService.updateCat(dto);
        System.out.println("Кот обновлён");
    }

    @DeleteMapping("/{id}")
    public void deleteCat(@PathVariable UUID id) {
        catService.deleteCat(id);
        System.out.println("Кот удалён");
    }
}