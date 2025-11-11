package CatsAndOwners.service.owner.impl;

import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;
import CatsAndOwners.model.entity.Owner;
import CatsAndOwners.repository.cat.CatRepository;
import CatsAndOwners.repository.owner.OwnerRepository;
import CatsAndOwners.service.owner.OwnerService;
import CatsAndOwners.util.mapper.OwnerMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;

    @Autowired
    public OwnerServiceImpl(OwnerRepository ownerRepository, CatRepository catRepository) {
        this.ownerRepository = ownerRepository;
        this.catRepository = catRepository;
    }

    @Override
    public void createOwner(CreateOwnerDto dto) {
        ownerRepository.save(OwnerMapper.toEntity(dto));
    }

    @Override
    public OwnerResponseDto getOwnerById(UUID id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Хозяин не найден."));
        return OwnerMapper.toDto(owner);
    }

    @Override
    public List<OwnerResponseDto> getAllOwners() {
        return ownerRepository.findAll()
                .stream()
                .map(OwnerMapper::toDto)
                .toList();
    }

    @Override
    public void updateOwner(UpdateOwnerDto dto) {
        Owner existingOwner = ownerRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Хозяин не найден."));

        Owner updatedOwner = OwnerMapper.toEntity(dto, existingOwner, catRepository);
        ownerRepository.save(updatedOwner);
    }

    @Override
    public void deleteOwner(UUID id) {
        ownerRepository.deleteById(id);
    }
}