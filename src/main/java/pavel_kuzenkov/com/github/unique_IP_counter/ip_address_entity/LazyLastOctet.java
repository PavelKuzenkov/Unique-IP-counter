package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class LazyLastOctet. Абстракция последнего октета.
 * Работает в "ленивом" режиме инициализации.  Для экономии памяти,
 * хранит в массивах не ссылки на следующий Octet, а примитивы boolean.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */

class LazyLastOctet implements Octet {

    /**
     * Блоки "ленивой" инициализации. Содержат значения последнего октета.
     */
    private boolean[] lastOctetsBlock1 = new boolean[64];
    private boolean[] lastOctetsBlock2 = new boolean[64];
    private boolean[] lastOctetsBlock3 = new boolean[64];
    private boolean[] lastOctetsBlock4 = new boolean[64];

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
    public boolean addNewOctet(short[] address, short octetNumber) {
        return false;
    }
}
