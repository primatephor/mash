package org.mash.loader;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ClassAnnotationMatchProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.config.HarnessDefinition;
import org.mash.config.Run;
import org.mash.config.Setup;
import org.mash.config.Teardown;
import org.mash.config.Verify;
import org.mash.harness.Harness;
import org.mash.harness.HarnessContext;
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
 * @since Jul 4, 2009
 */
public class HarnessBuilder
{
    private static final Logger log = LogManager.getLogger(HarnessBuilder.class.getName());
    private static HarnessBuilder instance;
    private Map<TypeKey, Class> types;

    HarnessBuilder()
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

    private Harness createInstance(HarnessDefinition harnessDefinition) throws HarnessException
    {
        Harness result;


        TypeKey key = TypeKey.build(harnessDefinition.getType(), harnessDefinition);
        Class clazz = getTypes().get(key);
        if (clazz == null)
        {
            try
            {
                clazz = Class.forName(harnessDefinition.getType());
            }
            catch (ClassNotFoundException e)
            {
                throw new HarnessException("Unable to create class " + harnessDefinition.getType(), e);
            }
        }

        log.debug("Building harness " + clazz.getName());
        try
        {
            result = (Harness) clazz.newInstance();
        }
        catch (Exception e)
        {
            throw new HarnessException("Problem creating class " + harnessDefinition.getType(), e);
        }
        return result;
    }

    Map<TypeKey, Class> getTypes() throws HarnessException
    {
        //load up the types
        if (types == null || types.size() == 0)
        {
            log.info("matching up classes to types listed on harnesses");
            FastClasspathScanner scanner = new FastClasspathScanner();
            NamedClassDiscover namedClassDiscover = new NamedClassDiscover();
            scanner.matchClassesWithAnnotation(HarnessName.class, namedClassDiscover);
            scanner.scan();
            types = buildTypes(namedClassDiscover.classes);
        }
        return types;
    }

    private Map<TypeKey, Class> buildTypes(List<String> classes) throws HarnessException
    {
        Map<TypeKey, Class> result = new HashMap<>();
        for (String className : classes)
        {
            Class harness = null;
            try
            {
                harness = Class.forName(className);
            }
            catch (NoClassDefFoundError e)
            {
                log.debug(e);
                log.info("Not adding " + className + " as a library was not found (you may not be using it).  " +
                                 "Use debug to see full stack");
            }
            catch (Exception e)
            {
                throw new HarnessException("Problem finding class " + className, e);
            }

            if (harness != null)
            {
                TypeKey key = TypeKey.build(harness);
                if (result.get(key) != null)
                {
                    throw new HarnessException(
                            "Harness with name already exists! Name:" + key.name + " type: " + key.type.name());
                }
                log.info("Adding " + key + " for harness " + harness.getName());
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

        public void run(HarnessContext context)
        {
            ((RunHarness) getWrap()).run(context);
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

    private class NamedClassDiscover implements ClassAnnotationMatchProcessor
    {
        List<String> classes = new ArrayList<>();

        @Override
        public void processMatch(Class<?> aClass) {
            log.info("Found harness named class " + aClass);
            classes.add(aClass.getName());
        }
    }

    public static class TypeKey
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
            else
            {
                log.warn("Unknown harness definition (unable to determin if setup, run, verify, or teardown:" +
                                 harness.getClass().getName());
            }

            return result;
        }

        public static TypeKey build(Class harness)
        {
            TypeKey result = new TypeKey();
            HarnessName harnessName = (HarnessName) harness.getAnnotation(HarnessName.class);
            result.name = harnessName.name();

            if (SetupHarness.class.isAssignableFrom(harness))
            {
                result.type = KeyTypes.SETUP;
            }
            else if (RunHarness.class.isAssignableFrom(harness))
            {
                result.type = KeyTypes.RUN;
            }
            else if (VerifyHarness.class.isAssignableFrom(harness))
            {
                result.type = KeyTypes.VERIFY;
            }
            else if (TeardownHarness.class.isAssignableFrom(harness))
            {
                result.type = KeyTypes.TEARDOWN;
            }
            else
            {
                log.warn("Unknown harness class (unable to determin if setup, run, verify, or teardown:" +
                                 harness.getName());
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

        @Override
        public String toString()
        {
            String typeStr = "";
            if (type != null)
            {
                typeStr = type.name();
            }
            return name + "=>" + typeStr;
        }
    }
}
