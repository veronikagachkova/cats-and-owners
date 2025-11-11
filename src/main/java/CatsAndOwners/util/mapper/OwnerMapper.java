package CatsAndOwners.util.mapper;

import CatsAndOwners.model.dto.owner.OwnerShortDto;
import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;
import CatsAndOwners.model.entity.Cat;
import CatsAndOwners.model.entity.Owner;
import CatsAndOwners.repository.cat.CatRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OwnerMapper {
    public static OwnerShortDto toOwnerShortDto(Owner owner) {
        if (owner == null) {
            return null;
        }

        OwnerShortDto dto = new OwnerShortDto();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setGender(owner.getGender());
        dto.setBirthDate(owner.getBirthDate());

        return dto;
    }

    public static Owner toEntity(CreateOwnerDto dto) {
        Owner owner = new Owner();
        owner.setId(UUID.randomUUID());
        owner.setName(dto.getName());
        owner.setGender(dto.getGender());
        owner.setBirthDate(dto.getBirthDate());

        if (dto.getCats() != null) {
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
        }

        return owner;
    }

    public static Owner toEntity(UpdateOwnerDto dto, Owner existingOwner, CatRepository catRepository) {
        existingOwner.getCats().forEach(cat -> cat.setOwner(null));
        existingOwner.getCats().clear();

        List<Cat> cats = dto.getCats().stream()
                .map(shortDto -> {
                    Cat cat = catRepository.findById(shortDto.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Кот не найден: " + shortDto.getId()));

                    cat.setOwner(existingOwner);
                    return cat;
                })
                .collect(Collectors.toList());

        existingOwner.getCats().addAll(cats);
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