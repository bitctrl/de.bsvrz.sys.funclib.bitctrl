/*
 * Segment 14 (ÜVi), SWE 14.BW-Übergangsvisualisierung 
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

public enum RdsLocationTabelle implements Zustand {
	DEUTSCHLAND_1("Deutschland(1), Tabelle 01", 1001), DEUTSCHLAND_2(
			"Deutschland(1), Tabelle 02", 1002), DEUTSCHLAND_3(
			"Deutschland(1), Tabelle 03", 1003), DEUTSCHLAND_4(
			"Deutschland(1), Tabelle 04", 1004), DEUTSCHLAND_5(
			"Deutschland(1), Tabelle 05", 1005), DEUTSCHLAND_6(
			"Deutschland(1), Tabelle 06", 1006), DEUTSCHLAND_7(
			"Deutschland(1), Tabelle 07", 1007), DEUTSCHLAND_8(
			"Deutschland(1), Tabelle 08", 1008), GRIECHENLAND_17(
			"Griechenland, Tabelle 17", 1017), GRIECHENLAND_18(
			"Griechenland, Tabelle 18", 1018), GRIECHENLAND_19(
			"Griechenland, Tabelle 19", 1019), GRIECHENLAND_20(
			"Griechenland, Tabelle 20", 1020), GRIECHENLAND_21(
			"Griechenland, Tabelle 21", 1021), GRIECHENLAND_22(
			"Griechenland, Tabelle 22", 1022), GRIECHENLAND_23(
			"Griechenland, Tabelle 23", 1023), GRIECHENLAND_24(
			"Griechenland, Tabelle 24", 1024), MAROKKO_33(
			"Marokko, Tabelle 33", 1033), MAROKKO_34("Marokko, Tabelle 34",
			001034), MAROKKO_35("Marokko, Tabelle 35", 1035), MAROKKO_36(
			"Marokko, Tabelle 36", 1036), MOLDAVIEN_51("Moldavien, Tabelle 51",
			1051), MOLDAVIEN_52("Moldavien, Tabelle 52", 1052), MONTENEGRO_59(
			"Montenegro, Tabelle 59", 1059), MONTENEGRO_60(
			"Montenegro, Tabelle 60", 1060),

	ALGERIEN_1("Algerien, Tabelle 01", 2001), ALGERIEN_2(
			"Algerien, Tabelle 02", 2002), ALGERIEN_3("Algerien, Tabelle 03",
			002003), ALGERIEN_4("Algerien, Tabelle 04", 2004), ZYPERN_17(
			"Zypern, Tabelle 17", 2017), ZYPERN_18("Zypern, Tabelle 18", 2018), TSCHECHIEN_25(
			"Tschechische Republik, Tabelle 25", 2025), TSCHECHIEN_26(
			"Tschechische Republik, Tabelle 26", 2026), TSCHECHIEN_27(
			"Tschechische Republik, Tabelle 27", 2027), TSCHECHIEN_28(
			"Tschechische Republik, Tabelle 28", 2028), IRLAND_41(
			"Irland, Tabelle 41", 2041), IRLAND_42("Irland, Tabelle 42", 002042), IRLAND_43(
			"Irland, Tabelle 43", 2043), IRLAND_44("Irland, Tabelle 44", 2044), IRLAND_45(
			"Irland, Tabelle 45", 002045), IRLAND_46("Irland, Tabelle 46", 2046), IRLAND_47(
			"Irland, Tabelle 47", 2047), IRLAND_48("Irland, Tabelle 48", 2048), ESTLAND_59(
			"Estland, Tabelle 59", 2059), ESTLAND_60("Estland, Tabelle 60",
			2060),

	ANDORRA_1("Andorra, Tabelle 01", 3001), ANDORRA_2("Andorra, Tabelle 02",
			003002), POLEN_5("Polen, Tabelle 05", 3005), POLEN_6(
			"Polen, Tabelle 06", 3006), POLEN_7("Polen, Tabelle 07", 3007), POLEN_8(
			"Polen, Tabelle 08", 3008), SAN_MARINO_21("San Marino, Tabelle 21",
			3021), SAN_MARINO_22("San Marino, Tabelle 22", 3022), TUERKEI_33(
			"Türkei, Tabelle 33", 003033), TUERKEI_34("Türkei, Tabelle 34",
			3034), TUERKEI_35("Türkei, Tabelle 35", 3035), TUERKEI_36(
			"Türkei, Tabelle 36", 003036), TUERKEI_37("Türkei, Tabelle 37",
			3037), TUERKEI_38("Türkei, Tabelle 38", 3038), TUERKEI_39(
			"Türkei, Tabelle 39", 3039), TUERKEI_40("Türkei, Tabelle 40", 3040),

	MAZEDONIEN_1("Mazedonien, Tabelle 01", 4001), MAZEDONIEN_2(
			"Mazedonien, Tabelle 02", 4002), SCHWEIZ_9("Schweiz, Tabelle 09",
			4009), SCHWEIZ_10("Schweiz, Tabelle 10", 4010), SCHWEIZ_11(
			"Schweiz, Tabelle 11", 4011), SCHWEIZ_12("Schweiz, Tabelle 12",
			004012), SCHWEIZ_13("Schweiz, Tabelle 13", 4013), SCHWEIZ_14(
			"Schweiz, Tabelle 14", 4014), SCHWEIZ_15("Schweiz, Tabelle 15",
			004015), SCHWEIZ_16("Schweiz, Tabelle 16", 4016), VATIKAN_25(
			"Vatikan, Tabelle 25", 4025), VATIKAN_26("Vatikan, Tabelle 26",
			004026), ISRAEL_33("Israel, Tabelle 33", 4033), ISRAEL_34(
			"Israel, Tabelle 34", 4034), ISRAEL_35("Israel, Tabelle 35", 004035), ISRAEL_36(
			"Israel, Tabelle 36", 4036),

	ITALIEN_1("Italien, Tabelle 01", 5001), ITALIEN_2("Italien, Tabelle 02",
			005002), ITALIEN_3("Italien, Tabelle 03", 5003), ITALIEN_4(
			"Italien, Tabelle 04", 5004), ITALIEN_5("Italien, Tabelle 05",
			005005), ITALIEN_6("Italien, Tabelle 06", 5006), ITALIEN_7(
			"Italien, Tabelle 07", 5007), ITALIEN_8("Italien, Tabelle 08", 5008), ITALIEN_9(
			"Italien, Tabelle 09", 5009), ITALIEN_10("Italien, Tabelle 10",
			5010), ITALIEN_11("Italien, Tabelle 11", 005011), ITALIEN_12(
			"Italien, Tabelle 12", 5012), ITALIEN_13("Italien, Tabelle 13",
			5013), ITALIEN_14("Italien, Tabelle 14", 005014), ITALIEN_15(
			"Italien, Tabelle 15", 5015), ITALIEN_16("Italien, Tabelle 16",
			5016), JORDANIEN_33("Jordanien, Tabelle 33", 5033), JORDANIEN_34(
			"Jordanien, Tabelle 34", 5034), JORDANIEN_35(
			"Jordanien, Tabelle 35", 5035), JORDANIEN_36(
			"Jordanien, Tabelle 36", 5036), SLOWAKEI_51("Slowakei, Tabelle 51",
			5051), SLOWAKEI_52("Slowakei, Tabelle 52", 5052), SLOWAKEI_53(
			"Slowakei, Tabelle 53", 5053), SLOWAKEI_54("Slowakei, Tabelle 54",
			5054),

	BELGIEN_1("Belgien, Tabelle 01", 6001), BELGIEN_2("Belgien, Tabelle 02",
			006002), BELGIEN_3("Belgien, Tabelle 03", 6003), BELGIEN_4(
			"Belgien, Tabelle 04", 6004), BELGIEN_5("Belgien, Tabelle 05",
			006005), BELGIEN_6("Belgien, Tabelle 06", 6006), BELGIEN_7(
			"Belgien, Tabelle 07", 6007), BELGIEN_8("Belgien, Tabelle 08", 6008), FINNLAND_17(
			"Finnland, Tabelle 17", 6017), FINNLAND_18("Finnland, Tabelle 18",
			6018), FINNLAND_19("Finnland, Tabelle 19", 6019), FINNLAND_20(
			"Finnland, Tabelle 20", 6020), FINNLAND_21("Finnland, Tabelle 21",
			6021), FINNLAND_22("Finnland, Tabelle 22", 6022), FINNLAND_23(
			"Finnland, Tabelle 23", 6023), FINNLAND_24("Finnland, Tabelle 24",
			6024), UKRAINE_33("Ukraine, Tabelle 33", 006033), UKRAINE_34(
			"Ukraine, Tabelle 34", 6034), UKRAINE_35("Ukraine, Tabelle 35",
			6035), UKRAINE_36("Ukraine, Tabelle 36", 006036), UKRAINE_37(
			"Ukraine, Tabelle 37", 6037), UKRAINE_38("Ukraine, Tabelle 38",
			6038), UKRAINE_39("Ukraine, Tabelle 39", 6039), UKRAINE_40(
			"Ukraine, Tabelle 40", 6040), SYRIEN_53("Syrien, Tabelle 53", 6053), SYRIEN_54(
			"Syrien, Tabelle 54", 006054), SYRIEN_55("Syrien, Tabelle 55", 6055), SYRIEN_56(
			"Syrien, Tabelle 56", 6056),

	LUXEMBURG_1("Luxemburg, Tabelle 01", 7001), LUXEMBURG_2(
			"Luxemburg, Tabelle 02", 7002), LUXEMBURG_3(
			"Luxemburg, Tabelle 03", 7003), LUXEMBURG_4(
			"Luxemburg, Tabelle 04", 7004), RUSSLAND_21("Russland, Tabelle 21",
			7021), RUSSLAND_22("Russland, Tabelle 22", 7022), RUSSLAND_23(
			"Russland, Tabelle 23", 7023), RUSSLAND_24("Russland, Tabelle 24",
			7024), RUSSLAND_25("Russland, Tabelle 25", 7025), RUSSLAND_26(
			"Russland, Tabelle 26", 7026), RUSSLAND_27("Russland, Tabelle 27",
			7027), RUSSLAND_28("Russland, Tabelle 28", 7028), TUNESIEN_53(
			"Tunesien, Tabelle 53", 7053), TUNESIEN_54("Tunesien, Tabelle 54",
			7054), TUNESIEN_55("Tunesien, Tabelle 55", 7055), TUNESIEN_56(
			"Tunesien, Tabelle 56", 7056),

	BULGARIEN_1("Bulgarien, Tabelle 01", 8001), BULGARIEN_2(
			"Bulgarien, Tabelle 02", 8002), BULGARIEN_3(
			"Bulgarien, Tabelle 03", 8003), BULGARIEN_4(
			"Bulgarien, Tabelle 04", 8004), NIEDERLANDE_17(
			"Niederlande, Tabelle 17", 8017), NIEDERLANDE_18(
			"Niederlande, Tabelle 18", 8018), NIEDERLANDE_19(
			"Niederlande, Tabelle 19", 8019), NIEDERLANDE_20(
			"Niederlande, Tabelle 20", 8020), NIEDERLANDE_21(
			"Niederlande, Tabelle 21", 8021), NIEDERLANDE_22(
			"Niederlande, Tabelle 22", 8022), NIEDERLANDE_23(
			"Niederlande, Tabelle 23", 8023), NIEDERLANDE_24(
			"Niederlande, Tabelle 24", 8024), PORTUGAL_41(
			"Portugal, Tabelle 41", 8041), PORTUGAL_42("Portugal, Tabelle 42",
			8042), PORTUGAL_43("Portugal, Tabelle 43", 8043), PORTUGAL_44(
			"Portugal, Tabelle 44", 8044), PORTUGAL_45("Portugal, Tabelle 45",
			8045), PORTUGAL_46("Portugal, Tabelle 46", 8046), PORTUGAL_47(
			"Portugal, Tabelle 47", 8047), PORTUGAL_48("Portugal, Tabelle 48",
			8048),

	ALBANIEN_1("Albanien, Tabelle 01", 9001), ALBANIEN_2(
			"Albanien, Tabelle 02", 9002), DAENEMARK_9("Dänemark, Tabelle 09",
			9009), DAENEMARK_10("Dänemark, Tabelle 10", 9010), DAENEMARK_11(
			"Dänemark, Tabelle 11", 9011), DAENEMARK_12("Dänemark, Tabelle 12",
			9012), DAENEMARK_13("Dänemark, Tabelle 13", 9013), DAENEMARK_14(
			"Dänemark, Tabelle 14", 9014), DAENEMARK_15("Dänemark, Tabelle 15",
			9015), DAENEMARK_16("Dänemark, Tabelle 16", 9016), SLOVENIEN_33(
			"Slovenien, Tabelle 33", 9033), SLOVENIEN_34(
			"Slovenien, Tabelle 34", 9034), SLOVENIEN_35(
			"Slovenien, Tabelle 35", 9035), SLOVENIEN_36(
			"Slovenien, Tabelle 36", 9036), LETTLAND_51("Lettland, Tabelle 51",
			9051), LETTLAND_52("Lettland, Tabelle 52", 9052), LIECHTENSTEIN_59(
			"Liechtenstein, Tabelle 59", 9059), LIECHTENSTEIN_60(
			"Liechtenstein, Tabelle 60", 9060),

	OESTERREICH_1("Österreich, Tabelle 01", 10001), OESTERREICH_2(
			"Österreich, Tabelle 02", 10002), OESTERREICH_3(
			"Österreich, Tabelle 03", 10003), OESTERREICH_4(
			"Österreich, Tabelle 04", 10004), OESTERREICH_5(
			"Österreich, Tabelle 05", 10005), OESTERREICH_6(
			"Österreich, Tabelle 06", 10006), OESTERREICH_7(
			"Österreich, Tabelle 07", 10007), OESTERREICH_8(
			"Österreich, Tabelle 08", 10008), ISLAND_21("Island, Tabelle 21",
			010021), ISLAND_22("Island, Tabelle 22", 10022), ISLAND_23(
			"Island, Tabelle 23", 10023), ISLAND_24("Island, Tabelle 24",
			010024), GIBRALTAR_33("Gibraltar, Tabelle 33", 10033), GIBRALTAR_34(
			"Gibraltar, Tabelle 34", 10034), LIBANON_53("Libanon, Tabelle 53",
			010053), LIBANON_54("Libanon, Tabelle 54", 10054), LIBANON_55(
			"Libanon, Tabelle 55", 10055), LIBANON_56("Libanon, Tabelle 56",
			010056),

	UNGARN_1("Ungarn, Tabelle 01", 11001), UNGARN_2("Ungarn, Tabelle 02",
			011002), UNGARN_3("Ungarn, Tabelle 03", 11003), UNGARN_4(
			"Ungarn, Tabelle 04", 11004), IRAK_17("Irak, Tabelle 17", 11017), IRAK_18(
			"Irak, Tabelle 18", 11018), IRAK_19("Irak, Tabelle 19", 11019), IRAK_20(
			"Irak, Tabelle 20", 11020), MONACO_33("Monaco, Tabelle 33", 11033), MONACO_34(
			"Monaco, Tabelle 34", 11034),

	UK_5("Vereinigtes Königreich, Tabelle 05", 12005), UK_6(
			"Vereinigtes Königreich, Tabelle 06", 12006), UK_7(
			"Vereinigtes Königreich, Tabelle 07", 12007), UK_8(
			"Vereinigtes Königreich, Tabelle 08", 12008), UK_9(
			"Vereinigtes Königreich, Tabelle 09", 12009), UK_10(
			"Vereinigtes Königreich, Tabelle 10", 12010), UK_11(
			"Vereinigtes Königreich, Tabelle 11", 12011), UK_12(
			"Vereinigtes Königreich, Tabelle 12", 12012), UK_13(
			"Vereinigtes Königreich, Tabelle 13", 12013), UK_14(
			"Vereinigtes Königreich, Tabelle 14", 12014), UK_15(
			"Vereinigtes Königreich, Tabelle 15", 12015), UK_16(
			"Vereinigtes Königreich, Tabelle 16", 12016), UK_17(
			"Vereinigtes Königreich, Tabelle 17", 12017), UK_18(
			"Vereinigtes Königreich, Tabelle 18", 12018), UK_19(
			"Vereinigtes Königreich, Tabelle 19", 12019), UK_20(
			"Vereinigtes Königreich, Tabelle 20", 12020), KROATIEN_33(
			"Kroatien, Tabelle 33", 12033), KROATIEN_34("Kroatien, Tabelle 34",
			12034), LITAUEN_43("Litauen, Tabelle 43", 012043), LITAUEN_44(
			"Litauen, Tabelle 44", 12044), MALTA_59("Malta, Tabelle 59", 12059), MALTA_60(
			"Malta, Tabelle 60", 12060),

	GERMANIA_1("Deutschland(13), Tabelle 01, entspricht LCL der BASt", 13001), GERMANIA_2(
			"Deutschland(13), Tabelle 02", 13002), GERMANIA_3(
			"Deutschland(13), Tabelle 03", 13003), GERMANIA_4(
			"Deutschland(13), Tabelle 04", 13004), GERMANIA_5(
			"Deutschland(13), Tabelle 05", 13005), GERMANIA_6(
			"Deutschland(13), Tabelle 06", 13006), GERMANIA_7(
			"Deutschland(13), Tabelle 07", 13007), GERMANIA_8(
			"Deutschland(13), Tabelle 08", 13008), LYBIEN_33(
			"Libyen, Tabelle 33", 13033), LYBIEN_34("Libyen, Tabelle 34",
			013034), LYBIEN_35("Libyen, Tabelle 35", 13035), LYBIEN_36(
			"Libyen, Tabelle 36", 13036), SERBIEN_51("Serbien, Tabelle 51",
			013051), SERBIEN_52("Serbien, Tabelle 52", 13052),

	RUMAENIEN_1("Rumänien, Tabelle 01", 14001), RUMAENIEN_2(
			"Rumänien, Tabelle 02", 14002), RUMAENIEN_3("Rumänien, Tabelle 03",
			14003), RUMAENIEN_4("Rumänien, Tabelle 04", 14004), SPANIEN_17(
			"Spanien, Tabelle 17", 014017), SPANIEN_18("Spanien, Tabelle 18",
			14018), SPANIEN_19("Spanien, Tabelle 19", 14019), SPANIEN_20(
			"Spanien, Tabelle 20", 014020), SPANIEN_21("Spanien, Tabelle 21",
			14021), SPANIEN_22("Spanien, Tabelle 22", 14022), SPANIEN_23(
			"Spanien, Tabelle 23", 014023), SPANIEN_24("Spanien, Tabelle 24",
			14024), SCHWEDEN_33("Schweden, Tabelle 33", 14033), SCHWEDEN_34(
			"Schweden, Tabelle 34", 14034), SCHWEDEN_35("Schweden, Tabelle 35",
			14035), SCHWEDEN_36("Schweden, Tabelle 36", 14036), SCHWEDEN_37(
			"Schweden, Tabelle 37", 14037), SCHWEDEN_38("Schweden, Tabelle 38",
			14038), SCHWEDEN_39("Schweden, Tabelle 39", 14039), SCHWEDEN_40(
			"Schweden, Tabelle 40", 14040),

	BELORUSSLAND_1("Belorussland, Tabelle 01", 15001), BELORUSSLAND_2(
			"Belorussland, Tabelle 02", 15002), BELORUSSLAND_3(
			"Belorussland, Tabelle 03", 15003), BELORUSSLAND_4(
			"Belorussland, Tabelle 04", 15004), AEGYPTEN_9(
			"Ägypten, Tabelle 09", 15009), AEGYPTEN_10("Ägypten, Tabelle 10",
			015010), AEGYPTEN_11("Ägypten, Tabelle 11", 15011), AEGYPTEN_12(
			"Ägypten, Tabelle 12", 15012), FRANKREICH_17(
			"Frankreich, Tabelle 17", 15017), FRANKREICH_18(
			"Frankreich, Tabelle 18", 15018), FRANKREICH_19(
			"Frankreich, Tabelle 19", 15019), FRANKREICH_20(
			"Frankreich, Tabelle 20", 15020), FRANKREICH_21(
			"Frankreich, Tabelle 21", 15021), FRANKREICH_22(
			"Frankreich, Tabelle 22", 15022), FRANKREICH_23(
			"Frankreich, Tabelle 23", 15023), FRANKREICH_24(
			"Frankreich, Tabelle 24", 15024), FRANKREICH_25(
			"Frankreich, Tabelle 25", 15025), FRANKREICH_26(
			"Frankreich, Tabelle 26", 15026), FRANKREICH_27(
			"Frankreich, Tabelle 27", 15027), FRANKREICH_28(
			"Frankreich, Tabelle 28", 15028), FRANKREICH_29(
			"Frankreich, Tabelle 29", 15029), FRANKREICH_30(
			"Frankreich, Tabelle 30", 15030), FRANKREICH_31(
			"Frankreich, Tabelle 31", 15031), FRANKREICH_32(
			"Frankreich, Tabelle 32", 15032), BOSNIEN__HERZEGOWINA_43(
			"Bosnien-Herzegowina, Tabelle 43", 15043), BOSNIEN__HERZEGOWINA_44(
			"Bosnien-Herzegowina, Tabelle 44", 15044), NORWEGEN_49(
			"Norwegen, Tabelle 49", 15049), NORWEGEN_50("Norwegen, Tabelle 50",
			15050), NORWEGEN_51("Norwegen, Tabelle 51", 15051), NORWEGEN_52(
			"Norwegen, Tabelle 52", 15052), NORWEGEN_53("Norwegen, Tabelle 53",
			15053), NORWEGEN_54("Norwegen, Tabelle 54", 15054), NORWEGEN_55(
			"Norwegen, Tabelle 55", 15055), NORWEGEN_56("Norwegen, Tabelle 56",
			15056);

	/**
	 * liefert die Location Tabelle mit dem übergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine der Status GERMANIA_1 geliefert.
	 */
	public static RdsLocationTabelle getStatus(final int gesuchterCode) {
		RdsLocationTabelle result = RdsLocationTabelle.GERMANIA_1;
		for (RdsLocationTabelle status : values()) {
			if (status.getCode() == gesuchterCode) {
				result = status;
				break;
			}
		}
		return result;

	}

	/**
	 * der Code des Zustandes.
	 */
	private int code;

	/**
	 * der Name des Zustandes.
	 */
	private String name;

	/**
	 * Konstruktor.<br>
	 * Die Funktion einen eine neue Instanz für eine Locationtabelle mit dem
	 * übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor wird
	 * nur innerhalb der Klasse verwendet. Es wird eine Menge vordefinierter
	 * Zustände zur Verfügung gestellt.
	 * 
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private RdsLocationTabelle(final String name, final int code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * liefert den Code des Zustandes.
	 * 
	 * @return den Code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * liefert den Namen des Zustandes.
	 * 
	 * @return der Name
	 */
	public String getName() {
		return name;
	}

}
