package CatsAndOwners.model.dto.owner.response;

import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.enums.Gender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public record OwnerResponseDto(
        UUID id,
        String name,
        Gender gender,
        LocalDate birthDate,
        List<CatShortDto> cats
) {
    public String toDisplayString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        StringBuilder sb = new StringBuilder();

        sb.append("╔═══════════════════════════════════════╗\n");
        sb.append("║              ВЛАДЕЛЕЦ                 ║\n");
        sb.append("╚═══════════════════════════════════════╝\n");
        sb.append("ID: ").append(id()).append("\n");
        sb.append("Имя: ").append(name()).append("\n");
        sb.append("Пол: ").append(gender() == Gender.MALE ? "мужской" : "женский").append("\n");
        sb.append("Дата рождения: ").append(birthDate().format(formatter)).append("\n");

        if (cats() == null || cats().isEmpty()) {
            sb.append("Коты: нет котов\n");
        } else {
            sb.append("Коты (").append(cats().size()).append("):\n");
            for (CatShortDto cat : cats()) {
                sb.append("  • ").append(cat.getName()).append(" (ID: ").append(cat.getId()).append(")\n");
            }
        }

        return sb.toString();
    }
}