package pavel_kuzenkov.com.github.unique_IP_counter.agregator;

import pavel_kuzenkov.com.github.unique_IP_counter.file_reader.IPAddressFileReader;
import pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity.AddressRepository;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class Aggregator. Класс для управления рабочим процессом. Передает путь к файлу в
 * IPAddressFileReader, даёт ключ на старт чтения, запрашивает у IPAddressFileReader'а
 * IP-адреса, передает их в AddressRepository и подсчитывает количество уникальных адресов.
 * Вернул обратно раздельную конфигурацию. Путь, хранилище и file reader указываются в отделных методах.
 * Да, с точки зрения повторного использования не слишком удачно, потому что нужно вызывать startProcess()
 * только после вызова остальных методов. Сделано для подстройки под реализацию меню, которая написана год назад.
 * Работа в 2 потока. Один поток читает из файла строки, а другой передаёт их в хранилище.
 *
 * @author Kuzenkov Pavel.
 * @since 08.12.2019
 */
public class AggregatorImpl implements Aggregator {

    /**
     * Поле с классом для чтения файла.
     */
    private IPAddressFileReader fileReader;

    /**
     * Хранилище уникальных IP-адресов.
     */
    private AddressRepository addressRepository;

    /**
     * Имя файла с IP-адресами.
     */
    private String filepath;

    /**
     * Поле с результатом подсчётов.
     */
    private long numberOfUniqueAddress = 0;

    /**
     * Индикатор работы аггрегатора. 
     */
    private boolean working = false;

    /**
     * Старт процесса подсчёта количества уникальных IP-адресов в текстовом файле. Должен вызываться
     * после setAddressRepository(), setFilePath(), setIPAddressFileReader().
     * @return количество уникальных IP-адресов в файле. -1 в случае неверной конфигурации Аггрегатора.
     */
    @Override
    public long startProcess() {
        if (checkConfig()) {
            startReadFile(this.filepath);
            System.out.println("Processing...");
            try {
                Thread.sleep(100); //Засыпаем на всякий случай. Чтобы процесс чтения точно запустился.
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            countUniqueIP();
            return numberOfUniqueAddress;
        } else {
            return -1;
        }
    }

    /**
     * Проверяем сконфигурирован ли аггрегатор.
     * @return true если сконфигурирован, false если нет.
     */
    private boolean checkConfig() {
        boolean result = true;
        if (filepath == null) {
            System.out.println("No file path specified in aggregator!");
            result = false;
        }
        if (fileReader == null) {
            System.out.println("No file reader specified in aggregator!");
            result = false;
        }
        if (addressRepository == null) {
            System.out.println("No address repository specified in aggregator!");
            result = false;
        }
        return result;
    }

    /**
     * Запуск чтения из файла.
     * @param filePath путь к файлу.
     */
    private void startReadFile(String filePath) {
        Thread readingThread = new Thread(new ReadingFile(filePath));
        readingThread.start();
    }

    /**
     * Запрос у file reader'а IP-адресов, предача их в хранилище и подсчет количества уникальных IP-адресов;
     */
    private void countUniqueIP() {
        working = true;
        int[] newAddress;
        while (working) {
            if (fileReader.isReady()) {
                if ((newAddress = fileReader.getNextIPAddress()) != null) {
                    addressRepository.put(newAddress);
                    if (addressRepository.isFull()) {
                        fileReader.stopReadAndProcess();
                        numberOfUniqueAddress = addressRepository.getNumberOfUniqueAddresses();
                        working = false;
                        break;
                    }
                }
            } else {
                working = false;
                numberOfUniqueAddress = addressRepository.getNumberOfUniqueAddresses();
                break;
            }
        }
    }

    /**
     * Установка хранилища адресов (тип алгоритма).
     * @param repository хранилище IP-адресов.
     */
    @Override
    public void setAddressRepository(AddressRepository repository) {
        addressRepository = repository;
    }

    /**
     * Установка путя до файла.
     * @param filePath путь до файла.
     */
    @Override
    public void setFilePath(String filePath) {
        this.filepath = filePath;
    }

    /**
     * Установка file reader'а.
     * @param reader file reader.
     */
    @Override
    public void setIPAddressFileReader(IPAddressFileReader reader) {
        fileReader = reader;
    }

    /**
     * Отдельный поток для чтения файла
     */
    private class ReadingFile implements Runnable {

        private String filePath;

        ReadingFile(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {
            try {
                fileReader.startReadAndProcess(filePath);
            }  catch (FileNotFoundException fnfe) {
                System.out.println("File " + filePath + " not found! Check that the input is correct.");
            } catch (IOException ioe) {
                System.out.println("Error reading file!");
            }
        }
    }
}
