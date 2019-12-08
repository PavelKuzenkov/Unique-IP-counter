package pavel_kuzenkov.com.github.unique_IP_counter.agregator;

import pavel_kuzenkov.com.github.unique_IP_counter.file_reader.IPAddressFileReader;
import pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity.AddressRepository;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class Aggregator. Класс для управления рабочим процессом. Передает путь к файлу в
 * IPAddressFileReader, даёт ключ на старт чтения, запрашивает у IPAddressFileReader'а
 * IP-адреса, передает их в AddressRepository и подсчитывает количество уникальных адресов.
 *
 * @author Kuzenkov Pavel.
 * @since 08.12.2019
 */
public class AggregatorImpl implements Aggregator {

    private IPAddressFileReader fileReader;

    private AddressRepository addressRepository;

    private long uniqueAddressCounter = 0;

    private boolean working = false;

    /**
     * Установка хранилища (тип алгоритма), file reader'а и старт процесса подсчёта количества уникальных IP-адресов в текстовом файле.
     * @param filePath путь до файла.
     * @param repository хранилище IP-адресов.
     * @param reader file reader.
     * @return количество уникальных IP-адресов в файле.
     */
    @Override
    public long startProcess(String filePath, AddressRepository repository, IPAddressFileReader reader) {
        fileReader = reader;
        addressRepository = repository;
        startReadFile(filePath);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        countUniqueIP();
        return uniqueAddressCounter;
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
