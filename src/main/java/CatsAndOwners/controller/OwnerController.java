package CatsAndOwners.controller;

import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;
import CatsAndOwners.service.owner.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {
    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public List<OwnerResponseDto> getAllOwners() {
        System.out.println("Получены все хозяева");
        return ownerService.getAllOwners();
    }

    @GetMapping("/{id}")
    public OwnerResponseDto getOwner(@PathVariable UUID id) {
        System.out.println("Получен Хозяин(" + id + ")");
        return ownerService.getOwnerById(id);
    }

    @PostMapping
    public void createOwner(@RequestBody CreateOwnerDto dto) {
        ownerService.createOwner(dto);
        System.out.println("Хозяин создан.");
    }

    @PutMapping("/{id}")
    public void updateOwner(@PathVariable UUID id, @RequestBody UpdateOwnerDto dto) {
        dto.setId(id);
        ownerService.updateOwner(dto);
        System.out.println("Хозяин обновлён.");
    }

    @DeleteMapping("/{id}")
    public void deleteOwner(@PathVariable UUID id) {
        ownerService.deleteOwner(id);
        System.out.println("Хозяин удалён.");
    }
}