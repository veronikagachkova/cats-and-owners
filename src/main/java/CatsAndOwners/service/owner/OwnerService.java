package CatsAndOwners.service.owner;

import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;

import java.util.List;
import java.util.UUID;

public interface OwnerService {
    void createOwner(CreateOwnerDto owner);
    OwnerResponseDto getOwnerById(UUID id);
    List<OwnerResponseDto> getAllOwners();
    void updateOwner(UpdateOwnerDto owner);
    void deleteOwner(UUID id);
}