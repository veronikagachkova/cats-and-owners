package CatsAndOwners.model.dto.cat.response;

import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.dto.owner.OwnerShortDto;
import CatsAndOwners.model.enums.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public record CatResponseDto(
        UUID id,
        String name,
        Gender gender,
        LocalDate birthDate,
        CatBreed breed,
        CatColor color,
        OwnerShortDto owner,
        List<CatShortDto> friends
) {
    public String toDisplayString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        StringBuilder sb = new StringBuilder();

        sb.append("╔═══════════════════════════════════════╗\n");
        sb.append("║                 КОТ                   ║\n");
        sb.append("╚═══════════════════════════════════════╝\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Имя: ").append(name).append("\n");
        sb.append("Пол: ").append(gender == Gender.MALE ? "самец" : "самка").append("\n");
        sb.append("Дата рождения: ").append(birthDate.format(formatter)).append("\n");
        sb.append("Порода: ").append(breed != null ? breed.getDisplayName() : "не указана").append("\n");
        sb.append("Окрас: ").append(color != null ? color.getDisplayName() : "не указан").append("\n");

        if (owner != null) {
            sb.append("Владелец: ").append(owner.getName())
                    .append(" (ID: ").append(owner.getId()).append(")\n");
        } else {
            sb.append("Владелец: нет владельца\n");
        }

        if (friends == null || friends.isEmpty()) {
            sb.append("Друзья: нет друзей\n");
        } else {
            sb.append("Друзья (").append(friends.size()).append("):\n");
            for (CatShortDto friend : friends) {
                sb.append("  • ").append(friend.getName()).append(" (ID: ").append(friend.getId()).append(")\n");
            }
        }

        return sb.toString();
    }
}