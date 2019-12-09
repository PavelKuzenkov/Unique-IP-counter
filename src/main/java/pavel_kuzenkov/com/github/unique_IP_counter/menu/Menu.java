package pavel_kuzenkov.com.github.unique_IP_counter.menu;

import pavel_kuzenkov.com.github.unique_IP_counter.agregator.Aggregator;
import pavel_kuzenkov.com.github.unique_IP_counter.file_reader.IPAddressFileBufferedReader;
import pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity.UniqueAddressLazyRepository;
import pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity.UniqueAddressRepository;

import java.util.ArrayList;

/**
 * Class Menu.
 *
 * @author Kuzenkov Pavel.
 * @since 26.07.2018
 */
class Menu {

    /**
     * Получение данных от пользователя.
     */
    private Input input;

    /**
     * Место управления рабочим процессом.
     */
    private Aggregator aggregator;

    /**
     * Меню.
     */
    private ArrayList<UserAction> actions = new ArrayList<>();

    /**
     * Конструтор инициализирующий поля.
     * @param input ввод данных.
     * @param aggregator Место управления рабочим процессом.
     */
    Menu(Input input, Aggregator aggregator) {
        this.input = input;
        this.aggregator = aggregator;
    }

    /**
     * Заполнение массива дейтвиями. (Выбор алгоритма)
     * @param ui
     */
    void fillActions(Start ui) {
        this.actions.clear();
        System.out.println("------------------- Unique IP counter ---------------------");
        this.actions.add(new EnterIPRepo(this.actions.size(), "Algorithm selection."));
        this.actions.add(this.actions.size(), new Exit(ui));
        int[] range = new int[this.actions.size()];
        for (int index = 0; index != range.length; index++) {
            range[index] = index;
        }
        ui.setRange(range);
    }

    /**
     * Заполнение массива дейтвиями. (файл с IP-адресами)
     */
    private void fillAfterIPRepoAction() {
        Exit exit = (Exit) Menu.this.actions.get(1);
        Menu.this.actions.clear();
        this.actions.add(new EnterPath(this.actions.size(), "Specify a file with IP addresses."));
        Menu.this.actions.add(Menu.this.actions.size(), new Exit(exit.getUi()));
        int[] range = new int[Menu.this.actions.size()];
        for (int index = 0; index != range.length; index++) {
            range[index] = index;
        }
        exit.getUi().setRange(range);
    }

    /**
     * Заполнение массива дейтвиями. После подсчёта.
     */
    private void fillAfterProcess() {
        Exit exit = (Exit) Menu.this.actions.get(1);
        Menu.this.actions.clear();
        this.actions.add(new OneMoreTime(this.actions.size(), "One more time?", exit.getUi()));
        Menu.this.actions.add(Menu.this.actions.size(), new Exit(exit.getUi()));
        int[] range = new int[Menu.this.actions.size()];
        for (int index = 0; index != range.length; index++) {
            range[index] = index;
        }
        exit.getUi().setRange(range);
    }

    /**
     * Действие введенное пользователем.
     * @param key номер действия.
     */
    void select(int key) {
        this.actions.get(key).execute(this.input, this.aggregator);
    }

    /**
     * Вывод меню в консоль.
     */
    void show() {
        for (UserAction action : this.actions) {
            if (action != null) {
                System.out.println(action.info());
            }
        }
    }

    /**
     * Внутренний класс EnterIPRepo. Отвечает за выбор типа алгоритма.
     */
    class EnterIPRepo extends BaseAction {

        /**
         * Конструктор класса.
         * @param key номер действия.
         * @param name описание действия.
         */
        EnterIPRepo(int key, String name) {
            super(key, name);
        }

        /**
         * Указываем файл.
         * @param input ввод данных.
         * @param aggregator Место управления рабочим процессом.
         */
        public void execute(Input input, Aggregator aggregator) {
            System.out.println("------------------- Select algorithm: ---------------------");
            System.out.println("0. \"Tree\" repository.");
            System.out.println("1. \"Lazy tree\" repository.");
            int repoType = input.ask("Select: ", new int[]{0, 1});
            //TODO это должно происходить не здесь. По хорошему, нужно передавать в аггрегатор какой-нибудь енам, и исходя из значения енама, аггрегатор должен компоновать себя
            if (repoType == 0) {
                aggregator.setAddressRepository(new UniqueAddressRepository());
            } else {
                aggregator.setAddressRepository(new UniqueAddressLazyRepository());
            }
            aggregator.setIPAddressFileReader(new IPAddressFileBufferedReader());//Т.к. пока только одна реализация IPAddressFileReader
            System.out.println();
            fillAfterIPRepoAction();
        }
    }

    /**
     * Внутренний класс EnterPath. Отвечает за ввод путя до файла с IP-адресами.
     */
    class EnterPath extends BaseAction {

        /**
         * Конструктор класса.
         * @param key номер действия.
         * @param name описание действия.
         */
        EnterPath(int key, String name) {
            super(key, name);
        }

        /**
         * Указываем файл.
         * @param input ввод данных.
         * @param aggregator Место управления рабочим процессом.
         */
        public void execute(Input input, Aggregator aggregator) {
            System.out.println("---------- Defining a file with IP addresses. -------------");
            String filePath = input.ask("Enter name/path of file with IP addresses.");
            aggregator.setFilePath(filePath);
            System.out.println();
            aggregator.startProcess();
            System.out.println("------------------------------------------------------------");
            fillAfterProcess();
        }
    }

    /**
     * Внутренний класс OneMoreTime. Предложение посчитать ещё раз.
     */
    class OneMoreTime extends BaseAction {

        private Start ui;

        /**
         * Конструктор класса.
         * @param key номер действия.
         * @param name описание действия.
         */
        OneMoreTime(int key, String name, Start ui) {
            super(key, name);
            this.ui = ui;
        }

        /**
         * Ещё раз?.
         * @param input ввод данных.
         * @param aggregator Место управления рабочим процессом.
         */
        public void execute(Input input, Aggregator aggregator) {
            fillActions(ui);
        }
    }

    /**
     * Внутренний класс Exit. Отвечает за выход из программы.
     */
    private class Exit implements UserAction {

        private Start ui;

        Exit(Start ui) {
            this.ui = ui;
        }

        Start getUi() {
            return ui;
        }

        /**
         * Действие пользователя.
         * @return номер действия в меню.
         */
        public int key() {
            return Menu.this.actions.size() - 1;
        }

        /**
         * Реализация выхода из программы.
         * @param input ввод данных.
         * @param aggregator хранилище заявок.
         */
        public void execute(Input input, Aggregator aggregator) {
            boolean stop = false;
            while (!stop) {
                String key = input.ask("Exit? y/n: ");
                switch (key) {
                    case "y" :
                        this.ui.exit();
                        stop = true;
                        break;
                    case "n" :
                        stop = true;
                        break;
                    default:
                        break;
                }
            }
        }

        /**
         * Вывод в консоль описания действия.
         * @return описание действия.
         */
        public String info() {
            return String.format("%s. %s", this.key(), "Exit program.");
        }

    }
}
