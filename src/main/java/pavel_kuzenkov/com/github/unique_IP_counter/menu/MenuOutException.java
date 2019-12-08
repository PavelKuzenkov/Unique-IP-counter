package pavel_kuzenkov.com.github.unique_IP_counter.menu;

/**
 * Class MenuOutException.
 *
 * @author Кузенков Павел
 * @since 26.07.2018
 */
class MenuOutException extends RuntimeException {

    /**
     * Принимаем сообщение об ошибке в конструктор и передаем
     * его в конструктор родителя.
     * @param msg Сообщение об ошибке.
     */
    public MenuOutException(String msg) {
        super(msg);
    }
}
