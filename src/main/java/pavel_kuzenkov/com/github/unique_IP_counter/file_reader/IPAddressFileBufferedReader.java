package pavel_kuzenkov.com.github.unique_IP_counter.file_reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

/**
 * Class IPAddressFileBufferedReader. Класс для открытия текстового файла и чтения из него IP-адресов.
 * Классу на вход приходит строка с путём до нужного файла, и даётся сигнал на старт.
 * Класс проверяет файл на наличие и начинает его читать построчно. Каждая строка проверяется, является
 * ли она валибным IP-адресом. Если нет - то просто отбрасывается. Если да - то преобразуется в
 * массив целых чисел, складывается в буфер, где ждёт в очереди, пока её не запросят из вне методом
 * getNextIPAddress().
 *
 * @author Kuzenkov Pavel.
 * @since 05.12.2019
 */
public class IPAddressFileBufferedReader implements IPAddressFileReader {

    /**
     * Буфер для валидных IP-адресов в виде массивов чисел. Сюда попадают результаты
     * обработки только тех строки из текстового файла, которые представляют собой IP-адрес.
     * Массивы состоят из 4 ячеек, каждая ячейка содержыт значение актета.
     */
    private Queue<int[]> buffer = new ConcurrentLinkedQueue<>();

    /**
     * Количество прочитанных из файла строк.
     */
    private long lineCounter = 0;

    /**
     * Количество найденныйх валидных IP-адресов.
     */
    private long validAddressesCounter = 0;

    /**
     * Флаг, благодаря которому даётся команда прекратить читать строки из файла. Также это индикатор
     * того, доступен ли файл для чтения.
     */
    private boolean reading = false;

    /**
     * Регулярное выражение, для определения, является ли строка валидным IP-адресом.
     */
    private final String IP_REGEXP = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    /**
     * Паттерн для определения, является ли строка валидным IP-адресом.
     */
    private final Pattern IP_PATTERN = Pattern.compile(IP_REGEXP);


    /**
     * Команда на старт. Поиск и открытие текстового файла с IP-адресами.
     * Обработка строк в файле. преобразование строк с IP-адресом в массив значений октетов.
     * @param path Путь до текстового файла с IP-адресами.
     * @throws FileNotFoundException Выбрасывается в случае если не удалось найти файл по заданному пути.
     */
    @Override
    public void startReadAndProcess(String path) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            reading = true;
            String line;
            while ((line = br.readLine()) != null && reading) {
                lineCounter++;
                while (!processAndToBuff(line, lineCounter)) {
                    Thread.sleep(100);
                }
            }
            if (reading) {
            System.out.println("Конец файла. Прочитано " +
                    lineCounter + " строк. Из них " +
                    validAddressesCounter + " валидных IP-адресов.");
            } else {
                System.out.println("Чтение файла прервано. Прочитано " +
                        lineCounter + " строк. Из них " +
                        validAddressesCounter + " валидных IP-адресов.");
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } finally {
            reading = false;
        }
    }

    /**
     * Обработка Входящей строки. Проверка на валидный IP-адрес. Преобразование в массив с октетами и сохранение в буфер.
     * Возвращаемый результат используется в качестве "отмашки" для продолжения чтения из файла. Именно поэтому
     * возвращается true если в строке не был найден IP-адрес. В этом случае в консоль выводится соответствующее
     * сообщение, а данная строка просто игнорируется. При добавлении массива в буфер, считается количество найденных
     * валидных IP-адресов.
     * @param incomingLine Входящая строка.
     * @return tru если строка успешно обработана и результат добавлен в буфер, или если строка не является валидным
     * IP-адресом т.е. можно продолжать чтение из файла.
     * false если если буфер заполнен.
     */
    private boolean processAndToBuff(String incomingLine, long lineCounter) {
        if (!IP_PATTERN.matcher(incomingLine).matches()) {
            System.out.println("Строка №" + lineCounter + ": \"" + "\" не является валидным IP-адресом, и была проигнорирована!");
            return true;
        }
        int[] processResult = getOctetsArray(incomingLine);
        if (buffer.size() < 2048 && buffer.offer(processResult)) {
            validAddressesCounter++;
            return true;
        } else return false;
    }

    /**
     * Преобразование строки с IP-адресом в массив со значениями октетов.
     * @param incomingLine строка с IP-адресом.
     * @return массив со значениями октетов.
     */
    private int[] getOctetsArray(String incomingLine) {
        int[] result = new int[4];
        String[] split = incomingLine.split("\\.");
        for (int i = 0; i < 4; i++) {
            result[i] = Integer.parseInt(split[i]);
        }
        return result;
    }

    /**
     * Возвращает массив значений октетов IP-адреса. Каждый раз возвращает значения октетов для следующей
     * прочитанной строки в текстовом файле.
     * @return массив значений октетов IP-адреса.
     */
    @Override
    public int[] getNextIPAddress() {
        return buffer.poll();
    }

    /**
     * Команда на остановку чтения из файла.
     */
    @Override
    public void stopReadAndProcess() {
        reading = false;
        buffer.clear();
    }

    /**
     * Проверка, работает ли ещё IPAddressFileReader, происходит ли чтение из файла. Не закончился ли файл с IP-адресами.
     * @return true если идёт чтение из файла, false если чтение прекратилось, файл закончился.
     */
    @Override
    public boolean isWorking() {
        return reading;
    }
}
