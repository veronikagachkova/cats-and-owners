package CatsAndOwners.service.cat.impl;

import CatsAndOwners.dao.cat.CatDao;
import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;
import CatsAndOwners.model.entity.Cat;
import CatsAndOwners.service.cat.CatService;
import CatsAndOwners.util.mapper.CatMapper;

import java.util.List;
import java.util.UUID;

public class CatServiceImpl implements CatService {
    private final CatDao catDao;

    public CatServiceImpl(CatDao catDao) {
        this.catDao = catDao;
    }

    @Override
    public void createCat(CreateCatDto dto) {
        catDao.create(CatMapper.toEntity(dto));
    }

    @Override
    public CatResponseDto getCatById(UUID id) {
        return CatMapper.toDto(catDao.findOne(id));
    }

    @Override
    public List<CatResponseDto> getFriends(UUID id) {
        List<Cat> friends = catDao.findFriends(id);
        return friends.stream()
                .map(CatMapper::toDto)
                .toList();
    }

    @Override
    public List<CatResponseDto> getAllCats() {
        return catDao.findMany()
                .stream()
                .map(CatMapper::toDto)
                .toList();
    }

    @Override
    public void updateCat(UpdateCatDto dto) {
        Cat existingCat = catDao.findOne(dto.getId());
        Cat updatedCat = CatMapper.toEntity(dto, existingCat, catDao);
        catDao.update(updatedCat);
    }

    @Override
    public void deleteCat(UUID id) {
        catDao.delete(id);
    }
}