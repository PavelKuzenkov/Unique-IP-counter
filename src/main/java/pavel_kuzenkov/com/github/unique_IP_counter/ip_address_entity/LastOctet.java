package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class LastOctet. Абстракция последнего октета.
 * Работает в обычном режиме инициализации. Для экономии памяти,
 * хранит в массиве не ссылки на следующий Octet, а примитивы boolean.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */
class LastOctet implements Octet {

    /**
     * Значения последнего октета.
     */
    private boolean[] lastOctets = new boolean[256];

    /**
     * Индикатор заполненности массива со значениями последнего октета.
     */
    private boolean full = false;

    /**
     * Счетчик заполненности массива с уникальными адресами.
     */
    private short fillCounter = 0;

    @Override
    public boolean isFull() {
        return full;
    }

    @Override
    public boolean addNextOctet(short[] address, int octetNumber) {
        return false;
    }
}
