# Unique-IP-counter
Данная небольшая программка решает нижеизложенную задачу. Предпологается, что будет 2 варианта алгоритма, с небольшими 
отличиями в "работе с памятью". 

###Задача:
Дан простой текстовый файл с IPv4 адресами. Одна строка – один адрес, примерно так:

```
145.67.23.4
8.34.5.23
89.54.3.124
89.54.3.124
3.45.71.5
...
```
Файл в размере не ограничен и может занимать десятки и сотни гигабайт.

Необходимо посчитать количество уникальных адресов в этом файле, затратив как можно меньше памяти и времени. 
Существует "наивный" алгоритм решения данной задачи (читаем строка за строкой, кладем строки в HashSet), 
желательно чтобы ваша реализация была лучше этого простого, наивного алгоритма.

### Что имеем?
На первый взгляд, задача не очень сложная. Нам нужно завести некий "список". Далее читать из файла строки 
по одной, и сверяться со "списком". Если адреса в списке ещё нет - то добавлять адрес в список. А когда файл 
закончится - просто посчитать в нашем "списке" количество записей. Это и будет ответ. Проблемы начинаются от 
того, что количество уникальных IPv4-адресов равно 256 х 256 х 256 х 256 = 4.294.967.296 штук. Так как 
1 IPv4-адрес это 4 байта, то просто набор байт всех IP-адресов, без точек-разделителей, переносов каретки и пр.
будет занимать 4.294.967.296 х 4 = 17.179.869.184 байта, т.е. 16 гигабайт. А при условии, что файл на входе 
может быть сотни гигабайт - то есть ненулевая вероятность того, что в нём будут все возможные IP-адреса.
И нам, в нашем "списке" для уникальных адресов, в итоге придётся хранить их все. Плюс ко всему, по этому 
"списку" каждый раз нужно будет как-то итерироваться, дабы узнать, есть ли уже там адрес или нет.
