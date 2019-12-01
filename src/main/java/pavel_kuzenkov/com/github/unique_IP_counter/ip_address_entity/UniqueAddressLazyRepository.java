package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class UniqueAddressRepository. Хранилище уникальных IP-адресов.
 * Работает в "ленивом" режиме инициализаци. Представляет собой четырёхуровневое
 * "дерево". По одному уровню на каждый октет IP-адреса.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */

public class UniqueAddressLazyRepository implements AddressRepository {

    /**
     * Блоки "ленивой" инициализации. Содержат уникальные адреса
     */
    private Octet[] addressesBlock1 = new Octet[64];
    private Octet[] addressesBlock2 = new Octet[64];
    private Octet[] addressesBlock3 = new Octet[64];
    private Octet[] addressesBlock4 = new Octet[64];

    /**
     * Индикатор заполненности хранилища.
     */
    private boolean full = false;

    /**
     * Индикаторы заполненности блоков "ленивой" инициализации.
     */
    private boolean block1full = false;
    private boolean block2full = false;
    private boolean block3full = false;
    private boolean block4full = false;

    /**
     * Счетчики заполненности блоков "ленивой" инициализации.
     */
    private byte fillCounter1 = 0;
    private byte fillCounter2 = 0;
    private byte fillCounter3 = 0;
    private byte fillCounter4 = 0;

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
