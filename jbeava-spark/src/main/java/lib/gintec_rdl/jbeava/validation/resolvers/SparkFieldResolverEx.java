package lib.gintec_rdl.jbeava.validation.resolvers;

import lib.gintec_rdl.jbeava.validation.filters.UploadFileContentValidator;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;
import org.eclipse.jetty.http.MultiPartFormInputStream;
import org.eclipse.jetty.util.MultiPartInputStreamParser;
import spark.Request;
import spark.route.HttpMethod;
import spark.utils.IOUtils;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

/**
 * This resolver extends {@link SparkFieldResolver} to add file upload handling and validation support.
 * <p>
 * The resolver produces
 */
public class SparkFieldResolverEx extends SparkFieldResolver {

    public SparkFieldResolverEx(Request request, MultipartConfigElement mce) {
        super(request, mce);
    }

    public SparkFieldResolverEx(Request request) {
        super(request);
    }

    @Override
    public Object getField(String fieldName) throws Exception {
        if (getRequestMethod() == HttpMethod.post) {
            if (isMultipart()) {
                MultiPartFormInputStream.MultiPart part = (MultiPartFormInputStream.MultiPart) request.raw().getPart(fieldName);
                String fileName = part.getContentDispositionFilename();;
                if(LocaleUtils.isNullOrEmpty(fieldName)){
                    return IOUtils.toString(part.getInputStream());
                }else{
                    // Return file part
                }
            }
        }
        return super.getField(fieldName);
    }

    public class FilePartInformation {
        private Part part;
        private String path;
        private UploadFileContentValidator validator;

        public Part getPart() {
            return part;
        }

        public void setPart(Part part) {
            this.part = part;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public UploadFileContentValidator getValidator() {
            return validator;
        }

        public void setValidator(UploadFileContentValidator validator) {
            this.validator = validator;
        }
    }
}
