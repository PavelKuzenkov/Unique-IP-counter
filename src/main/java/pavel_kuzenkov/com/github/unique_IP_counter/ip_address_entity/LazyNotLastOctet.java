package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class LazyLastOctet. Абстракция непоследнего октета.
 * Работает в "ленивом" режиме инициализации. Вместо одного массива
 * со значениями следующего октета хранит 4 массива, которые инициализируются по мере надобности.
 * Хранит в массивах ссылки на следующий Octet.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */
class LazyNotLastOctet implements Octet {

    /**
     * Блоки "ленивой" инициализации. Содержат значения следующего октета.
     */
    private Octet[] OctetsBlock1 = null;
    private Octet[] OctetsBlock2 = null;
    private Octet[] OctetsBlock3 = null;
    private Octet[] OctetsBlock4 = null;

    /**
     * Индикатор заполненности массива со следующими октетами.
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
        return full;
    }

    @Override
    public boolean addNewOctet(short[] address, short octetNumber) {
        return false;
    }

    /**
     * Инициализация нужного блока со значениями следующего октета.
     * @param blocNumber номер блока, который необходимо инициализировать.
     */
    private void initiateOctetsBlock(int blocNumber) {
        switch (blocNumber) {
            case 1:
                OctetsBlock1 = new Octet[64];
                break;
            case 2:
                OctetsBlock2 = new Octet[64];
                break;
            case 3:
                OctetsBlock3 = new Octet[64];
                break;
            case 4:
                OctetsBlock4 = new Octet[64];
                break;
        }
    }
}
