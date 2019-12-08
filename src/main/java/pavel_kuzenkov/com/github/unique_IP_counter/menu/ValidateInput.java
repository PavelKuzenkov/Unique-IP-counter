package pavel_kuzenkov.com.github.unique_IP_counter.menu;

/**
 * Class ValidateInput. Для проверки корректности вводимых пользователем команд.
 *
 * @author Кузенков Павел
 * @since 26.07.2018
 */
class ValidateInput implements Input {

    private final Input input;

    public ValidateInput(final Input input) {
        this.input = input;
    }

    @Override
    public String ask(String question) {
        return this.input.ask(question);
    }

    /**
     * Проверка корректности вводимых пользователем команд в меню.
     * @param question вопрос от программы.
     * @param range диапозон допустимых значений команд.
     * @return ввод пользователя в консоль.
     */
    public int ask(String question, int[] range) {
        boolean invalid = true;
        int value = -1;
        do {
            try {
                value = this.input.ask(question, range);
                invalid = false;
            } catch (MenuOutException moe) {
                System.out.println("Необходимо выбрать значение из диапазона меню.");
            } catch (NumberFormatException nfe) {
                System.out.println("Необходимо ввести корректное значение.");
            }
        } while (invalid);
        return value;
    }

//    /**
//     * Проверка корректности вводимых пользователем значений интервалов.
//     * @param question1 вопрос от программы.
//     * @param question2 вопрос от программы.
//     * @return ввод пользователя в консоль.
//     */
//    public double ask(String question1, String question2) {
//        boolean invalid = true;
//        double value = 0;
//        do {
//            try {
//                value = this.input.ask(question1, question2);
//                invalid = false;
//            } catch (NumberFormatException nfe) {
//                System.out.println("Необходимо ввести корректное значение.");
//            }
//        } while (invalid);
//        System.out.println(question1.concat(String.valueOf(value)).concat(System.lineSeparator()));
//        return value;
//    }
}
