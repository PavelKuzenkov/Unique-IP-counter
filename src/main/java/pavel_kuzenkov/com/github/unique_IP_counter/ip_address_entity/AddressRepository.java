package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Interface AddressRepository. Описывает поведение хранилища уникальных IP-адресов.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */
public interface AddressRepository {

    /**
     * Сохранение нового уникального IP-адреса в хранилище.
     * @param incomingAddress Массив значений октетов IP-адреса.
     * @return true если адрес уникален(в хранилище ещё нет такого адреса) и был сохран в хранилище,
     * false если хранилище уже содержит такой адрес.
     */
    boolean put(int[] incomingAddress);

    /**
     * Проверка хранилища на заполненность.
     * @return true если хранилище полностью заполнено(содержит все возможные варианты IP-адресов),
     * false если хранилище ещё не заполнено.
     */
    boolean isFull();

    /**
     * Возвращает количество хранимых уникальных IP-адресов.
     * @return количество хранимых уникальных IP-адресов.
     */
    long getNumberOfUniqueAddresses();
}
