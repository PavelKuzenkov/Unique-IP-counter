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

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean addNextOctet(short[] address, int octetNumber) {
        return false;
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
