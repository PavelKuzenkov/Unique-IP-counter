package pavel_kuzenkov.com.github.unique_IP_counter.agregator;

import pavel_kuzenkov.com.github.unique_IP_counter.file_reader.IPAddressFileReader;
import pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity.AddressRepository;

/**
 * Interface Aggregator. Описывает поведение управления рабочим процессом. Передает путь к файлу в
 * IPAddressFileReader, даёт ключ на старт чтения, запрашивает у IPAddressFileReader'а
 * IP-адреса, передает их в AddressRepository и подсчитывает количество уникальных адресов.
 *
 * @author Kuzenkov Pavel.
 * @since 08.12.2019
 */
public interface Aggregator {

    /**
     * Установка хранилища (тип алгоритма), file reader'а и старт процесса подсчёта количества уникальных IP-адресов в текстовом файле.
     * @param filePath путь до файла.
     * @param repository хранилище IP-адресов.
     * @param reader file reader.
     * @return количество уникальных IP-адресов в файле.
     */
    long startProcess(String filePath, AddressRepository repository, IPAddressFileReader reader);
}
