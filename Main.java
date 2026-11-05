import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    // Використовуємо Locale.of замість застарілого конструктора new Locale(...)
    private static Locale currentLocale = Locale.of("uk", "UA");
    private static ResourceBundle bundle = ResourceBundle.getBundle("location.messages", currentLocale);

    private static void setupLogger() {
        try {
            LogManager.getLogManager().reset();
            Logger rootLogger = Logger.getLogger("");
            rootLogger.setLevel(Level.ALL);

            // 1. Консольний handler: INFO і вище
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(consoleHandler);

            // 2. Файловий handler: FINE і вище у файл app.log
            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setLevel(Level.FINE);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);

        } catch (Exception e) {
            System.err.println("Не вдалося налаштувати логування: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        setupLogger();
        logger.fine("Програма успішно запущена.");

        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)));
        List<Student> students = new ArrayList<>();
        students.add(new Student("Kira", 19, "126"));

        while (true) {
            System.out.println("\n" + bundle.getString("menu.title"));
            System.out.println(bundle.getString("menu.show"));
            System.out.println(bundle.getString("menu.add"));
            System.out.println(bundle.getString("menu.save"));
            System.out.println(bundle.getString("menu.load"));
            System.out.println(bundle.getString("menu.lang"));
            System.out.println(bundle.getString("menu.exit"));
            System.out.print(bundle.getString("menu.choice"));

            if (!scanner.hasNextInt()) {
                logger.warning("Користувач ввів не числове значення у головному меню.");
                System.out.println(bundle.getString("msg.not.number"));
                scanner.next(); 
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); 
            logger.fine("Користувач обрав пункт меню: " + choice);

            switch (choice) {
                case 1:
                    logger.info("Перегляд списку студентів.");
                    System.out.println(bundle.getString("msg.current.list"));

                    if (students.isEmpty()) {
                        logger.info("Список студентів виявився порожнім.");
                        System.out.println(bundle.getString("msg.empty.list"));
                    } else {
                        for (Student s : students) {
                            System.out.println("- " + s);
                        }
                    }
                    break;

                case 2:
                    logger.info("Початок процесу додавання нового студента.");
                    System.out.print(bundle.getString("msg.enter.name"));
                    String name = scanner.nextLine();
                    
                    System.out.print(bundle.getString("msg.enter.age"));
                    while (!scanner.hasNextInt()) {
                        logger.warning("Спроба ввести невірний формат віку.");
                        System.out.println(bundle.getString("msg.age.error"));
                        scanner.next();
                    }
                    int age = scanner.nextInt();
                    scanner.nextLine(); 
                    
                    System.out.print(bundle.getString("msg.enter.spec"));
                    String specialty = scanner.nextLine();

                    students.add(new Student(name, age, specialty));
                    logger.info("Студента успішно додано до колекції: " + name + ", " + age + ", " + specialty);
                    System.out.println(" " + bundle.getString("msg.student.added"));
                    break;

                case 3:
                    System.out.print(bundle.getString("msg.save.file"));
                    String saveFile = scanner.nextLine();
                    FileManager.saveObject(saveFile, students);
                    break;

                case 4:
                    System.out.print(bundle.getString("msg.load.file"));
                    String loadFile = scanner.nextLine();
                    Object loadedData = FileManager.loadObject(loadFile);
                    if (loadedData != null) {
                        students = (List<Student>) loadedData;
                        logger.info("Колекцію в пам'яті програми оновлено даними з файлу.");
                        System.out.println(bundle.getString("msg.data.updated"));
                    }
                    break;

                case 5: // Вибір мови
                    System.out.print(bundle.getString("lang.prompt"));
                    if (scanner.hasNextInt()) {
                        int langChoice = scanner.nextInt();
                        scanner.nextLine();
                        if (langChoice == 1) {
                            currentLocale = Locale.of("uk", "UA");
                        } else if (langChoice == 2) {
                            currentLocale = Locale.of("en", "US");
                        }
                        bundle = ResourceBundle.getBundle("location.messages", currentLocale);
                        logger.info("Змінено мову інтерфейсу.");
                        System.out.println(bundle.getString("lang.changed"));
                    } else {
                        scanner.next();
                    }
                    break;

                case 0:
                    logger.info("Завершення роботи програми.");
                    System.out.println(bundle.getString("msg.exit.prog"));
                    scanner.close();
                    return;

                default:
                    logger.warning("Користувач ввів неіснуючий пункт меню: " + choice);
                    System.out.println(bundle.getString("msg.invalid.choice"));
            }
        }
    }
}
