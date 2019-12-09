package pavel_kuzenkov.com.github.unique_IP_counter.menu;

import java.util.Scanner;

/**
 * Class ConsoleInput используется для ввода пользовательских данных из консоли.
 *
 * @author Кузенков Павел
 * @since 25.07.2018
 */
class ConsoleInput implements Input {

    private Scanner input = new Scanner(System.in);

    /**
     * Работа с консолью.
     * @param question вопрос от программы.
     * @return ввод пользователя в консоль.
     */
    public String ask(String question) {
        System.out.println(question);
        return this.input.nextLine();
    }

    /**
     * Работа с консолью. Выбор пунктов меню.
     * @param question вопрос от программы.
     * @param range диапозон допустимых значений команд.
     * @return выбраный пункт меню.
     */
    public int ask(String question, int[] range) {
        System.out.print(question);
        int key = Integer.parseInt(this.input.nextLine());
        boolean exist = false;
        for (int value : range) {
            if (value == key) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            throw new MenuOutException("Out of range.");
        }
        return key;
    }
}
