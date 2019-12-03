package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class ThirdOctet. Абстракция третьего октета.
 * Работает в обычном режиме инициализации. Значение текущего октета
 * определяется номером ячейки массива предыдущего октета.
 * Хранит в массиве ссылки на следующий Octet.
 *
 * @author Kuzenkov Pavel.
 * @since 02.12.2019
 */
class ThirdOctet implements Octet {

    /**
     * Массив со следующими октетами.
     */
    private Octet[] nextOctets = new Octet[256];

    /**
     * Индикатор заполненности массива со следующими октетами.
     */
    private boolean full = false;

    /**
     * Счетчик заполненности массива со следующим октетом.
     */
    private short fillCounter = 0;

    /**
     * Проверка данного октета на заполненность.
     * @return true если данный и следующий октет заполнены(содержит все возможные варианты),
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
    @Override
    public boolean addNextOctet(int[] address) {
        Octet destination = nextOctets[address[2]];
        if (destination == null) {
            return createNewOctetAndAdd(address);
        }
        boolean result = false;
        if (!destination.isFull()) {
            result = destination.addNextOctet(address);
            if (destination.isFull()) {  //Возможно после добавления, следующий октет заполнился.
                if (++fillCounter >= 256) { //В принципе можно объеденить в один if.
                    full = true;
                    nextOctets = null; //В данный октет пришли все возможные значения. Начинаем освобождать память (схлопывать ветвь).
                }
            }
        }
        return result;
    }

    /**
     * Инициализация нового октета и запись в него нового значения.
     * @param address Массив значений октетов IP-адреса.
     * @return true т.к. адрес уникален(новый октет) и был сохран в хранилище.
     */
    private boolean createNewOctetAndAdd(int[] address) {
        LastOctet newOctet = new LastOctet();
        nextOctets[address[2]] = newOctet;
        return newOctet.addNextOctet(address);
    }
}
