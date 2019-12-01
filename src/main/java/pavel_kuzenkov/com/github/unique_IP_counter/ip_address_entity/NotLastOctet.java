package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class LastOctet. Абстракция последнего октета.
 * Работает в обычном режиме инициализации. Значение текущего октета
 * определяется номером ячейки массива предыдущего октета.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */
class NotLastOctet implements Octet {

    /**
     * Массив со следующими октетами.
     */
    private Octet[] nextOctets = new Octet[256];

    /**
     * Индикатор заполненности массива со следующими октетами.
     */
    private boolean full = false;

    /**
     * Счетчик заполненности массива со следующим октетом.
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
