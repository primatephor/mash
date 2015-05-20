package org.mash.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When implementing a harness, instead of parsing the parameter array you can use this annotation and the mash 
 * framework will set the appropriately named parameter value (MUST BE A STRING)
 *
 * @author
 * @since Sep 17, 2009 10:42:27 AM
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface HarnessParameter
{
    String name();
}
