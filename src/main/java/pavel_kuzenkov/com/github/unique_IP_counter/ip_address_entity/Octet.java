package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * interface Octet. Описывает поведение одного из блоков IP-адреса (октет).
 * Значение текущего октета определяется номером ячейки массива предыдущего октета.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */
interface Octet {

    /**
     * Проверка данного октета на заполненность.
     * @return true если данный и следующий(если он есть) октет заполнены(содержит все возможные варианты),
     * false если октет ещё не заполнен.
     */
    boolean isFull();

    /**
     * Добавление нового значения октета.
     * @param address массив со значениями октетов.
     * @return true если адрес уникален(в хранилище ещё нет такого адреса) и был сохран в хранилище,
     * false если хранилище уже содержит такой адрес.
     */
    boolean addNextOctet(int[] address);

}
