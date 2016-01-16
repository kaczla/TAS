## Client

### Opis:

Tworzenie i uruchomienie aplikacji klienckiej.
</br>
Domyślny adres: <b>localhost:8081</b>
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
Vaadin Framework 7.5.9
</br>
</br>

#### Pakiety dla Ubuntu:

`sudo apt-get install openjdk-8-jdk openjdk-8-jde maven`
</br>
</br>

### Uruchomienie:

Uruchomienie serwera można wykonać na 2 sposoby:
</br>
a. Uruchomienie skryptu <b>run.sh</b> poleceniem `./run.sh` (w razie problemów z uprawnieniami należy wpisać komendę `chmod u+x run.sh`)
</br>
b. Wpisanie poleceń `mvn install` oraz `mvn jetty:run`
</br>
</br>

### Uwagi:

Upewnij się że usługa <b>mysql</b> jest uruchomiona.
</br>
Upewnij się że <b>Serwer WWW</b> jest uruchomiony.
</br>
Budowanie całej aplikacji klienckiej może zająć kilka minut.
</br>
Aby zmienić <b>adres klienta</b> należy edytować plik <b>pom.xml</b> w polach <b>host</b> oraz <b>port</b> w <b>build/plugin/configuration/httpConnector</b> ([pom.xml](pom.xml))
</br>
Aby zmienić <b>adres docelowy serwera</b> należy edytować pole <b>serverURL</b> w klasie <b>BaseInformation</b> ([BaseInformation.java](src/main/java/tasslegro/base/BaseInformation.java)).
</br>
</br>
