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
 *
 * @author Kuzenkov Pavel.
 * @since 08.12.2019
 */
public class AggregatorImpl implements Aggregator {

    private IPAddressFileReader fileReader;

    private AddressRepository addressRepository;

    private String filepath;

    private long uniqueAddressCounter = 0;

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
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            countUniqueIP();
            return uniqueAddressCounter;
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
            System.out.println("В аггрегаторе не указан путь до файла!");
            result = false;
        }
        if (fileReader == null) {
            System.out.println("В аггрегаторе не указан file reader!");
            result = false;
        }
        if (addressRepository == null) {
            System.out.println("В аггрегаторе не указано хранилище адресов!");
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
            if (fileReader.isWorking()) {
                while ((newAddress = fileReader.getNextIPAddress()) == null && fileReader.isWorking()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            } else {
                working = false;
                break;
            }
            if (!addressRepository.isFull()) {
                addressRepository.put(newAddress);
            } else {
                working = false;
                fileReader.stopReadAndProcess();
                uniqueAddressCounter = addressRepository.getNumberOfUniqueAddresses();
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
                System.out.println("Файл " + filePath + " не найден! Проверьте корректность ввода.");
            } catch (IOException ioe) {
                System.out.println("Ошибка при чтении файла!");
            }
        }
    }
}
