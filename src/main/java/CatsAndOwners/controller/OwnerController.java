package CatsAndOwners.controller;

import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;
import CatsAndOwners.service.owner.OwnerService;

import java.util.List;
import java.util.UUID;

public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public void createOwner(CreateOwnerDto dto) {
        ownerService.createOwner(dto);
        System.out.println("Хозяин создан.");
    }

    public OwnerResponseDto getOwner(UUID id) {
        return ownerService.getOwnerById(id);
    }

    public List<OwnerResponseDto> getAllOwners() {
        return ownerService.getAllOwners();
    }

    public void updateOwner(UpdateOwnerDto dto) {
        ownerService.updateOwner(dto);
        System.out.println("Хозяин обновлён.");
    }

    public void deleteOwner(UUID id) {
        ownerService.deleteOwner(id);
        System.out.println("Хозяин удалён.");
    }
}