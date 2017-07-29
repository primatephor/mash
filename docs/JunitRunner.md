# Introduction #

To run as a system test, the harness runner has a JUnit implementation. 
This provides an easy way to inline ant and get the full JUnit reporting functionality.


# Details #

JUnit tests are build using the ScriptDefinition and packaged as a JUnit `TestSuite`. 
The `ErrorFormatter` formats the HarnessError nicely for the junit report mechanism.

To invoke with Ant, here's a sample ant project:
```
<project name="SystemTest" default="system.test" basedir=".">
    <!-- include files in 'lib' directory -->
    <fileset id="lib.jar" dir="lib" includes="*.jar"/>
    <!-- default tag setting-->
    <property name="tags" value=""/>
    <path id="run.classpath">
        <fileset refid="lib.jar"/>
    </path>

    <target name="init">
        <property name="target" value="target"/>
        <property name="report" value="${target}/report"/>
        <property name="log" value="${target}/log"/>
        <mkdir dir="${report}"/>
        <mkdir dir="${log}"/>
    </target>

    <target name="clean" depends="init">
        <delete failonerror="false" dir="${target}"/>
    </target>

    <target name="system.test" description="Performs system tests and builds a report" depends="init">
        <junit printsummary="on"
               haltonfailure="no"
               showoutput="true"
               fork="yes">
            <!-- specify the base suite file -->
            <sysproperty key="system.test.file" value="src/SystemTest.xml"/>
            <sysproperty key="test.tags" value="${tags}"/>
            <sysproperty key="jdbc.url" value="jdbc:jtds:sybase://mydb:1111"/>
            <sysproperty key="log4j.configuration" value="build.log4j.properties"/>
            <classpath>
                <path refid="run.classpath"/>
                <pathelement path="."/>
            </classpath>
            <test name="org.mash.junit.SystemTest" haltonfailure="no" todir="${report}">
                <formatter type="xml"/>
            </test>
        </junit>

        <junitreport todir="${report}">
            <fileset dir="${report}">
                <include name="TEST-*.xml"/>
            </fileset>
            <report format="frames"
                    todir="${report}"/>
        </junitreport>
    </target>
</project>
```

Now any continuous integration software can run this system test, using the `src/SystemTest.xml` 
[SuiteDefinition](SuiteDefinition.md) to invoke all the Scripts as a system test.

To specify which tagged scripts to run, invoke Ant with a comma separated list of tags and the runner will filter based 
on that. 
For instance: `ant -Dtags=user,web` would invoke all tests containing `<Tag>user</Tag>` and `<Tag>web</Tag>`.