# Verde S.r.l. - Progetto Basi di Dati

<p align="center">
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante">
    <img src="https://sonarcloud.io/images/project_badges/sonarcloud-light.svg" alt="SonarQube Cloud" height="42">
  </a>
  &nbsp;&nbsp;
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante">
    <img src="https://sonarcloud.io/api/project_badges/quality_gate?project=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante" alt="Quality Gate" height="42">
  </a>
</p>

<p align="center">
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante&metric=ncloc" alt="Lines of Code" height="20">
  </a>
  &nbsp;
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante&metric=reliability_rating" alt="Reliability Rating" height="20">
  </a>
  &nbsp;
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante&metric=security_rating" alt="Security Rating" height="20">
  </a>
  &nbsp;
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=montigabriele_Sistema-per-la-gestione-della-vendita-all-ingrosso-di-piante&metric=sqale_rating" alt="Maintainability Rating" height="20">
  </a>
</p>

---

## Sistema per la gestione della vendita all’ingrosso di piante

**Verde S.r.l.** è un'applicazione Java sviluppata per la gestione della vendita all'ingrosso di piante.

Il sistema permette di gestire le principali attività commerciali e logistiche dell'azienda, tra cui:

- specie di piante e catalogo;
- storico dei prezzi;
- aziende rivenditrici;
- ordini di vendita;
- fornitori;
- ordini di rifornimento;
- magazzino e giacenze;
- utenti e ruoli applicativi.

L'applicazione utilizza un'interfaccia **CLI** e persiste i dati su un database **MySQL** tramite **JDBC** e **stored procedure**.

---

## Requisiti

- **Java JDK 21** o superiore
- **Apache Maven**
- **MySQL 8.0+**
- **MySQL Connector/J**

Le dipendenze Java sono gestite tramite Maven.

---

## Tecnologie utilizzate

- Java 21
- Maven
- MySQL
- JDBC
- Stored Procedure
- Trigger
- SonarCloud

---

## Ruoli applicativi

Il sistema prevede tre ruoli principali.

### Amministratore

L'amministratore gestisce gli utenti applicativi.

Funzionalità principali:

- inserimento utenti;
- eliminazione utenti;
- visualizzazione utenti;
- verifica degli username esistenti.

---

### Responsabile Commerciale

Il responsabile commerciale gestisce le funzionalità relative alla vendita.

Funzionalità principali:

- gestione delle specie di piante;
- consultazione del catalogo;
- gestione dello storico dei prezzi;
- gestione delle aziende rivenditrici;
- creazione e modifica degli ordini di vendita;
- ricerca degli ordini;
- cambio dello stato degli ordini;
- consultazione delle disponibilità di magazzino;
- report relativi alle vendite.

---

### Responsabile Logistico

Il responsabile logistico gestisce approvvigionamento e magazzino.

Funzionalità principali:

- gestione dei fornitori;
- associazione tra fornitori e specie;
- gestione degli ordini di rifornimento;
- ricerca e modifica degli ordini;
- cambio dello stato degli ordini;
- registrazione della consegna;
- gestione delle giacenze;
- visualizzazione delle scorte critiche.

---

## Architettura

Il progetto utilizza una struttura a layer basata su **BCE**, **MVC**, **GRASP**, **SOLID** e pattern GoF.