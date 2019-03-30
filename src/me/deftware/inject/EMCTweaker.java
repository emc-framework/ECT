package me.deftware.inject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class EMCTweaker implements ITweaker {

	protected ArrayList<String> list = new ArrayList<>();

	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
		list.addAll(args);
		if (!args.contains("--version") && profile != null) {
			list.add("--version");
			list.add(profile);
		}
		if (!args.contains("--assetDir") && assetsDir != null) {
			list.add("--assetDir");
			list.add(assetsDir.getAbsolutePath());
		}
		if (!args.contains("--gameDir") && gameDir != null) {
			list.add("--gameDir");
			list.add(gameDir.getAbsolutePath());
		}

	}

	@Override
	public String[] getLaunchArguments() {
		return list.toArray(new String[list.size()]);
	}

	@Override
	public String getLaunchTarget() {
		return "net.minecraft.client.main.Main";
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		classLoader.registerTransformer("me.deftware.inject.EMCTransformer");
	}

}
