package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class LazySecondOctet. Абстракция второго октета.
 * Работает в "ленивом" режиме инициализации. Вместо одного массива
 * со значениями следующего октета хранит 4 массива, которые инициализируются по мере надобности.
 * Хранит в массивах ссылки на следующий Octet.
 *
 * @author Kuzenkov Pavel.
 * @since 02.12.2019
 */
class LazySecondOctet implements Octet {

    /**
     * Блоки "ленивой" инициализации. Содержат значения следующего октета.
     * Инициализируются массивами по 64 ячейки каждый, вместо одного массива на 256 ячеек.
     */
    private Octet[] nextOctetsBlock1 = null;
    private Octet[] nextOctetsBlock2 = null;
    private Octet[] nextOctetsBlock3 = null;
    private Octet[] nextOctetsBlock4 = null;

    /**
     * Индикатор заполненности массива со следующими октетами.
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
     * Проверка данного октета на заполненность.
     * @return true если данный и следующий(если он есть) октет заполнены(содержит все возможные варианты),
     * false если октет ещё не заполнен.
     */
    @Override
    public boolean isFull() {
        return full;
    }

    /**
     * Добавление нового значения октета. Учет заполненности октета. Сначала находим блок "ленивой"
     * инициализации, куда нужно записать новое значение октета. Делаем это путем деления нацело
     * значения этого октета на 64 (количестко ячеек в каждом блоке), Проверяем, не заполнен ли уже
     * этот блок. Затем находим номер ячейки в этом блоке, куда необходимо записать новое значение.
     * Делаем это путём нахождения остатка от деления значения октета на 64. Если ячейка не занята - создаём
     * новый Octet и передаём ему массив с адресом. Если занята - проверяем не заполнен ли следующий
     * октет и передаем массив с адресом. После этого проверяем, не заполнился ли октет после
     * добавления в него новой записи. Если заполнился - увеличиваем счетчик соответствующего блока.
     * Если счетчик равен 64(блок заполнился) выставляем соотвествующий флаг и проверяем и удаляем
     * (схлопываем) блок. Все следующие попытки записи в этот блок будут пресекаться. Если заполнены
     * все 4 блока - выставляем флаг заполненности у всего октета.
     *
     * @param address массив со значениями октетов.
     * @return true если адрес уникален(в хранилище ещё нет такого адреса) и был сохран в хранилище,
     * false если хранилище уже содержит такой адрес.
     */
     // Возможно код не совсем понятен при чтении, из-за обилия "хардкода". Не стал выводить в
     // отдельные методы и заводить переменные т.к. экземпляров этого класса может создаться очень
     // много.
    @Override
    public boolean addNextOctet(int[] address) {
        if (blocksFull[address[1] / 64]) return false; //Если блок уже был заполнен - сразу выходим. Такой адрес уже есть в хранилище.
        Octet[] destinationBlock = getDestinationBlock(address[1]);
        boolean result = false;
        Octet destinationOctet = destinationBlock[address[1] % 64]; //Т.к. 4 массива по 64 ячейки, чтобы узнать место в массиве назначения, нужно взять остаток от деления.
        if (destinationOctet == null) {
            return createNewOctetAndAdd(destinationBlock, address);
        }
        if (!destinationOctet.isFull()) {
            result = destinationOctet.addNextOctet(address);
            if (destinationOctet.isFull()) { //Возможно после добавления, следующий октет заполнился.
                if ((fillCounters[address[1] / 64] = ++fillCounters[address[1] / 64]) >= 64) {//В принципе можно объеденить в один if.
                    blocksFull[address[1] / 64] = true;
                    deleteNeededBlock(address[1] / 64); //В данный блок пришли все возможные значения. Начинаем освобождать память (схлопывать ветвь).
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
            return nextOctetsBlock1 != null ? nextOctetsBlock1 : (nextOctetsBlock1 = new Octet[64]);
        }
        if (octetValue >= 64 && octetValue < 128) {
            return nextOctetsBlock2 != null ? nextOctetsBlock2 : (nextOctetsBlock2 = new Octet[64]);
        }
        if (octetValue >= 128 && octetValue < 192) {
            return nextOctetsBlock3 != null ? nextOctetsBlock3 : (nextOctetsBlock3 = new Octet[64]);
        }
        return nextOctetsBlock4 != null ? nextOctetsBlock4 : (nextOctetsBlock4 = new Octet[64]); //Не стал делать ещё одну проверку потому что по идее другие значения в хранилище прийти не могут(проверка на входе хранилища)
    }

    /**
     * Инициализация нового октета и запись в него нового значения.
     * @param address Массив значений октетов IP-адреса.
     * @return true т.к. адрес уникален(новый октет) и был сохран в хранилище.
     */
    private boolean createNewOctetAndAdd(Octet[] destinationBlock, int[] address) {
        LazyThirdOctet newOctet = new LazyThirdOctet();
        destinationBlock[address[1] % 64] = newOctet;
        return newOctet.addNextOctet(address);
    }

    /**
     * Удаление заполненного блока со значениями следующего октета.
     * @param blocNumber номер блока, который необходимо удалить.
     */
    private void deleteNeededBlock(int blocNumber) {
        switch (blocNumber) {
            case 0:
                nextOctetsBlock1 = null;
                break;
            case 1:
                nextOctetsBlock2 = null;
                break;
            case 2:
                nextOctetsBlock3 = null;
                break;
            case 3:
                nextOctetsBlock4 = null;
                break;
        }
    }
}
