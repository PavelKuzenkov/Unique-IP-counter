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
     * Массив с последовательностью октетов (уникальных адресов). Данный массив является первым в последовательности.
     * По сути "корнем дерева". Список первых октетов.
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
    public boolean put(int[] incomingAddress) {
        if (!isValidAddress(incomingAddress)) {
            printInfo(incomingAddress);
            return false;
        }
        Octet destination = addresses[incomingAddress[0]];
        if (destination == null) {
            return createNewOctetAndAdd(incomingAddress);
        }
        boolean result = false;
        if (!destination.isFull()) {
            result = destination.addNextOctet(incomingAddress);
            if (destination.isFull()) {  //Возможно после добавления, следующий октет заполнился.
                if (++fillCounter >= 256) { //В принципе можно объеденить в один if.
                    full = true;
                    addresses = null; //В данный октет пришли все возможные значения. Начинаем освобождать память (схлопывать ветвь).
                }
            }
        }
        return result;
    }

    /**
     * Инициализация нового октета и запись в него нового значения.
     * @param incomingAddress Массив значений октетов IP-адреса.
     * @return true т.к. адрес уникален(новый октет) и был сохран в хранилище.
     */
    private boolean createNewOctetAndAdd(int[] incomingAddress) {
        SecondOctet newOctet = new SecondOctet();
        addresses[incomingAddress[0]] = newOctet;
        return newOctet.addNextOctet(incomingAddress);
    }

    /**
     * Проверка входящего массива с IP-адресом на валидность. По идее данный метод должен быть тут, так как
     * хранилище должно само проверять входные данные на валидность. Хотя, мы теряем немного в производительности.
     * @param incomingAddress Массив значений октетов IP-адреса.
     * @return true если адрес валидный, false если адрес не валидный.
     */
    private boolean isValidAddress(int[] incomingAddress) {
        if (incomingAddress.length != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (incomingAddress[i] < 0 || incomingAddress[i] > 255) {
                return false;
            }
        }
        return true;
    }

    /**
     * вывод в консоль сообщения о поступлении невалидного адреса.
     * @param incomingAddress Массив значений октетов IP-адреса.
     */
    private void printInfo(int[] incomingAddress) {
        StringBuilder message = new StringBuilder();
        for (int address : incomingAddress) {
            message.append(address).append(".");
        }
        message.append(" - невалидный IP-адрес!");
        System.out.println(message);
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
