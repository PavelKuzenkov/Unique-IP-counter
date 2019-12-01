package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;

class LazyNotLastOctet implements Octet {
    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean addNewOctet(short[] address, short octetNumber) {
        return false;
    }
}
