import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {
    private static final Logger logger = Logger.getLogger(FileManager.class.getName());

    // Метод для збереження колекції у файл
    public static void saveObject(String filename, Object data) {
        logger.info("Спроба збереження даних у файл: " + filename);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
            logger.info("Дані успішно збережено у файл: " + filename);
            System.out.println(" Дані успішно збережено у файл: " + filename);
        } catch (IOException e) {
            // Фіксуємо помилку з рівнем SEVERE
            logger.log(Level.SEVERE, "Помилка збереження файлу: " + filename, e);
            System.out.println(" Помилка збереження файлу: " + e.getMessage());
        }
    }

    // Метод для читання об'єкта з файлу
    public static Object loadObject(String filename) {
        logger.info("Спроба завантаження даних з файлу: " + filename);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object data = ois.readObject();
            logger.info("Дані успішно завантажено з файлу: " + filename);
            System.out.println(" Дані успішно завантажено з файлу: " + filename);
            return data;
        } catch (IOException | ClassNotFoundException e) {
            // Фіксуємо помилку читання або десеріалізації
            logger.log(Level.WARNING, "Помилка читання файлу: " + filename, e);
            System.out.println(" Помилка читання файлу: " + e.getMessage());
            return null;
        }
    }
}
