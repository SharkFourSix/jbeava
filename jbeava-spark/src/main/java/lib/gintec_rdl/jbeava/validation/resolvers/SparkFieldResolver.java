package lib.gintec_rdl.jbeava.validation.resolvers;

import lib.gintec_rdl.jbeava.validation.FieldResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.utils.IOUtils;

import javax.servlet.MultipartConfigElement;
import java.util.Locale;

/**
 * <p>This resolver source data from SparkJava's HTTP requests.</p>
 * <p>The resolver supports GET and POST methods only.</p>
 */
public class SparkFieldResolver extends FieldResolver {
    protected Request request;
    private final Logger logger;
    protected MultipartConfigElement mce;

    /**
     * @param request The request to pull data from.
     * @param mce     Configuration for handling multipart forms.
     */
    public SparkFieldResolver(Request request, MultipartConfigElement mce) {
        this.request = request;
        this.mce = mce;
        this.logger = LoggerFactory.getLogger(SparkFieldResolver.class);
    }

    public SparkFieldResolver(Request request) {
        this(request, null);
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public Object getField(String fieldName) {
        if ("post".equalsIgnoreCase(request.requestMethod())) {
            if (request.contentType().startsWith("multipart/form-data")) {
                MultipartConfigElement config = request.attribute("org.eclipse.jetty.multipartConfig");
                if (config == null) {
                    config = getMultipartConfiguration();
                }
                request.attribute("org.eclipse.jetty.multipartConfig", config);
                try {
                    return IOUtils.toString(request.raw().getPart(fieldName).getInputStream());
                } catch (Exception e) {
                    getLogger().error("Error when reading multipart form field {}.", fieldName, e);
                    return null;
                }
            } else {
                return request.queryParams(fieldName);
            }
        } else if ("get".equalsIgnoreCase(request.requestMethod())) {
            return request.params(fieldName);
        } else {
            throw new UnsupportedOperationException(String.format(Locale.US,
                    "Reading from %s methods is not supported.", request.requestMethod()));
        }
    }

    private MultipartConfigElement getMultipartConfiguration() {
        if (this.mce == null) {
            this.mce = new MultipartConfigElement("tmp");
        }
        return this.mce;
    }
}