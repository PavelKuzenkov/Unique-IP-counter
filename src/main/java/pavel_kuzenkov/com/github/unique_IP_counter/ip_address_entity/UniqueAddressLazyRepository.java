package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class UniqueAddressRepository. Хранилище уникальных IP-адресов.
 * Работает в "ленивом" режиме инициализаци. Вместо одного массива
 * со значениями адресов октета хранит 4 массива, которые инициализируются по мере надобности.
 * Представляет собой четырёхуровневое "дерево". По одному уровню на каждый октет IP-адреса.
 * Есть 4 массива, в общей сложности, на 256 ячеек, по одной ячейке для каждого из возможных значений актета.
 * Каждая ячейка предназначена для хранения следующего октета, в котором в свою очередь есть аналогичный массив.
 * Данная иерархия создана для ликвидации дубликатов значений октетов, и для высокой скорости "итерации"
 * по записям. По сути, значением первого октета адреса являтся номер ячейки одного из массивов addressesBlock,
 * в которой хранится ссылка на октет. Значением второго октета - номер ячейки массива в самом октете и т.д.
 * до последнего октета. Предпологается, что когда в хранилище будут присутствовать
 * все возможные адреса для какой либо подсети(например все хосты сети 192.168.1.0/24) - то соответствующие ветви дерева
 * "схлопнуться". Входящие адреса проверяются на валидность. Если адрес не валиден - в консоль пишется сообщение.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */

class UniqueAddressLazyRepository implements AddressRepository {

    /**
     * Блоки "ленивой" инициализации. Содержат последовательности октетов (уникальных адресов).
     * Данные массивы являются первыми в последовательности. Фактически являются "корнем дерева".
     * Списки первых октетов.
     * Инициализируются массивами по 64 ячейки каждый, вместо одного массива на 256 ячеек.
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
    private boolean[] blocksFull = new boolean[4];

    /**
     * Счетчики заполненности блоков "ленивой" инициализации.
     */
    private byte[] fillCounters = new byte[4]; //По идее так должно уходить чуть меньше памяти, чем с четыремя отдельными переменными. Даже не смотря на накладные расходы на массив.

    /**
     * Счетчик уникальных IP-адресов попавших в хранилище.
     */
    private long uniqueAddressCounter = 0;

    /**
     * Проверка хранилища на заполненность.
     * @return true если все 4 блока "ленивой" инициализации заполнены,
     * т.е. хранилище заполнено (содержит все возможные варианты IP-адресов),
     * false если хранилище ещё не заполнено.
     */
    @Override
    public boolean isFull() {
        return full;
    }

    /**
     * Проверка количества уникальных IP-адресов в хранилище.
     * @return количество уникальных IP-адресов.
     */
    @Override
    public long getNumberOfUniqueAddresses() {
        return uniqueAddressCounter;
    }

    /**
     * Добавление нового адреса в хранилище. Учет заполненности хранилища. Сначала находим блок "ленивой"
     * инициализации, куда нужно записать новыйадрес. Делаем это путем деления нацело
     * значения первого октета на 64 (количестко ячеек в каждом блоке), Проверяем, не заполнен ли уже
     * этот блок. Затем находим номер ячейки в этом блоке, куда необходимо записать новое значение.
     * Делаем это нахождением остатка от деления значения первого октета на 64. Если ячейка не занята - создаём
     * новый Octet и передаём ему массив с адресом. Если занята - проверяем не заполнен ли следующий
     * октет и передаем массив с адресом. После этого проверяем, не заполнился ли октет после
     * добавления в него новой записи. Если заполнился - увеличиваем счетчик соответствующего блока.
     * Если счетчик равен 64(блок заполнился) выставляем соотвествующий флаг и проверяем и удаляем
     * (схлопываем) блок. Все следующие попытки записи в этот блок будут пресекаться. Если заполнены
     * все 4 блока - выставляем флаг заполненности у всего хранилища.
     *
     * @param incomingAddress массив со значениями октетов входящего адреса.
     * @return true если адрес уникален(в хранилище ещё нет такого адреса) и был сохран в хранилище,
     * false если хранилище уже содержит такой адрес.
     */
    // Возможно код не совсем понятен при чтении, из-за обилия "хардкода". Не стал выводить в
    // отдельные методы и заводить переменные т.к. экземпляров этого класса может создаться очень
    // много.
    @Override
    public boolean put(int[] incomingAddress) {
        if (!isValidAddress(incomingAddress)) {
            printInfo(incomingAddress);
            return false;
        }
        if (blocksFull[incomingAddress[0] / 64]) return false; //Если блок уже был заполнен - сразу выходим. Такой адрес уже есть в хранилище.
        Octet[] destinationBlock = getDestinationBlock(incomingAddress[0]);
        boolean result = false;
        Octet destinationOctet = destinationBlock[incomingAddress[0] % 64]; //Т.к. 4 массива по 64 ячейки, чтобы узнать место в массиве назначения, нужно взять остаток от деления.
        if (destinationOctet == null) {
            return createNewOctetAndAdd(destinationBlock, incomingAddress);
        }
        if (!destinationOctet.isFull()) {
            result = destinationOctet.addNextOctet(incomingAddress);
            if (destinationOctet.isFull()) { //Возможно после добавления, следующий октет заполнился.
                if ((fillCounters[incomingAddress[0] / 64] = ++fillCounters[incomingAddress[0] / 64]) >= 64) {//В принципе можно объеденить в один if.
                    blocksFull[incomingAddress[0] / 64] = true;
                    deleteNeededBlock(incomingAddress[0] / 64); //В данный блок пришли все возможные значения. Начинаем освобождать память (схлопывать ветвь).
                    full = (blocksFull[0] && blocksFull[1] && blocksFull[2] && blocksFull[3]); //Если заполнены все 4 блока - значит в октет пришли все возможные значения.
                }
            }
        }
        return result;
    }

    /**
     * Определяем, с каким блоком "ленивой" инициализации предстоит работать.
     * Если нужный блок еще не инициализирован - он проинициализируется.
     * Метод не проверяет, был ли блок заполнен, поэтому вызывается только после проверки на заполненность.
     * @param octetValue Значение октета.
     * @return нужный нам блок.
     */
    private Octet[] getDestinationBlock(int octetValue) {
        if (octetValue >= 0 && octetValue < 64) {
            return addressesBlock1 != null ? addressesBlock1 : (addressesBlock1 = new Octet[64]);
        }
        if (octetValue >= 64 && octetValue < 128) {
            return addressesBlock2 != null ? addressesBlock2 : (addressesBlock2 = new Octet[64]);
        }
        if (octetValue >= 128 && octetValue < 192) {
            return addressesBlock3 != null ? addressesBlock3 : (addressesBlock3 = new Octet[64]);
        }
        return addressesBlock4 != null ? addressesBlock4 : (addressesBlock4 = new Octet[64]); //Не стал делать ещё одну проверку потому что по идее другие значения в хранилище прийти не могут(проверка на входе хранилища)
    }

    /**
     * Инициализация нового октета и запись в него нового значения.
     * @param address Массив значений октетов IP-адреса.
     * @return true т.к. адрес уникален(новый октет) и был сохран в хранилище.
     */
    private boolean createNewOctetAndAdd(Octet[] destinationBlock, int[] address) {
        LazySecondOctet newOctet = new LazySecondOctet();
        destinationBlock[address[1] % 64] = newOctet;
        return newOctet.addNextOctet(address);
    }

    /**
     * Удаление заполненного блока со значениями первого октета.
     * @param blocNumber номер блока, который необходимо удалить.
     */
    private void deleteNeededBlock(int blocNumber) {
        switch (blocNumber) {
            case 0:
                addressesBlock1 = null;
                break;
            case 1:
                addressesBlock2 = null;
                break;
            case 2:
                addressesBlock3 = null;
                break;
            case 3:
                addressesBlock4 = null;
                break;
        }
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
}
