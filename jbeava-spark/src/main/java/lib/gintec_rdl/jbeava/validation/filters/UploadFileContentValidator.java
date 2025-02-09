package lib.gintec_rdl.jbeava.validation.filters;

import java.io.InputStream;

public interface UploadFileContentValidator {
    /**
     * Inspects the given file's input stream and returns its corresponding file extension.
     *
     * @param inputStream The input stream whose contents to inspect. The stream is guaranteed to be seekable.
     * @return File extension or null to indicate the file is unsupported or unknown by the validating party.
     */
    String accept(InputStream inputStream);
}
