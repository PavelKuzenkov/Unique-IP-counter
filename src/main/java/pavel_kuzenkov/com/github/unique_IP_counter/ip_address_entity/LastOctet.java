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
    private int fillCounter = 0;

    /**
     * Проверка данного октета на заполненность.
     * @return true если данный и следующий(если он есть) октет заполнены(содержит все возможные варианты),
     * false если октет ещё не заполнен.
     */
    @Override
    public boolean isFull() {
        return full;
    }

    /**
     * Добавление нового значения октета. Учет заполненности октета.
     * @param address массив со значениями октетов.
     * @return true если адрес уникален(в хранилище ещё нет такого адреса) и был сохран в хранилище,
     * false если хранилище уже содержит такой адрес.
     */
    @Override
    public boolean addNextOctet(int[] address) {
        if (!lastOctets[address[3]]) {
            lastOctets[address[3]] = true;
            if (++fillCounter >= 256) {
                full = true;
                lastOctets = null; //В данный октет пришли все возможные значения. Начинаем освобождать память (схлопывать ветвь).
            }
            return true;
        } else return false;
    }
}
