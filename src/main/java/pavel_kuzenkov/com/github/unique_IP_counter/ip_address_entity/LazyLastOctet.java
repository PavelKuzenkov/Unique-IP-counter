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
     * Инициалихируются массивами по 64 ячейки каждый, вместо одного массива на 256 ячеек.
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
     * Добавление нового значения октета. Учет заполненности октета.
     * @param address массив со значениями октетов.
     * @return true если адрес уникален(в хранилище ещё нет такого адреса) и был сохран в хранилище,
     * false если хранилище уже содержит такой адрес.
     */
    //Возможно код не совсем понятен при чтении, из-за обилия "хардкода". Не стал выводить в отдельные методы и заводить переменные т.к. экземпляров этого класса может создаться очень много.
    @Override
    public boolean addNextOctet(short[] address) {
        if (blocksFull[address[3] / 64]) return false;//Если блок уже был заполнен - сразу выходим. Такой адрес уже есть в хранилище.
        boolean[] destination = getDestinationBlock(address[3]);
        if (!destination[address[3] % 64]) { //Т.к. 4 массива по 64 ячейки
            destination[address[3] % 64] = true;
            if ((fillCounters[address[3] / 64] = ++fillCounters[address[3] / 64]) >= 64) {
                blocksFull[address[3] / 64] = true;
                deleteNeededBlock(address[3] / 64);//В данный блок пришли все возможные значения. Начинаем освобождать память (схлопывать ветвь).
                full = (blocksFull[0] && blocksFull[1] && blocksFull[2] && blocksFull[3]); //Если заполнены все 4 блока - значит в октет пришли все возможные значения.
            }
            return true;
        } else return false;
    }

    /**
     * Определяем, с каким блоком "ленивой" инициализации предстоит работать.
     * Если нужный блок еще не инициализирован - он проинициализируется.
     * Метод не проверяет, был ли блок заполнен, поэтому вызывается только после проверки на заполненность.
     * @param octetValue Значение октета.
     * @return нужный нам блок.
     */
    private boolean[] getDestinationBlock(short octetValue) {
        if (octetValue >= 0 && octetValue < 64) {
            return lastOctetsBlock1 != null ? lastOctetsBlock1 : (lastOctetsBlock1 = new boolean[64]);
        }
        if (octetValue >= 64 && octetValue < 128) {
            return lastOctetsBlock2 != null ? lastOctetsBlock2 : (lastOctetsBlock2 = new boolean[64]);
        }
        if (octetValue >= 128 && octetValue < 192) {
            return lastOctetsBlock3 != null ? lastOctetsBlock3 : (lastOctetsBlock3 = new boolean[64]);
        }
        return lastOctetsBlock4 != null ? lastOctetsBlock4 : (lastOctetsBlock4 = new boolean[64]); //Не стал делать ещё одну проверку потому что по идее другие значения в хранилище прийти не могут(проверка на входе хранилища)

    }

    /**
     * Удаление заполненного блока со значениями последнего октета.
     * @param blocNumber номер блока, который необходимо удалить.
     */
    private void deleteNeededBlock(int blocNumber) {
        switch (blocNumber) {
            case 0:
                lastOctetsBlock1 = null;
                break;
            case 1:
                lastOctetsBlock2 = null;
                break;
            case 2:
                lastOctetsBlock3 = null;
                break;
            case 3:
                lastOctetsBlock4 = null;
                break;
        }
    }
}
