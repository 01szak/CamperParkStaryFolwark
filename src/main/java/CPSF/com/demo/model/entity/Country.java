package CPSF.com.demo.model.entity;

import lombok.Getter;

@Getter
public enum Country {
    // Europe
    ALBANIA("al", "Albania", "+355"),
    ANDORRA("ad", "Andora", "+376"),
    AUSTRIA("at", "Austria", "+43"),
    BELGIUM("be", "Belgia", "+32"),
    BELARUS("by", "Białoruś", "+375"),
    BOSNIA_AND_HERZEGOVINA("ba", "Bośnia i Hercegowina", "+387"),
    BULGARIA("bg", "Bułgaria", "+359"),
    CROATIA("hr", "Chorwacja", "+385"),
    CYPRUS("cy", "Cypr", "+357"),
    CZECH_REPUBLIC("cz", "Czechy", "+420"),
    DENMARK("dk", "Dania", "+45"),
    ESTONIA("ee", "Estonia", "+372"),
    FINLAND("fi", "Finlandia", "+358"),
    FRANCE("fr", "Francja", "+33"),
    GREECE("gr", "Grecja", "+30"),
    SPAIN("es", "Hiszpania", "+34"),
    IRELAND("ie", "Irlandia", "+353"),
    ICELAND("is", "Islandia", "+354"),
    LITHUANIA("lt", "Litwa", "+370"),
    LUXEMBOURG("lu", "Luksemburg", "+352"),
    LATVIA("lv", "Łotwa", "+371"),
    MALTA("mt", "Malta", "+356"),
    MOLDOVA("md", "Mołdawia", "+373"),
    MONACO("mc", "Monako", "+377"),
    GERMANY("de", "Niemcy", "+49"),
    NORWAY("no", "Norwegia", "+47"),
    POLAND("pl", "Polska", "+48"),
    PORTUGAL("pt", "Portugalia", "+351"),
    ROMANIA("ro", "Rumunia", "+40"),
    SAN_MARINO("sm", "San Marino", "+378"),
    SERBIA("rs", "Serbia", "+381"),
    SLOVAKIA("sk", "Słowacja", "+421"),
    SLOVENIA("si", "Słowenia", "+386"),
    SWITZERLAND("ch", "Szwajcaria", "+41"),
    SWEDEN("se", "Szwecja", "+46"),
    TURKEY("tr", "Turcja", "+90"),
    UKRAINE("ua", "Ukraina", "+380"),
    HUNGARY("hu", "Węgry", "+36"),
    UNITED_KINGDOM("gb", "Wielka Brytania", "+44"),
    ITALY("it", "Włochy", "+39"),

    // Americas
    ANTIGUA_AND_BARBUDA("ag", "Antigua i Barbuda", "+1-268"),
    ARGENTINA("ar", "Argentyna", "+54"),
    BAHAMAS("bs", "Bahamy", "+1-242"),
    BARBADOS("bb", "Barbados", "+1-246"),
    BELIZE("bz", "Belize", "+501"),
    BOLIVIA("bo", "Boliwia", "+591"),
    BRAZIL("br", "Brazylia", "+55"),
    CHILE("cl", "Chile", "+56"),
    COLOMBIA("co", "Kolumbia", "+57"),
    COSTA_RICA("cr", "Kostaryka", "+506"),
    CUBA("cu", "Kuba", "+53"),
    DOMINICA("dm", "Dominika", "+1-767"),
    DOMINICAN_REPUBLIC("do", "Dominikana", "+1-809"),
    ECUADOR("ec", "Ekwador", "+593"),
    EL_SALVADOR("sv", "Salwador", "+503"),
    GRENADA("gd", "Grenada", "+1-473"),
    GUATEMALA("gt", "Gwatemala", "+502"),
    GUYANA("gy", "Gujana", "+592"),
    HAITI("ht", "Haiti", "+509"),
    HONDURAS("hn", "Honduras", "+504"),
    JAMAICA("jm", "Jamajka", "+1-876"),
    CANADA("ca", "Kanada", "+1"),
    MEXICO("mx", "Meksyk", "+52"),
    NICARAGUA("ni", "Nikaragua", "+505"),
    PANAMA("pa", "Panama", "+507"),
    PARAGUAY("py", "Paragwaj", "+595"),
    PERU("pe", "Peru", "+51"),
    SAINT_KITTS_AND_NEVIS("kn", "Saint Kitts i Nevis", "+1-869"),
    SAINT_LUCIA("lc", "Saint Lucia", "+1-758"),
    SAINT_VINCENT_AND_THE_GRENADINES("vc", "Saint Vincent i Grenadyny", "+1-784"),
    SURINAME("sr", "Surinam", "+597"),
    TRINIDAD_AND_TOBAGO("tt", "Trynidad i Tobago", "+1-868"),
    UNITED_STATES("us", "Stany Zjednoczone", "+1"),
    URUGUAY("uy", "Urugwaj", "+598"),
    VENEZUELA("ve", "Wenezuela", "+58");

    private final String isoCode;
    private final String name;
    private final String phoneSuffix;

    Country(String isoCode, String name, String phoneSuffix) {
        this.isoCode = isoCode;
        this.name = name;
        this.phoneSuffix = phoneSuffix;
    }
}
