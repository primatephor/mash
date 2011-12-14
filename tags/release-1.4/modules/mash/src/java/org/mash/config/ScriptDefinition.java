package org.mash.config;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

/**
 * User: teastlack Date: Jul 2, 2009 Time: 3:35:08 PM
 */
public interface ScriptDefinition
{
    List<String> getTag();

    List<Parameter> getParameter();

    List<Object> getHarnesses();

    String getName();

    String getDir();

    String getFile();

    File getPath();

    BigInteger getOrder();
}
