package com.cosmiiko.bsodod;

import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateChecker {
    static String GetLatestVersion() {
        String gradleFile = null;
        try {
            gradleFile = IOUtils.toString(
                    new URL("https://raw.githubusercontent.com/Cosmiiko/MC-BSODOnDeath/main/build.gradle"),
                    Charset.defaultCharset()
            );
        } catch (IOException e) {
            return null;
        }

        if (gradleFile == null) return null;

        Pattern pattern = Pattern.compile("version = '(.*)'");
        Matcher matcher = pattern.matcher(gradleFile);

        if (matcher.find())
        {
            return matcher.group(1);
        }
        else
        {
            return null;
        }
    }

    static String GetCurrentVersion()
    {
        return UpdateChecker.class.getPackage().getImplementationVersion();
    }
}
