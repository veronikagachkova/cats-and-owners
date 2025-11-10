package CatsAndOwners.console;

import CatsAndOwners.controller.CatController;
import CatsAndOwners.controller.OwnerController;
import CatsAndOwners.model.dto.cat.CatShortDto;
import CatsAndOwners.model.dto.cat.request.CreateCatDto;
import CatsAndOwners.model.dto.cat.request.UpdateCatDto;
import CatsAndOwners.model.dto.cat.response.CatResponseDto;
import CatsAndOwners.model.dto.owner.request.CreateOwnerDto;
import CatsAndOwners.model.dto.owner.request.UpdateOwnerDto;
import CatsAndOwners.model.dto.owner.response.OwnerResponseDto;
import CatsAndOwners.model.enums.CatBreed;
import CatsAndOwners.model.enums.CatColor;
import CatsAndOwners.model.enums.Gender;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleInterface {
    private final CatController catController;
    private final OwnerController ownerController;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleInterface(CatController catController, OwnerController ownerController) {
        this.catController = catController;
        this.ownerController = ownerController;
    }

    public void start() {
        while (true) {
            System.out.println("""
                \n=-=-=-=- Меню -=-=-=-=
                0. Выйти
                1. Добавить хозяина
                2. Добавить кота
                3. Показать всех хозяев
                4. Показать всех котов
                5. Найти хозяина
                6. Найти кота
                7. Удалить хозяина
                8. Удалить кота
                9. Посмотреть друзей кота
                10. Обновить друзей кота
                11. Обновить список котов у хозяина
                Выберите действие:
                """);

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 0 -> {
                    System.out.println("Выход...");
                    return;
                }
                case 1 -> createOwnerFromInput();
                case 2 -> createCatFromInput();
                case 3 -> displayAllOwners();
                case 4 -> displayAllCats();
                case 5 -> findOwnerById();
                case 6 -> findCatById();
                case 7 -> deleteOwnerById();
                case 8 -> deleteCatById();
                case 9 -> showCatFriends();
                case 10 -> updateCatFriends();
                case 11 -> updateOwnerCats();
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void createCatFromInput() {
        System.out.print("Имя кота: ");
        String name = scanner.nextLine();

        Gender gender = askGender();
        LocalDate birthDate = askDate("Дата рождения (yyyy-mm-dd): ");
        CatBreed breed = askEnum(CatBreed.class, "Порода");
        CatColor color = askEnum(CatColor.class, "Цвет");

        CreateCatDto dto = new CreateCatDto(
                name,
                gender,
                birthDate,
                breed,
                color,
                null);
        catController.createCat(dto);
    }

    private void createOwnerFromInput() {
        System.out.print("Имя хозяина: ");
        String name = scanner.nextLine();

        Gender gender = askGender();
        LocalDate birthDate = askDate("Дата рождения (yyyy-mm-dd): ");

        CreateOwnerDto dto = new CreateOwnerDto(
                name,
                gender,
                birthDate,
                null);
        ownerController.createOwner(dto);
    }

    private void displayAllOwners() {
        List<OwnerResponseDto> owners = ownerController.getAllOwners();
        System.out.println("\n" + "═".repeat(50));
        System.out.println("СПИСОК ВСЕХ ВЛАДЕЛЬЦЕВ");
        System.out.println("═".repeat(50));

        for (int i = 0; i < owners.size(); i++) {
            System.out.println(owners.get(i).toDisplayString());
            if (i < owners.size() - 1) {
                System.out.println("\n" + "─".repeat(50) + "\n");
            }
        }
    }

    private void displayAllCats() {
        List<CatResponseDto> cats = catController.getAllCats();
        System.out.println("\n" + "═".repeat(50));
        System.out.println("СПИСОК ВСЕХ КОТОВ");
        System.out.println("═".repeat(50));

        for (int i = 0; i < cats.size(); i++) {
            System.out.println(cats.get(i).toDisplayString());
            if (i < cats.size() - 1) {
                System.out.println("\n" + "─".repeat(50) + "\n");
            }
        }
    }

    private void findOwnerById() {
        System.out.print("Введите UUID хозяина: ");
        try {
            UUID id = UUID.fromString(scanner.nextLine());
            System.out.println(ownerController.getOwner(id));
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный формат UUID.");
        }
    }

    private void findCatById() {
        System.out.print("Введите UUID кота: ");
        try {
            UUID id = UUID.fromString(scanner.nextLine());
            System.out.println(catController.getCat(id));
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный формат UUID.");
        }
    }

    private void showCatFriends() {
        System.out.print("Введите UUID кота: ");
        UUID catId = UUID.fromString(scanner.nextLine());

        try {
            List<CatResponseDto> friends = catController.getCatFriends(catId);
            if (friends.isEmpty()) {
                System.out.println("У этого кота нет друзей.");
            } else {
                System.out.println("Друзья кота:");
                friends.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void updateCatFriends() {
        System.out.print("Введите UUID кота, которому хотите обновить друзей: ");
        UUID catId = UUID.fromString(scanner.nextLine());

        System.out.print("Введите UUID друзей через запятую: ");
        String[] friendIds = scanner.nextLine().split(",");

        List<CatShortDto> friends = Arrays.stream(friendIds)
                .map(String::trim)
                .map(UUID::fromString)
                .map(CatShortDto::new)
                .toList();

        UpdateCatDto updateDto = new UpdateCatDto();
        updateDto.setId(catId);
        updateDto.setFriends(friends);

        catController.updateCat(updateDto);
        System.out.println("Друзья кота обновлены.");
    }

    private void updateOwnerCats() {
        System.out.print("Введите UUID хозяина: ");
        UUID ownerId = UUID.fromString(scanner.nextLine());

        System.out.print("Введите UUID котов через запятую: ");
        String[] catIds = scanner.nextLine().split(",");

        List<CatShortDto> cats = Arrays.stream(catIds)
                .map(String::trim)
                .map(UUID::fromString)
                .map(CatShortDto::new)
                .toList();

        UpdateOwnerDto updateDto = new UpdateOwnerDto();
        updateDto.setId(ownerId);
        updateDto.setCats(cats);

        ownerController.updateOwner(updateDto);
        System.out.println("Коты хозяина обновлены.");
    }

    private void deleteOwnerById() {
        System.out.print("Введите UUID хозяина: ");
        UUID id = UUID.fromString(scanner.nextLine());
        ownerController.deleteOwner(id);
    }

    private void deleteCatById() {
        System.out.print("Введите UUID кота: ");
        UUID id = UUID.fromString(scanner.nextLine());
        catController.deleteCat(id);
    }

    private Gender askGender() {
        System.out.print("Пол (MALE/FEMALE): ");
        return Gender.valueOf(scanner.nextLine().toUpperCase());
    }

    private LocalDate askDate(String date) {
        System.out.print(date);
        return LocalDate.parse(scanner.nextLine());
    }

    private <T extends Enum<T>> T askEnum(Class<T> enumType, String label) {
        System.out.printf("%s (%s): ", label, Arrays.toString(enumType.getEnumConstants()));
        return Enum.valueOf(enumType, scanner.nextLine().toUpperCase());
    }
}