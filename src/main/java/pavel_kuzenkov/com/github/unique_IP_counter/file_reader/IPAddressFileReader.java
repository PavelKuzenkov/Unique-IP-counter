package pavel_kuzenkov.com.github.unique_IP_counter.file_reader;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Интерфейс описывающий поведение класса, для открытия текстового файла и чтения
 * из него IP-адресов. Классу на вход приходит строка с путём до нужного файла, и даётся сигнал на старт.
 * Класс проверяет файл на наличие и начинает его читать построчно. Каждая строка проверяется, является
 * ли она валибным IP-адресом. Если нет - то просто отбрасывается. Если да - то преобразуется в
 * массив целых чисел и возвращается методом getNextIPAddress(), по запросу из вне.
 *
 * @author Kuzenkov Pavel.
 * @since 05.12.2019
 */
public interface IPAddressFileReader {

    /**
     * Команда на старт. Поиск и открытие текстового файла с IP-адресами.
     * Обработка строк в файле. преобразование строк с IP-адресом в массив значений октетов.
     * @param path Путь до текстового файла с IP-адресами.
     * @throws FileNotFoundException Выбрасывается в случае если не удалось найти файл по заданному пути.
     * @throws IOException Ошибка ввода вывода.
     */
    void startReadAndProcess(String path) throws FileNotFoundException, IOException;

    /**
     * Возвращает массив значений октетов IP-адреса. Каждый раз возвращает значения октетов для следующей
     * прочитанной строки в текстовом файле.
     * @return массив значений октетов IP-адреса. null в случае, если следующая строка из файла еще
     * не обработана.
     */
    int[] getNextIPAddress();

    /**
     * Команда на остановку чтения из файла. Закрытие файла.
     */
    void stopReadAndProcess();

    /**
     * Проверка, работает ли ещё IPAddressFileReader, происходит ли чтение из файла. Не закончился ли файл с IP-адресами.
     * @return true если идёт чтение из файла, false если чтение прекратилось, файл закончился.
     */
    boolean isWorking();
}
