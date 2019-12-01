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
     * @param address Строка с IP-адресом.
     * @return true если адрес уникален(в хранилище ещё нет такого адреса) и был сохран в хранилище,
     * false если хранилище уже содержит такой адрес.
     */
    public boolean put(String address);

    /**
     * Проверка хранилища на заполненность.
     * @return true если хранилище полностью заполнено(содержит все возможные варианты IP-адресов),
     * false если хранилище ещё не заполнено.
     */
    public boolean isFull();

    /**
     * Возвращает количество хранимых уникальных IP-адресов.
     * @return количество хранимых уникальных IP-адресов.
     */
    public long getNumberOfUniqueAddresses();
}
