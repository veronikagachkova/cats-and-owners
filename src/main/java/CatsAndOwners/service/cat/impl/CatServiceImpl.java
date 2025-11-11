package CatsAndOwners.service.cat.impl;

import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;
import CatsAndOwners.model.entity.Cat;
import CatsAndOwners.model.enums.CatBreed;
import CatsAndOwners.model.enums.CatColor;
import CatsAndOwners.repository.cat.CatRepository;
import CatsAndOwners.service.cat.CatService;
import CatsAndOwners.util.mapper.CatMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CatServiceImpl implements CatService {
    private final CatRepository catRepository;

    @Autowired
    public CatServiceImpl(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @Override
    public void createCat(CreateCatDto dto) {
        catRepository.save(CatMapper.toEntity(dto));
    }

    @Override
    public CatResponseDto getCatById(UUID id) {
        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Кот не найден."));
        return CatMapper.toDto(cat);
    }

    @Override
    public List<CatResponseDto> getFriends(UUID id) {
        List<Cat> friends = catRepository.findFriends(id);
        return friends.stream()
                .map(CatMapper::toDto)
                .toList();
    }

    @Override
    public List<CatResponseDto> getAllCats() {
        return catRepository.findAll()
                .stream()
                .map(CatMapper::toDto)
                .toList();
    }

    @Override
    public List<CatResponseDto> getCatsByColor(CatColor color) {
        List<Cat> cats = catRepository.findByColor(color);
        return cats.stream()
                .map(CatMapper::toDto)
                .toList();
    }

    @Override
    public List<CatResponseDto> getCatsByBreed(String breed) {
        CatBreed catBreed = CatBreed.valueOf(breed.toUpperCase());
        List<Cat> cats = catRepository.findByBreed(catBreed);
        return cats.stream()
                .map(CatMapper::toDto)
                .toList();
    }

    @Override
    public void updateCat(UpdateCatDto dto) {
        Cat existingCat = catRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Кот не найден."));
        Cat updatedCat = CatMapper.toEntity(dto, existingCat);
        catRepository.save(updatedCat);
    }

    @Override
    public void deleteCat(UUID id) {
        catRepository.deleteById(id);
    }
}