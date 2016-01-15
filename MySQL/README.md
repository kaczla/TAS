## MySQL

### Opis:

Tworzenie i inicjalizowanie bazy danych MySQL.
</br>
</br>

### Testowane na:

**Ubuntu 15.10**
</br>
x64
</br>
Java 1.8
</br>
MySQL 5.6.27
</br>
</br>

#### Pakiety dla Ubuntu:

`sudo apt-get install openjdk-8-jdk openjdk-8-jde mysql-server mysql-client libmysql-java`
</br>
</br>

### Uruchomienie:

Utworzenie bazy danych można wykonać na 2 sposoby:
</br>
a. Uruchomienie skryptu <b>run.sh</b> poleceniem `./run.sh root` (gdzie pierwszy argument jest nazwą użytkownika, np. <b>root</b>), kolejno wpisując hasło użytkownika (w razie problemów z uprawnieniami należy wpisać komendę `chmod u+x run.sh`)
</br>
b. Skompilować plik <b>MySQL.java</b> po czym uruchomić
</br>
</br>

### Uwagi:

Upewnij się że usługa <b>mysql</b> jest uruchomiona.
</br>
Aby zmienić <b>adres docelowy bazy danych</b> dla sposobu <b>B</b> należy edytować pole <b>ConnectionDBAddres</b> w klasie <b>MySQL</b> ([MySQL.java](src/MySQL/MySQL.java))
</br>
Aby zmienić <b>login i hasło do bazy danych</b> dla sposobu <b>B</b> należy edytować pole <b>UserName</b> i <b>UserPassword</b> w klasie <b>MySQL</b> ([MySQL.java](src/MySQL/MySQL.java))
</br>
Jeżeli występują problemy ze znakami UTF-8 to należy dodać do pliku <b>/etc/mysql/mysql.conf.d/</b>:
`[mysqld]
character-set-server=utf8
collation-server=utf8_general_ci
init-connect='SET NAMES utf8'
[client]
default-character-set=utf8`
</br>
</br>
