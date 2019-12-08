package pavel_kuzenkov.com.github.unique_IP_counter.menu;

import pavel_kuzenkov.com.github.unique_IP_counter.agregator.Aggregator;

interface UserAction {

    int key();

    void execute(Input input, Aggregator aggregator);

    String info();

}
