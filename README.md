[![Build Status](https://travis-ci.org/bitctrl/de.bsvrz.sys.funclib.bitctrl.svg?branch=develop)](https://travis-ci.org/bitctrl/de.bsvrz.sys.funclib.bitctrl)
[![Build Status](https://api.bintray.com/packages/bitctrl/maven/de.bsvrz.sys.funclib.bitctrl/images/download.svg)](https://bintray.com/bitctrl/maven/de.bsvrz.sys.funclib.bitctrl)

********************************************************************************
*                        Allgemeine Systemfunktionen                           *
********************************************************************************

Version: ${version}

Übersicht
=========

Dieses Modul enthält allgemeine Funktionen die von mehreren SWE genutzt werden
oder potentiell genutzt werden können.


Versionsgeschichte
==================

1.5.2
=====
- Fehler beim Abruf von Objekten der Konfiguration, weil die ObjektFactory versucht hat, 
  Objekte von Typen abzurufen, die in der Konfiguration nicht vorhanden sind.

1.5.1
=====
- Klassenpfad im runtime-jar korrigiert

1.5.0
=====
- Umstellung auf Java 8 und UTF-8


1.5.0
=====
- Umstellung auf Java 8 und UTF-8

1.4.1
=====
WGS84Koordinaten haben keiner Setter für Länge und Breite, da ansonsten equals/hashCode
gegen den API-Kontrakt verstossen

1.4.0
=====
Da die SWE de.bsvrz.sys.funclib.bitctrl perspektivisch durch die bereits existierende 
Version 2.0 auch auf dem Server ersetzt werden soll, wurden im ersten Schritt die 
Pakete die von der SWE des Segments DuA verwendet werden in eine eigene SWE 
"de.bsvrz.sys.funclib.bitctrl.dua" ausgelagert.

Wenn die Funktionsbibliothek verwendet werden soll, müssen für die DuA-Komponenten
mindestens folgende Versionen verwendet werden, die auf die neue SWE zurückgreifen:

- SWE 4.6  Abfrage Pufferdaten (de.bsvrz.dua.abfrpuffer) in Version 1.3.0
- SWE 4.9 Aggregation LVE (de.bsvrz.dua.aggrlve) in Version 1.3.0
- SWE 4.10 Ergänzung BASt-Band (de.bsvrz.dua.bastband) in Version 1.3.0
- SWE 4.7 Datenaufbereitung LVE (de.bsvrz.dua.dalve) in Version 1.6.0
- SWE 4.8 Datenaufbereitung UFD (de.bsvrz.dua.daufd) in Version 1.4.0
- SWE 4.DeFa DE Fehleranalyse fehlende Messdaten (de.bsvrz.dua.fehlertls) in Version 1.5.0
- SWE 4.11 Güteberechnung (de.bsvrz.dua.guete) in Version 1.3.0
- SWE 4.DELzFh DE Langzeit-Fehlererkennung (de.bsvrz.dua.langfehlerlve) in Version 1.5.0
- SWE 4.5 Messwertersetzung LVE (de.bsvrz.dua.mwelve) in Version 1.4.0
- SWE 4.12 Messwertersetzung UFD (de.bsvrz.dua.mweufd) in Version 1.3.0
- SWE 4.1 Pl-Prüfung formal (de.bsvrz.dua.plformal) in Version 1.4.0
- SWE 4.13 Pl-Prüfung langzeit UFD (de.bsvrz.dua.pllangufd) in Version 1.4.0
- SWE 4.2 Pl-Prüfung logisch LVE (de.bsvrz.dua.plloglve) in Version 1.3.0
- SWE 4.3 Pl-Prüfung logisch UFD (de.bsvrz.dua.pllogufd) in Version 1.6.0
- SWE 4.14 SWE Glättewarnung und -prognose (de.bsvrz.dua.progglaette) in Version 1.3.0

1.3.0
=====
- Unterstützung für die Umfelddatenarten ZeitreserveGlätte (Vaisala) und Taustoffmenge je Quadratmeter (TLS2012)
- Neues Flag -fehlerhafteWertePublizieren, mit dem das Defaultverhalten, implausible Werte
  durch 'fehlerhaft' zu ersetzen, überschrieben werden kann.

1.2.7
=====
- nicht verwendete Klasse "SortierteListe" wegen Java 8 - Kompatibilitätsproblem 
  entfernt

1.2.4
=====
- AbstraktAusfallUeberwachung ConcurrentModificationException korrigiert


1.2.3
=====
- Umstellung auf Maven-Build
- neue Exception UmfeldDatenSensorUnbekannteDatenartException für unbekannte Umfelddatensensoren 

2014-06-12

  - dua.ufd: Bei unbekannten Umfelddatenarten wird neue Exception 
    'UmfeldDatenSensorUnbekannteDatenartException' ausgelöst und in der UmfeldDatenMessStelle 
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

  - Betriebsmeldungsdaten liefern in Settern das Objekt selbst zurück, um die Initialisierung
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
  - PdSituationseigenschaften: Länge wird mit gesetzt


2010-06-09

  - FIX: Sämtliche Konstruktoren DataDescription(atg, asp, sim) ersetzt durch
         DataDescription(atg, asp)

2009-04-28

  - NEU: boolean LogTools.isLogbar(Debug, Level) prüft ob ein Logger auf einem
    bestimmten Level Ausgaben auf einem beliebigen Handler macht.

  - UPDATE: void LogTools.log(Debug, LogNachricht, Object...) baut Meldungen
    erst dann zusammen (Nachrichtentext und Parameter), wenn sie tatsächlich
    benötigt werden. Verwendet LogTools.isLogbar(Debug, Level).


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
Weißenfelser Straße 67
04229 Leipzig
Phone: +49 341-490670
mailto: info@bitctrl.de
