package CPSF.com.demo.model.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Country {
    // Europe
    ALBANIA("AL", "Albania", "+355"),
    ANDORRA("AD", "Andora", "+376"),
    AUSTRIA("AT", "Austria", "+43"),
    BELGIUM("BE", "Belgia", "+32"),
    BELARUS("BY", "Białoruś", "+375"),
    BOSNIA_AND_HERZEGOVINA("BA", "Bośnia i Hercegowina", "+387"),
    BULGARIA("BG", "Bułgaria", "+359"),
    CROATIA("HR", "Chorwacja", "+385"),
    CYPRUS("CY", "Cypr", "+357"),
    CZECH_REPUBLIC("CZ", "Czechy", "+420"),
    DENMARK("DK", "Dania", "+45"),
    ESTONIA("EE", "Estonia", "+372"),
    FINLAND("FI", "Finlandia", "+358"),
    FRANCE("FR", "Francja", "+33"),
    GREECE("GR", "Grecja", "+30"),
    SPAIN("ES", "Hiszpania", "+34"),
    IRELAND("IE", "Irlandia", "+353"),
    ICELAND("IS", "Islandia", "+354"),
    LITHUANIA("LT", "Litwa", "+370"),
    LUXEMBOURG("LU", "Luksemburg", "+352"),
    LATVIA("LV", "Łotwa", "+371"),
    MALTA("MT", "Malta", "+356"),
    MOLDOVA("MD", "Mołdawia", "+373"),
    MONACO("MC", "Monako", "+377"),
    GERMANY("DE", "Niemcy", "+49"),
    NORWAY("NO", "Norwegia", "+47"),
    POLAND("PL", "Polska", "+48"),
    PORTUGAL("PT", "Portugalia", "+351"),
    ROMANIA("RO", "Rumunia", "+40"),
    SAN_MARINO("sM", "San Marino", "+378"),
    SERBIA("RS", "Serbia", "+381"),
    SLOVAKIA("SK", "Słowacja", "+421"),
    SLOVENIA("SI", "Słowenia", "+386"),
    SWITZERLAND("CH", "Szwajcaria", "+41"),
    SWEDEN("SE", "Szwecja", "+46"),
    TURKEY("TR", "Turcja", "+90"),
    UKRAINE("UA", "Ukraina", "+380"),
    HUNGARY("HU", "Węgry", "+36"),
    UNITED_KINGDOM("GB", "Wielka Brytania", "+44"),
    ITALY("IT", "Włochy", "+39"),

    // Americas
    ANTIGUA_AND_BARBUDA("AG", "Antigua i Barbuda", "+1-268"),
    ARGENTINA("AR", "Argentyna", "+54"),
    BAHAMAS("BS", "Bahamy", "+1-242"),
    BARBADOS("BB", "Barbados", "+1-246"),
    BELIZE("BZ", "Belize", "+501"),
    BOLIVIA("BO", "Boliwia", "+591"),
    BRAZIL("BR", "Brazylia", "+55"),
    CHILE("CL", "Chile", "+56"),
    COLOMBIA("CO", "Kolumbia", "+57"),
    COSTA_RICA("CR", "Kostaryka", "+506"),
    CUBA("CU", "Kuba", "+53"),
    DOMINICA("DM", "Dominika", "+1-767"),
    DOMINICAN_REPUBLIC("DO", "Dominikana", "+1-809"),
    ECUADOR("EC", "Ekwador", "+593"),
    EL_SALVADOR("SV", "Salwador", "+503"),
    GRENADA("GD", "Grenada", "+1-473"),
    GUATEMALA("GT", "Gwatemala", "+502"),
    GUYANA("GY", "Gujana", "+592"),
    HAITI("HT", "Haiti", "+509"),
    HONDURAS("HN", "Honduras", "+504"),
    JAMAICA("JM", "Jamajka", "+1-876"),
    CANADA("CA", "Kanada", "+1"),
    MEXICO("MX", "Meksyk", "+52"),
    NICARAGUA("NI", "Nikaragua", "+505"),
    PANAMA("PA", "Panama", "+507"),
    PARAGUAY("PY", "Paragwaj", "+595"),
    PERU("PE", "Peru", "+51"),
    SAINT_KITTS_AND_NEVIS("KN", "Saint Kitts i Nevis", "+1-869"),
    SAINT_LUCIA("LC", "Saint Lucia", "+1-758"),
    SAINT_VINCENT_AND_THE_GRENADINES("VC", "Saint Vincent i Grenadyny", "+1-784"),
    SURINAME("SR", "Surinam", "+597"),
    TRINIDAD_AND_TOBAGO("tt", "Trynidad i Tobago", "+1-868"),
    UNITED_STATES("US", "Stany Zjednoczone", "+1"),
    URUGUAY("UY", "Urugwaj", "+598"),
    VENEZUELA("VE", "Wenezuela", "+58");

    private final String isoCode;
    private final String name;
    private final String phoneSuffix;

    Country(String isoCode, String name, String phoneSuffix) {
        this.isoCode = isoCode;
        this.name = name;
        this.phoneSuffix = phoneSuffix;
    }

    public static Optional<Country> getByIsoCode(String isoCode) {
        return Arrays.stream(Country.values()).filter(c -> c.isoCode.equals(isoCode)).findFirst();
    }
}
