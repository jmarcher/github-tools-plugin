package com.nerdscorner.android.plugin.utils;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class GithubUtils {
    private GithubUtils() {
    }

    public static void openWebLink(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebLink(URL url) {
        try {
            openWebLink(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
