package org.mash.loader;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import org.apache.log4j.Logger;
import org.mash.config.HarnessDefinition;
import org.mash.harness.Harness;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.TeardownHarness;
import org.mash.harness.VerifyHarness;
import org.mash.loader.harnesssetup.AnnotatedHarness;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Building a harness entails actually creating an instance of that harness, and applying the configurations to those
 * harnesses.
 * <p/>
 *
 * @author teastlack
 * @since Jul 4, 2009
 */
public class HarnessBuilder
{
    private static final Logger log = Logger.getLogger(HarnessBuilder.class.getName());
    private static HarnessBuilder instance;
    private Map<String, String> types;

    public HarnessBuilder()
    {
    }

    public static HarnessBuilder getInstance()
    {
        if (instance == null)
        {
            instance = new HarnessBuilder();
        }
        return instance;
    }

    public Harness buildHarness(HarnessDefinition harnessDefinition) throws HarnessException
    {
        Harness harness;
        try
        {
            harness = createInstance(harnessDefinition);
            if (harness instanceof SetupHarness)
            {
                harness = new SetupAnnotatedHarness(harness);
            }
            else if (harness instanceof RunHarness)
            {
                harness = new RunAnnotatedHarness(harness);
            }
            else if (harness instanceof VerifyHarness)
            {
                harness = new VerifyAnnotatedHarness(harness);
            }
            else if (harness instanceof TeardownHarness)
            {
                harness = new TeardownAnnotatedHarness(harness);
            }
            harness.setDefinition(harnessDefinition);
        }
        catch (Exception e)
        {
            throw new HarnessException("Problem building harness '" + harnessDefinition.getType() + "':" + e.getMessage(), e);
        }
        return harness;
    }

    private Harness createInstance(HarnessDefinition harnessDefinition) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        Harness result;
        //load up the types
        if (types == null)
        {
            types = new HashMap<String, String>();

//            ClassLoader loader = getClass().getClassLoader();
//            DataInputStream dstream = new DataInputStream(new BufferedInputStream(bits));
//            ClassFile cf = new ClassFile(dstream);
//            String className = cf.getName();
//            AnnotationsAttribute visible = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag);
//            for (javassist.bytecode.annotation.Annotation ann : visible.getAnnotations())
//            {
//                System.out.println("@" + ann.getTypeName());
//            }

        }
        String className = types.get(harnessDefinition.getType());
        if (className == null)
        {
            className = harnessDefinition.getType();
        }
        result = (Harness) Class.forName(className).newInstance();
        return result;
    }

    private class SetupAnnotatedHarness extends AnnotatedHarness implements SetupHarness
    {
        private SetupAnnotatedHarness(Harness wrap)
        {
            super(wrap);
        }

        public void setup() throws Exception
        {
            ((SetupHarness) getWrap()).setup();
        }
    }

    private class RunAnnotatedHarness extends AnnotatedHarness implements RunHarness
    {
        private RunAnnotatedHarness(Harness wrap)
        {
            super(wrap);
        }

        public void run(List<RunHarness> previous, List<SetupHarness> setups)
        {
            ((RunHarness) getWrap()).run(previous, setups);
        }

        public RunResponse getResponse()
        {
            return ((RunHarness) getWrap()).getResponse();
        }
    }

    private class VerifyAnnotatedHarness extends AnnotatedHarness implements VerifyHarness
    {
        private VerifyAnnotatedHarness(Harness wrap)
        {
            super(wrap);
        }

        public void verify(RunHarness run, List<SetupHarness> setup)
        {
            ((VerifyHarness) getWrap()).verify(run, setup);
        }
    }

    private class TeardownAnnotatedHarness extends AnnotatedHarness implements TeardownHarness
    {
        private TeardownAnnotatedHarness(Harness wrap)
        {
            super(wrap);
        }

        public void teardown(List<SetupHarness> setups)
        {
            ((TeardownHarness) getWrap()).teardown(setups);
        }
    }

    //todo: finish parsing jarfiles
    //been using http://code.google.com/p/annovention/ (nice work Animesh Kumar)
//    private ResourceIterator getResourceIterator(URL url, Filter filter) throws IOException
//    {
//        String urlString = url.toString();
//        if (urlString.endsWith("!/")) {
//            urlString = urlString.substring(4);
//            urlString = urlString.substring(0, urlString.length() - 2);
//            url = new URL(urlString);
//        }
//
//        if (!urlString.endsWith("/")) {
//            return new JarFileIterator(url.openStream(), filter);
//        } else {
//
//            if (!url.getProtocol().equals("file")) {
//                throw new IOException("Unable to understand protocol: " + url.getProtocol());
//            }
//
//            File f = new File(url.getPath());
//            if (f.isDirectory()) {
//                return new ClassFileIterator(f, filter);
//            } else {
//                return new JarFileIterator(url.openStream(), filter);
//            }
//        }
//    }


    public final URL[] findResources()
    {
        List<URL> list = new ArrayList<URL>();
        String classpath = System.getProperty("java.class.path");
        StringTokenizer tokenizer = new StringTokenizer(classpath,
                                                        File.pathSeparator);

        while (tokenizer.hasMoreTokens())
        {
            String path = tokenizer.nextToken();
            try
            {
                path = java.net.URLDecoder.decode(path, "UTF-8");

                File fp = new File(path);
                if (!fp.exists())
                {
                    throw new RuntimeException(
                            "File in java.class.path does not exist: " + fp);
                }
                list.add(fp.toURL());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return list.toArray(new URL[list.size()]);
    }

}
