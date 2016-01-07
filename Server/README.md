## Server

### Opis:

Tworzenie i uruchomienie serwera WWW - REST API.
</br>
Domyślny adres: <b>localhost:8080</b>
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
Apache Maven 3.3.3
</br>
</br>

#### Pakiety dla Ubuntu:

`sudo apt-get install openjdk-8-jdk openjdk-8-jde maven`
</br>
</br>

### Uruchomienie:

Uruchomienie klienta można wykonać na 2 sposoby:
a. Uruchomienie skryptu <b>run.sh</b> poleceniem `./run.sh` (w razie problemów z uprawnieniami należy wpisać komendę `chmod u+x run.sh`)
b. Wpisanie polecenia `mvn jetty:run`
</br>
</br>

### Uwagi:

Upewnij się że usługa <b>mysql</b> jest uruchomiona.
</br>
Upewnij się że baza danych została poprawnie utworzona.
</br>
</br>
