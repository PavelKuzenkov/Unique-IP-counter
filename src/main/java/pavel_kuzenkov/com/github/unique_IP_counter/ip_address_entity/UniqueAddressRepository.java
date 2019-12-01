package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class UniqueAddressRepository. Хранилище уникальных IP-адресов.
 * Работает в обычном режиме обычной инициализаци. Представляет собой четырёхуровневое
 * "дерево". По одному уровню на каждый октет IP-адреса. Есть массив на 256 ячеек, по одной ячейке
 * для каждого из возможных значений актета. Каждая ячейка предназначена для хранения следующего октета,
 * в котором в свою очередь есть аналогичный массив. Данная иерархия создана для ликвидации дубликатов значений октетов,
 * и для высокой скорости "итерации" по записям. По сути, значением первого октета адреса являтся
 * номер ячейки массива addresses (от 0 до 255), в которой хранится ссылка на октет. Значением второго октета - номер ячейки
 * массива в самом октете и т.д. до последнего октета. Предпологается, что когда в хранилище будут присутствовать
 * все возможные адреса для какой либо подсети(например все хосты сети 192.168.1.0/24) - то соответствующие ветви дерева
 * "схлопнуться".
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */

class UniqueAddressRepository implements AddressRepository{

    /**
     * Массив с уникальными адресами.
     */
    private Octet[] addresses = new Octet[256];

    /**
     * Индикатор заполненности хранилища.
     */
    private boolean full = false;

    /**
     * Счетчик заполненности массива с уникальными адресами.
     */
    private int fillCounter = 0;

//    /**
//     * Счетчик уникальных адресов.
//     */
//    private long uniqueAddressCounter = 0;

    /**
     * Сохранение нового уникального IP-адреса в хранилище.
     * @param incomingAddress Массив значений октетов IP-адреса.
     * @return true если адрес уникален(в хранилище ещё нет такого адреса) и был сохран в хранилище,
     * false если хранилище уже содержит такой адрес.
     */
    @Override
    public boolean put(short[] incomingAddress) {
        Octet destination = addresses[incomingAddress[0]];
        if (destination == null) {
            return createNewOctetAndPut(incomingAddress);
        }
        if (destination.isFull()) return false;
        return destination.addNextOctet(incomingAddress, 1);
    }

    /**
     * Инициализация нового октета и запись в него нового значения.
     * @param incomingAddress Массив значений октетов IP-адреса.
     * @return true т.к. адрес уникален(новый октет) и был сохран в хранилище.
     */
    private boolean createNewOctetAndPut(short[] incomingAddress) {
        NotLastOctet newOctet = new NotLastOctet();
        addresses[incomingAddress[0]] = newOctet;
        return newOctet.addNextOctet(incomingAddress, 1);
    }


    @Override
    public boolean isFull() {
        return full;
    }

//    @Override
//    public long getNumberOfUniqueAddresses() {
//        return uniqueAddressCounter;
//    }
}
