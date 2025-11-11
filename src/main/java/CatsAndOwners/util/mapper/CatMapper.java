package CatsAndOwners.util.mapper;

import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;
import CatsAndOwners.model.entity.Cat;
import CatsAndOwners.model.entity.Owner;

import java.util.List;
import java.util.stream.Collectors;

import static CatsAndOwners.util.mapper.OwnerMapper.toOwnerShortDto;

public class CatMapper {
    public static CatShortDto toCatShortDto(Cat cat) {
        CatShortDto dto = new CatShortDto();
        dto.setId(cat.getId());
        dto.setName(cat.getName());

        return dto;
    }

    public static Cat toEntity(CreateCatDto dto) {
        Cat cat = new Cat();
        cat.setName(dto.getName());
        cat.setGender(dto.getGender());
        cat.setBirthDate(dto.getBirthDate());
        cat.setBreed(dto.getBreed());
        cat.setColor(dto.getColor());

        if (dto.getOwner() != null) {
            Owner owner = new Owner();
            owner.setId(dto.getOwner().getId());
            cat.setOwner(owner);
        }

        if (dto.getFriends() != null) {
            List<Cat> friends = dto.getFriends().stream().map(shortDto -> {
                Cat friend = new Cat();
                friend.setId(shortDto.getId());
                return friend;
            }).collect(Collectors.toList());

            cat.setFriends(friends);
        }

        return cat;
    }

    public static Cat toEntity(UpdateCatDto dto, Cat existingCat) {
        List<Cat> friends = dto.getFriends().stream()
                .map(shortDto -> {
                    Cat friend = new Cat();
                    friend.setId(shortDto.getId());
                    return friend;
                })
                .toList();

        existingCat.setFriends(friends);

        return existingCat;
    }

    public static CatResponseDto toDto(Cat cat) {
        return new CatResponseDto(
                cat.getId(),
                cat.getName(),
                cat.getGender(),
                cat.getBirthDate(),
                cat.getBreed(),
                cat.getColor(),
                toOwnerShortDto(cat.getOwner()),
                cat.getFriends().stream()
                        .map(CatMapper::toCatShortDto)
                        .toList()
        );
    }
}