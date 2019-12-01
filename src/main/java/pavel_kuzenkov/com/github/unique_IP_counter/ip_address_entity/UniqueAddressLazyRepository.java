package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class UniqueAddressRepository. Хранилище уникальных IP-адресов.
 * Работает в "ленивом" режиме инициализаци. Вместо одного массива
 * со значениями адресов октета хранит 4 массива, которые инициализируются по мере надобности.
 * Представляет собой четырёхуровневое "дерево". По одному уровню на каждый октет IP-адреса.
 * Есть 4 массива, в общей сложности, на 256 ячеек, по одной ячейке для каждого из возможных значений актета.
 * Каждая ячейка предназначена для хранения следующего октета, в котором в свою очередь есть аналогичный массив.
 * Данная иерархия создана для ликвидации дубликатов значений октетов, и для высокой скорости "итерации"
 * по записям. По сути, значением первого октета адреса являтся номер ячейки массива addresses (от 0 до 255),
 * в которой хранится ссылка на октет. Значением второго октета - номер ячейки массива в самом октете и т.д.
 * до последнего октета. Предпологается, что когда в хранилище будут присутствовать
 * все возможные адреса для какой либо подсети(например все хосты сети 192.168.1.0/24) - то соответствующие ветви дерева
 * "схлопнуться".
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */

class UniqueAddressLazyRepository implements AddressRepository {

    /**
     * Блоки "ленивой" инициализации. Содержат уникальные адреса
     */
    private Octet[] addressesBlock1 = null;
    private Octet[] addressesBlock2 = null;
    private Octet[] addressesBlock3 = null;
    private Octet[] addressesBlock4 = null;

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

//    /**
//     * Счетчик уникальных адресов.
//     */
//    private long uniqueAddressCounter = 0;


    @Override
    public boolean put(short[] address) {
        return false;
    }

    @Override
    public boolean isFull() {
        return full;
    }

//    @Override
//    public long getNumberOfUniqueAddresses() {
//        return uniqueAddressCounter;
//    }

    /**
     * Инициализация нужного блока с адресами.
     * @param blocNumber номер блока, который необходимо инициализировать.
     */
    private void initiateOctetsBlock(int blocNumber) {
        switch (blocNumber) {
            case 1:
                addressesBlock1 = new Octet[64];
                break;
            case 2:
                addressesBlock2 = new Octet[64];
                break;
            case 3:
                addressesBlock3 = new Octet[64];
                break;
            case 4:
                addressesBlock4 = new Octet[64];
                break;
        }
    }
}
