package org.mash.loader;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;
import org.apache.log4j.Logger;
import org.mash.config.HarnessDefinition;
import org.mash.config.Run;
import org.mash.config.Setup;
import org.mash.config.Teardown;
import org.mash.config.Verify;
import org.mash.harness.Harness;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.TeardownHarness;
import org.mash.harness.VerifyHarness;
import org.mash.loader.harnesssetup.AnnotatedHarness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<TypeKey, Class> types;

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
            throw new HarnessException(
                    "Problem building harness '" + harnessDefinition.getType() + "':" + e.getMessage(), e);
        }
        return harness;
    }

    private Harness createInstance(HarnessDefinition harnessDefinition)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, HarnessException
    {
        Harness result;
        //load up the types
        if (types == null)
        {
            Discoverer discoverer = new ClasspathDiscoverer();
            // Register class annotation listener
            NamedClassDiscover namedClassDiscover = new NamedClassDiscover();
            discoverer.addAnnotationListener(namedClassDiscover);
            discoverer.discover();
            types = buildTypes(namedClassDiscover.classes);
        }

        TypeKey key = TypeKey.build(harnessDefinition.getType(), harnessDefinition);
        Class clazz = types.get(key);
        if (clazz == null)
        {
            try
            {
                clazz = Class.forName(harnessDefinition.getType());
            }
            catch (ClassNotFoundException e)
            {
                throw new HarnessException("Unable to create class "+harnessDefinition.getType(), e);
            }
        }

        log.debug("Building harness " + clazz.getName());
        result = (Harness) clazz.newInstance();
        return result;
    }

    private Map<TypeKey, Class> buildTypes(List<String> classes) throws ClassNotFoundException, HarnessException
    {
        Map<TypeKey, Class> result = new HashMap<TypeKey, Class>();
        for (String className : classes)
        {
            Class harness = null;
            try
            {
                harness = Class.forName(className);
            }
            catch (ClassNotFoundException e)
            {
                log.debug(e);
                log.info("Not adding " + className + " as library not found (you may not be using it).  " +
                                 "Use debug to see full stack");
            }
            if (harness != null)
            {
                TypeKey key = TypeKey.build(harness);
                if (result.get(key) != null)
                {
                    throw new HarnessException(
                            "Harness with name already exists! Name:" + key.name + " type: " + key.type.name());
                }
                result.put(key, harness);
            }
        }
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

    private class NamedClassDiscover implements ClassAnnotationDiscoveryListener
    {
        List<String> classes = new ArrayList<String>();

        public void discovered(String clazz, String annotation)
        {
            log.info("Adding harness named class " + clazz + " annotation:" + annotation);
            classes.add(clazz);
        }

        public String[] supportedAnnotations()
        {
            return new String[]{HarnessName.class.getName()};
        }
    }

    private static class TypeKey
    {
        private String name;
        private KeyTypes type;

        public static TypeKey build(String annotationName, HarnessDefinition harness)
        {
            TypeKey result = new TypeKey();
            result.name = annotationName;

            if (harness instanceof Setup)
            {
                result.type = KeyTypes.SETUP;
            }
            else if (harness instanceof Run)
            {
                result.type = KeyTypes.RUN;
            }
            else if (harness instanceof Verify)
            {
                result.type = KeyTypes.VERIFY;
            }
            else if (harness instanceof Teardown)
            {
                result.type = KeyTypes.TEARDOWN;
            }

            return result;
        }

        public static TypeKey build(Class harness)
        {
            TypeKey result = new TypeKey();
            HarnessName harnessName = (HarnessName) harness.getAnnotation(HarnessName.class);
            result.name = harnessName.name();

            if (contains(SetupHarness.class, harness.getInterfaces()))
            {
                result.type = KeyTypes.SETUP;
            }
            else if (contains(RunHarness.class, harness.getInterfaces()))
            {
                result.type = KeyTypes.RUN;
            }
            else if (contains(VerifyHarness.class, harness.getInterfaces()))
            {
                result.type = KeyTypes.VERIFY;
            }
            else if (contains(TeardownHarness.class, harness.getInterfaces()))
            {
                result.type = KeyTypes.TEARDOWN;
            }

            return result;
        }

        enum KeyTypes
        {
            SETUP,
            RUN,
            VERIFY,
            TEARDOWN
        }

        public static boolean contains(Class isEqual, Class[] interfaces)
        {
            boolean result = false;
            for (Class anInterface : interfaces)
            {
                if (anInterface.equals(isEqual))
                {
                    result = true;
                    break;
                }
            }
            return result;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            TypeKey typeKey = (TypeKey) o;

            if (name != null ? !name.equals(typeKey.name) : typeKey.name != null)
            {
                return false;
            }
            if (type != null ? !type.equals(typeKey.type) : typeKey.type != null)
            {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode()
        {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
            return result;
        }
    }
}
