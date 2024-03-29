package io.github.zechiax.date;

import io.github.zechiax.api.RenamePluginBase;
import io.github.zechiax.api.RenamingException;
import org.pf4j.Extension;
import org.pf4j.Plugin;

import java.time.LocalDate;

public class DatePlugin extends Plugin  {
    @Extension
    public static class DateMask extends RenamePluginBase {
    // This will have 3 masks
    // [D] - Day
    // [M] - Month
    // [Y] - Year
    private final String dayPattern = "[D]";
    private final String monthPattern = "[M]";
    private final String yearPattern = "[Y]";
    @Override
    public String getName() {
        return "Date Mask";
    }

    @Override
    public String rename() throws RenamingException {
        var date = LocalDate.now();

        var day = String.format("%02d", date.getDayOfMonth());
        var month = String.format("%02d", date.getMonthValue());
        var year = String.valueOf(date.getYear());

        var newName = context.getCurrentName();


        var indices = getPatternIndices(dayPattern, newName);
        for (var index : indices) {
            newName = replaceSubstring(newName, day, index, index + dayPattern.length());
        }



        indices = getPatternIndices(monthPattern, newName);
        for (var index : indices) {
            newName = replaceSubstring(newName, month, index, index + monthPattern.length());
        }



        indices = getPatternIndices(yearPattern, newName);
        for (var index : indices) {
            newName = replaceSubstring(newName, year, index, index + yearPattern.length());
        }


        return newName;
    }
}

}
