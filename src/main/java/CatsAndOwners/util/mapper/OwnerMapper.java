package CatsAndOwners.util.mapper;

import CatsAndOwners.dao.cat.CatDao;
import CatsAndOwners.model.dto.owner.OwnerShortDto;
import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;
import CatsAndOwners.model.entity.Cat;
import CatsAndOwners.model.entity.Owner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OwnerMapper {
    public static OwnerShortDto toOwnerShortDto(Owner owner) {
        if (owner == null) {
            return null;
        }

        OwnerShortDto dto = new OwnerShortDto();
        dto.setId(owner.getId());
        dto.setName(owner.getName());

        return dto;
    }

    public static Owner toEntity(CreateOwnerDto dto) {
        Owner owner = new Owner();
        owner.setName(dto.getName());
        owner.setGender(dto.getGender());
        owner.setBirthDate(dto.getBirthDate());

        if (dto.getCats() != null && !dto.getCats().isEmpty()) {
            List<Cat> cats = dto.getCats().stream()
                    .map(shortDto -> {
                        Cat cat = new Cat();
                        cat.setId(shortDto.getId());
                        cat.setName(shortDto.getName());
                        cat.setGender(shortDto.getGender());
                        cat.setBirthDate(shortDto.getBirthDate());
                        cat.setBreed(shortDto.getBreed());
                        cat.setColor(shortDto.getColor());
                        cat.setOwner(owner);
                        return cat;
                    })
                    .collect(Collectors.toList());

            owner.setCats(cats);
        } else {
            owner.setCats(new ArrayList<>());
        }

        return owner;
    }

    public static Owner toEntity(UpdateOwnerDto dto, Owner existingOwner, CatDao catDao) {
        if (dto.getCats() != null && !dto.getCats().isEmpty()) {
            List<Cat> cats = dto.getCats().stream()
                    .map(catDto -> {
                        Cat cat = catDao.findOne(catDto.getId());
                        if (cat == null) {
                            throw new IllegalArgumentException("Кот с ID " + catDto.getId() + " не найден");
                        }
                        cat.setOwner(existingOwner); // Привязываем к хозяину
                        return cat;
                    })
                    .toList();
            existingOwner.setCats(cats);
        } else {
            existingOwner.setCats(new ArrayList<>());
        }

        return existingOwner;
    }

    public static OwnerResponseDto toDto(Owner owner) {
        return new OwnerResponseDto(
                owner.getId(),
                owner.getName(),
                owner.getGender(),
                owner.getBirthDate(),
                owner.getCats().stream()
                        .map(CatMapper::toCatShortDto)
                        .toList()
        );
    }
}