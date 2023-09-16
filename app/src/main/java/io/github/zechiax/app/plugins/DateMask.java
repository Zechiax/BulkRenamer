package io.github.zechiax.app.plugins;

import io.github.zechiax.app.core.RenameContext;
import io.github.zechiax.app.core.RenamePluginBase;
import io.github.zechiax.app.core.RenamingException;

import java.time.LocalDate;

public class DateMask extends RenamePluginBase {
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
    public String rename(RenameContext context) throws RenamingException {
        var date = LocalDate.now();

        var day = String.format("%02d", date.getDayOfMonth());
        var month = String.format("%02d", date.getMonthValue());
        var year = String.valueOf(date.getYear());

        var newName = context.currentName();


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
