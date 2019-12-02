/**
 * Пакет содержит классы и интерфейсы, для реализации компонентов IP-адресов и хранилище уникальных IP-адресов.
 * Абстракция IP-адреса представляет собой цепочку из классов, реализующих интерфейс Octet. Каждый октет содержит
 * массив со ссылками на следующие октеты, причем значением этого следующего октета является номер ячейки в массиве.
 * Для экономии памяти, последний (четвертый) октет в цепочке содержит массив не со ссылками на экземпляр класса,
 * а примитив boolean. Так же было решено реализовать для второго и третьего октета (значения первого октета
 * содержатся в массиве самого хранилища UniqueAddressRepository и UniqueAddressLazyRepository) отдельные классы
 * SecondOctet и ThirdOctet (первоначально предпологалось, что будет всего 2 реализации интерфейса Octet, для последнего
 * октета и для всех остальных). Да, возможно такая реализация не совсем приглядная, но таким образом запись нового
 * адреса в хранилище будет происходить немного быстрее, т.к. октету не нужно будет при новой записи каждый раз проверять
 * предпоследний он или нет, не будет лишнего if else и флагов. Каждый октет изначально будет знать, какой он по счёту,
 * и какую ячейку входящего массива должен обрабатывать(появляется немного "хардкода" и дублирование кода). Экономиться и время и
 * немного памяти.
 *
 * @author Kuzenkov Pavel.
 * @since 01.12.2019
 */
package pavel_kuzenkov.com.github.unique_IP_counter.ip_address_entity;