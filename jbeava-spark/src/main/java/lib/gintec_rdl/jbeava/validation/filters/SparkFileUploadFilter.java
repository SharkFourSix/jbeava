package lib.gintec_rdl.jbeava.validation.filters;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.resolvers.SparkFieldResolverEx;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class SparkFileUploadFilter extends ValidationFilterImpl<SparkFieldResolverEx.FilePartInformation, File> {
    private static final String MESSAGE_TEMPLATE = "${name} is not a recognized file type.";

    public SparkFileUploadFilter() {
        super("fileUpload");
    }

    private Map<String, Object> parseArguments(List<String> args, long minSize, long maxSize) {
        Map<String, Object> values = new LinkedHashMap<>() {{
            put("minSize", minSize);
            put("maxSize", maxSize);
            put("preserveName", false);
        }};
        if (!args.isEmpty()) {
            for (String arg : args) {
                // minSize:43,maxSize:34,preserve:true
            }
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(Map<String, Object> args, String key) {
        return (T) args.get(key);
    }

    @Override
    public File filter(String name, String message, SparkFieldResolverEx.FilePartInformation filePartInformation, List<String> args) throws JBeavaException {
        File file;
        long minSize;
        long maxSize;
        boolean preserveName;
        String fileExtension;
        Map<String, Object> argValues;

        if (filePartInformation == null) {
            return null;
        }

        // Get arguments
        argValues = parseArguments(args, filePartInformation.getMinSize(), filePartInformation.getMaxSize());
        minSize = getValue(argValues, "minSize");
        maxSize = getValue(argValues, "maxSize");
        preserveName = getValue(argValues, "preserveName");

        // File name
        file = new File(
                filePartInformation.getPath(),
                preserveName ? Optional.ofNullable(filePartInformation.getPart()
                        .getSubmittedFileName()).orElse(Long.toHexString(System.currentTimeMillis()))
                        : Long.toHexString(System.currentTimeMillis())
        );

        // We only apply content type validation if a validator is specified
        if (filePartInformation.getValidator() == null) {
            int index = file.getName().lastIndexOf('.');
            if (index != -1) {
                fileExtension = file.getName().substring(index + 1);
            } else {
                fileExtension = "";
            }
        } else {
            // Save the part to disk so we can seek back and forth
            try {
                filePartInformation.getPart().write(Long.toString(System.currentTimeMillis(), 36));
            } catch (IOException e) {
                getLogger().error("Error processing file {}.", name, e);
                throw new JBeavaException(format("There was an error when processing file %s.", name));
            }

            if (LocaleUtils.isNullOrEmpty(fileExtension = validateFile(name, file, filePartInformation.getValidator()))) {
                argValues.put("name", name);
                throw new JBeavaException(renderMessage(messageTemplate(message, MESSAGE_TEMPLATE), argValues));
            }
        }

        // move to destination


        return null;
    }
    // input
    // args (types, )
    //

    // 1. Copy to temp
    // 2. Type validator
    // 3. Return path to file

    private String validateFile(String name, File file, UploadFileContentValidator validator) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return validator.accept(fis);
        } catch (IOException e) {
            String additionalMessage;
            if (!file.delete()) {
                additionalMessage = "Additionally, ";
            } else {
                additionalMessage = "";
            }
            getLogger().error("Error when attempting to open file {}.{}", file, additionalMessage, e);
            throw new JBeavaException(format("There was an error processing the uploaded file: %s.", name));
        }
    }
}
