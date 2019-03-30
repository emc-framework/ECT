package me.deftware.inject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EMCTweakerSecond extends EMCTweaker {
	
	@Override
    public final void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.list = new ArrayList<>();
    }

}
