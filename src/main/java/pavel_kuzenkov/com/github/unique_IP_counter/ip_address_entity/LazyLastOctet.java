package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class LazyLastOctet. Абстракция последнего октета.
 * Работает в "ленивом" режиме инициализации. Вместо одного массива
 * со значениями последнего октета хранит 4 массива, которые инициализируются по мере надобности.
 * Для экономии памяти, хранит в массивах не ссылки на следующий Octet, а примитивы boolean.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */

class LazyLastOctet implements Octet {

    /**
     * Блоки "ленивой" инициализации. Содержат значения последнего октета.
     */
    private boolean[] lastOctetsBlock1 = null;
    private boolean[] lastOctetsBlock2 = null;
    private boolean[] lastOctetsBlock3 = null;
    private boolean[] lastOctetsBlock4 = null;

    /**
     * Индикатор заполненности последнего октета.
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
     * @param octetNumber номер ячейки массива, содержащей значение для следующего октета.
     * @return true если адрес уникален(в хранилище ещё нет такого адреса) и был сохран в хранилище,
     * false если хранилище уже содержит такой адрес.
     */
    @Override
    public boolean addNextOctet(short[] address, int octetNumber) {
        return true;
//        whichBlock(address[octetNumber]);
////        int position = 0;
//        if (!lastOctets[address[octetNumber]]) {
//            lastOctets[address[octetNumber]] = true;
//            if (++fillCounter == 256) full = true;
//            return true;
//        } else return false;
    }

    /**
     * Определяем, в какой блок нужно добавить новое значение.
     * @param firstOctetValue Значение октета.
     * @return номер блока.
     */
    private int whichBlock(short firstOctetValue) {
        if (firstOctetValue >= 0 && firstOctetValue < 64) return 1;
        if (firstOctetValue >= 64 && firstOctetValue < 128) return 2;
        if (firstOctetValue >= 128 && firstOctetValue < 192) return 3;
        return 4;
    }

    /**
     * Инициализация нужного блока со значениями последнего октета.
     * @param blocNumber номер блока, который необходимо инициализировать.
     */
    private void initiateOctetsBlock(int blocNumber) {
        switch (blocNumber) {
            case 1:
                lastOctetsBlock1 = new boolean[64];
                break;
            case 2:
                lastOctetsBlock2 = new boolean[64];
                break;
            case 3:
                lastOctetsBlock3 = new boolean[64];
                break;
            case 4:
                lastOctetsBlock4 = new boolean[64];
                break;
        }
    }
}
