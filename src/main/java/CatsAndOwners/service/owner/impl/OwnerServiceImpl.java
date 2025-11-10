package CatsAndOwners.service.owner.impl;

import CatsAndOwners.dao.cat.CatDao;
import CatsAndOwners.dao.owner.OwnerDao;
import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;
import CatsAndOwners.model.entity.Cat;
import CatsAndOwners.model.entity.Owner;
import CatsAndOwners.service.owner.OwnerService;
import CatsAndOwners.util.mapper.OwnerMapper;

import java.util.List;
import java.util.UUID;

public class OwnerServiceImpl implements OwnerService {
    private final OwnerDao ownerDao;
    private final CatDao catDao;

    public OwnerServiceImpl(OwnerDao ownerDao, CatDao catDao) {
        this.ownerDao = ownerDao;
        this.catDao = catDao;
    }

    @Override
    public void createOwner(CreateOwnerDto dto) {
        ownerDao.create(OwnerMapper.toEntity(dto));
    }

    @Override
    public OwnerResponseDto getOwnerById(UUID id) {
        return OwnerMapper.toDto(ownerDao.findOne(id));
    }

    @Override
    public List<OwnerResponseDto> getAllOwners() {
        return ownerDao.findMany()
                .stream()
                .map(OwnerMapper::toDto)
                .toList();
    }

    @Override
    public void updateOwner(UpdateOwnerDto dto) {
        Owner existingOwner = ownerDao.findOne(dto.getId());
        Owner updatedOwner = OwnerMapper.toEntity(dto, existingOwner, catDao);
        ownerDao.update(updatedOwner);
    }

    @Override
    public void deleteOwner(UUID id) {
        ownerDao.delete(id);
    }
}