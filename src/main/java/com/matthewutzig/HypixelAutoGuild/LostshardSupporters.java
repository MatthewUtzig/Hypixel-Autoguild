package com.matthewutzig.HypixelAutoGuild;

import java.util.ArrayList;
import java.util.Random;

public class LostshardSupporters {

    protected static ArrayList<String> supporters = new ArrayList<String>();
    protected static boolean initialized = false;

    //return if supporter exists
    public static boolean isSupporter(String name) {
        if(!initialized) {
            initialized = true;
            supporters.add("ilovegifts");
            supporters.add("jedleo");
            supporters.add("_chlxe");
            supporters.add("astoeth");
            supporters.add("advocat_");
            supporters.add("stringsoffantasy");
            supporters.add("distillatez");
            supporters.add("nerdycombos");
            supporters.add("garygary1275");
            supporters.add("richienb");
            supporters.add("cannedmeats");
            supporters.add("shacor");
            supporters.add("evetimi");
            supporters.add("spheredsphered10");
            supporters.add("xdavidet");
            supporters.add("swarnava");
            supporters.add("itsnt");
            supporters.add("ssf7");
            supporters.add("undyingninja");
            supporters.add("fenwicke_fox");
            supporters.add("theskillerhacker");
            supporters.add("doubleupperyt");
            supporters.add("kaisarcubetv");
            supporters.add("drewtheshrew");
            supporters.add("nulistaf");
            supporters.add("sc_brightstar");
            supporters.add("croooked");
            supporters.add("annaliarose1");
            supporters.add("techmarkgaming");
            supporters.add("whitplayz");
            supporters.add("paulobbe");
            supporters.add("zadioc");
            supporters.add("deafpower18");
            supporters.add("skywarrior299");
            supporters.add("lamfugax");
            supporters.add("ares054");
            supporters.add("ktap4321");
            supporters.add("ylipee_br");
            supporters.add("wesleyjef");
            supporters.add("croooked");
            supporters.add("bengee23");
            supporters.add("emptied");
            supporters.add("bogiplay_yt");
            supporters.add("styxasiangg");
            supporters.add("bengee23");
            supporters.add("emptied_");
            supporters.add("w1nter_s0ld1er");
            supporters.add("garynlol");
            supporters.add("master_chief42");
            supporters.add("224659477");
            supporters.add("malic3_");
            supporters.add("prodigiousus");
            supporters.add("cringefridge");
            supporters.add("forkannaa");
            supporters.add("destroyer83746");
            supporters.add("nukestye");
            supporters.add("smooshed_potato");
        }
        return supporters.contains(name.toLowerCase());

    }
}
