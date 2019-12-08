package pavel_kuzenkov.com.github.unique_IP_counter.menu;

/**
 * Class BaseAction.
 *
 * @author Кузенков Павел
 * @since 26.07.2018
 */
abstract class BaseAction implements UserAction {

    /**
     * Номер действия.
     */
    private final int key;

    /**
     * Описание действия.
     */
    private final String name;

    /**
     * Конструктор класса.
     * @param key номер действия.
     * @param name описание действия.
     */
    protected BaseAction(final int key, final String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * Действие пользователя.
     * @return номер действия в меню.
     */
    @Override
    public int key() {
        return this.key;
    }

    /**
     * Описания действия.
     * @return описание действия.
     */
    @Override
    public String info() {
        return String.format("%s. %s", this.key, this.name);
    }
}
