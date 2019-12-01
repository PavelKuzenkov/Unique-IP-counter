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
    private Octet[] nextOctets = null;

    /**
     * Индикатор заполненности массива со следующими октетами.
     */
    private boolean full = false;

    /**
     * Счетчик заполненности массива со следующим октетом.
     */
    private short fillCounter = 0;

    /**
     * Конструктор с параметрами. Принимает порядковый номер создаваемого октета.
     * Если октет предпоследний (третий) - то нужно инициализировать массив классом LastOctet.
     * @param octetNumber номер создаваемого октета.
     */
    public NotLastOctet(int octetNumber) {
        if (octetNumber < 3) {
            nextOctets = new NotLastOctet[256];
        } else {
            nextOctets = new LastOctet[256];
        }
    }

    /**
     * Проверка данного октета на заполненность.
     * @return true если данный и следующий октет заполнены(содержит все возможные варианты),
     * false если октет ещё не заполнен.
     */
    @Override
    public boolean isFull() {
        return full;
    }

    /**
     * Добавление нового значения октета. Учет заполненности октета.
     * @param address массив со значениями октетов.
     * @param octetNumber номер ячейки массива, содержащей значение для следующего октета.
     * @return true если адрес уникален(в хранилище ещё нет такого адреса) и был сохран в хранилище,
     * false если хранилище уже содержит такой адрес.
     */
    @Override
    public boolean addNextOctet(short[] address, int octetNumber) {
        //TODO сам себя загнал в тупик) Как теперь понять, какой октет следующий? Каждый раз проверять?
        return false;
    }
}
