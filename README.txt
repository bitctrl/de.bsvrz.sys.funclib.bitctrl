********************************************************************************
*                        Allgemeine Systemfunktionen                           *
********************************************************************************

Version: ${version}

�bersicht
=========

Dieses Modul enth�lt allgemeine Funktionen die von mehreren SWE genutzt werden
oder potentiell genutzt werden k�nnen.


Versionsgeschichte
==================

1.3.1
=====
�bernahme der �nderungen der Firma Kappich im Rahmen der DuA-�berarbeitung
DuaKonstanten:
- neue Konstante f�r ATG "atg.messQuerschnittVirtuell"

AbstraktVerwaltungsAdapter:
- Betriebsmeldung beim Fehlschlagen der Initialisierung entfernt

MessQuerschnittVirtuell:
- Anteile des VMQ als eigene Klasse "MessQuerschnittAnteile" ausgelagert
- Debug-Level im Konstruktor auf FINE gesetzt, wenn die ATG "virtuellStandard" nicht versorgt ist
- Funktionsnamen f�r die ermittlung der MessQuerschnittAnteile angepasst

AtgMessQuerschnittVirtuell erg�nzt
Schnittstelle MessQuerschnittAnteile erg�nzt

AtgMessQuerschnittVirtuellVLage
- erweitert die neue Schnittstelle MessQuerschnittAnteile
- Ist der Messquerschnitt von dem die Geschwindigkeit uebernommen werden soll nicht explizit versorgt wird nicht mehr der erste aus der Liste der Anteile genommen
- getMessQuerschnittGeschwindigkeit liefert gegebenenfalls null und nicht den erstbesten MQ

DUAUmfeldDatenSensor
- verwendet eine IndentityHashMap f�r die Verwaltung der Instanzen

UmfeldDatenArt
- hashCode-Funktion erg�nzt

1.3.0
=====
- Unterst�tzung f�r die Umfelddatenarten ZeitreserveGl�tte (Vaisala) und Taustoffmenge je Quadratmeter (TLS2012)
- Neues Flag -fehlerhafteWertePublizieren, mit dem das Defaultverhalten, implausible Werte
  durch 'fehlerhaft' zu ersetzen, �berschrieben werden kann.

1.2.7
=====
- nicht verwendete Klasse "SortierteListe" wegen Java 8 - Kompatibilit�tsproblem 
  entfernt

1.2.4
=====
- AbstraktAusfallUeberwachung ConcurrentModificationException korrigiert


1.2.3
=====
- Umstellung auf Maven-Build
- neue Exception UmfeldDatenSensorUnbekannteDatenartException f�r unbekannte Umfelddatensensoren 

2014-06-12

  - dua.ufd: Bei unbekannten Umfelddatenarten wird neue Exception 
    'UmfeldDatenSensorUnbekannteDatenartException' ausgel�st und in der UmfeldDatenMessStelle 
    derart behandelt, dass eine Warnung ausgegeben und der betroffene Sensor ignoriert wird 


2012-08-16

  - Suche nach dem vor einer Situation liegenden MQ wird bei Erkennung einer Schleife abgebrochen
    und per Exception gemeldet.

2012-08-15

  - Suche nach dem vor einer Situation liegenden MQ wird bei Erkennung einer Schleife abgebrochen.

2011-10-24

  - Senden von reinen Betriebsmeldungen in DUA um die Umsetzung von Objekt-PID/ID nach
    Betriebsmeldungs-ID erweitert.

2011-10-19

  - Betriebsmeldungsdaten liefern in Settern das Objekt selbst zur�ck, um die Initialisierung
    zu erleichtern

2011-10-04

  - LogTools um Funktionen zur Uebergabe des Systemobjekts und der ID in Betriebsmeldungen
    erweitert

2011-05-02

  - FIX: Beim Einsatz der neuen DatenaufbereitungLVE ist uns aufgefallen, dass wenn
    virtuelle Messquerschnitte auf Basis der atg.messQuerschnittVirtuellStandard berechnet
    werden sollen, die Applikation sich immer noch mit einem Fehler beendet. Dies war ein Fehler
    innerhalb der Funktionsbibliothek

2011-01-17

  - FIX: Negative Intervalllaengen bei PL-Logisch LVE-Ausfallhaeufigkeit
    fuehren nicht mehr zum Abbruch

2010-10-11

  - FIX: Umrechnung ASB-Stationierung in SegmentUndOffset korrigiert
  - PdSituationseigenschaften: L�nge wird mit gesetzt


2010-06-09

  - FIX: S�mtliche Konstruktoren DataDescription(atg, asp, sim) ersetzt durch
         DataDescription(atg, asp)

2009-04-28

  - NEU: boolean LogTools.isLogbar(Debug, Level) pr�ft ob ein Logger auf einem
    bestimmten Level Ausgaben auf einem beliebigen Handler macht.

  - UPDATE: void LogTools.log(Debug, LogNachricht, Object...) baut Meldungen
    erst dann zusammen (Nachrichtentext und Parameter), wenn sie tats�chlich
    ben�tigt werden. Verwendet LogTools.isLogbar(Debug, Level).


2010-05-20
  - UPDATE: Netzreferenzen erweitert


Bemerkungen
===========

Das Modul stellt eine Softwarebibliothek dar. Die JAR-Datei muss zur Benutzung
lediglich im Klassenpfad der Anwendung aufgenommen werden. Die Beschreibung der
Schnittstelle kann in der API-Dokumentation nachgelesen werden.


Disclaimer
==========

DAV-Funktionsbibliothek
Copyright (C) 2007 BitCtrl Systems GmbH

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the Free Software Foundation, Inc., 51
Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.


Kontakt
=======

BitCtrl Systems GmbH
Wei�enfelser Stra�e 67
04229 Leipzig
Phone: +49 341-490670
mailto: info@bitctrl.de
