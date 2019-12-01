package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class UniqueAddressRepository. Хранилище уникальных IP-адресов.
 * Работает в обычном режиме обычной инициализаци. Представляет собой четырёхуровневое
 * "дерево". По одному уровню на каждый октет IP-адреса.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */

public class UniqueAddressRepository implements AddressRepository{

    /**
     * Массив с уникальными адресами.
     */
    private Octet[] addresses = new Octet[256];

    /**
     * Индикатор заполненности хранилища.
     */
    private boolean full = false;

    /**
     * Счетчик заполненности массива с уникальными адресами.
     */
    private int fillCounter = 0;

    /**
     * Счетчик уникальных адресов.
     */
    private long uniqueAddressCounter = 0;

    @Override
    public boolean put(String address) {

        return false;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public long getNumberOfUniqueAddresses() {
        return uniqueAddressCounter;
    }
}
