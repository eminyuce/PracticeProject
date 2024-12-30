package com.acqu.co.excel.converter.actuator.util;

import com.acqu.co.excel.converter.actuator.model.specs.SearchFieldOption;

import jakarta.persistence.Column;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    /**
     * Generates a list of search field options for the given class, excluding specified fields.
     *
     * @param clazz           The class to inspect.
     * @param excludedFields  A list of field names to exclude.
     * @return A list of SearchFieldOption objects.
     */
    public static List<SearchFieldOption> getSearchFieldOptions(Class<?> clazz, List<String> excludedFields) {
        List<SearchFieldOption> options = new ArrayList<>();

        // Get all declared fields of the class
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // Check if the field is annotated with @Column (i.e., it's a database column)
            if (field.isAnnotationPresent(Column.class)) {
                // Get the field name
                String fieldName = field.getName();

                // Skip excluded fields
                if (CollectionUtils.isNotEmpty(excludedFields) && excludedFields.contains(fieldName)) {
                    continue;
                }

                // Convert field name to a user-friendly display name
                String displayName = convertToDisplayName(fieldName);

                // Create a SearchFieldOption object
                options.add(new SearchFieldOption(fieldName, displayName));
            }
        }

        return options;
    }

    private static String convertToDisplayName(String fieldName) {
        // Convert camelCase to "Camel Case"
        String spacedName = fieldName.replaceAll("([A-Z])", " $1")
                .replaceAll("_", " ")
                .trim()
                .toLowerCase();

        // Capitalize the first letter of each word
        StringBuilder displayName = new StringBuilder();
        String[] words = spacedName.split(" ");
        for (String word : words) {
            if (word.length() > 0) {
                displayName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        // Remove the trailing space and return the result
        return displayName.toString().trim();
    }
}