package gndata.app.html;

import java.io.*;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

/**
 * Helps to configure velocity and read templates from resources.
 */
public class VelocityHelper {


    static {
        Properties p = new Properties();
        p.setProperty("resource.loader", "string");
        p.setProperty("resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");

        Velocity.init(p);
    }

    /**
     * Load a template from a resource name.
     *
     * @param path  The resource name.
     *
     * @return The loaded template.
     *
     * @throws IOException If the loading of the template fails.
     */
    public static Template getTemplateFromResource(String path) throws IOException {
        if (!Velocity.resourceExists(path)) {
            StringResourceRepository repo = StringResourceLoader.getRepository();

            InputStream stream = VelocityHelper.class.getClassLoader()
                                       .getResourceAsStream(path);

            repo.putStringResource(path, IOUtils.toString(stream, "UTF-8"));
        }
        return Velocity.getTemplate(path);
    }

}
