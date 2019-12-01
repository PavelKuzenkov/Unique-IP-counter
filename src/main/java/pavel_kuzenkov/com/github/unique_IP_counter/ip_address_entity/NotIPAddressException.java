package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

/**
 * Class NotIPAddressException. Выбрасывается AddressRepository
 * в случае если на вход подаётся строка не содержащая IP-адрес.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */

public class NotIPAddressException extends RuntimeException {

    /**
     * Принимаем сообщение об ошибке в конструктор и передаем
     * его в конструктор родителя.
     * @param msg Сообщение об ошибке.
     */
    public NotIPAddressException(String msg) {
        super(msg);
    }
}
