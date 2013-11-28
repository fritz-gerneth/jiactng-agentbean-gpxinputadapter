jiactng-agentbean-gpxinputadapter
=================================
This JIAC V agent bean accepts incoming connections to read GPX information and saves them into the memory.

As Null is evil, all arguments are mandantory, not nullable and all methods will never return null. Consider an implicit @NotNull annotation on every parameter and method.

This artifact is available in my developer repository:
```xml
<repository>
    <id>de.effms.dev.repository</id>
    <name>Developer Repository</name>
    <url>https://github.com/fritz-gerneth/dev-maven-repo/raw/master/</url>
</repository>
```

A sample configuration could be
```xml
<!-- Simple factories to avoid creation in beans -->
<bean class="de.effms.jiactng.agentbean.gpxinputadapter.GpsServerSocketFactory">
    <constructor-arg value="14500" />
</bean>
<bean class="de.effms.jiactng.agentbean.gpxinputadapter.UnmarshallerFactory">
    <constructor-arg value="de.effms.jiactng.facts.gpx" />
</bean>

<!-- The thread pool for this agent-bean. Could use a dedicated pool for every step as well -->
<bean name="de.effms.gpsadapter.threadpool" class="java.util.concurrent.Executors" factory-method="newCachedThreadPool" />

<!-- This Bean listens and accepts new connections -->
<bean class="de.effms.jiactng.agentbean.gpxinputadapter.IncomingConnectionListener">
    <!-- The Socket the AgentBean uses to listen for new connections -->
    <constructor-arg>
        <bean factory-bean="de.effms.jiactng.agentbean.gpxinputadapter.GpsServerSocketFactory"
              factory-method="getInstance" />
    </constructor-arg>
    <constructor-arg ref="de.effms.jiactng.agentbean.gpxinputadapter.ContentReaderExecutor" />
</bean>

<!-- Reads all content from the socket -->
<bean class="de.effms.jiactng.agentbean.gpxinputadapter.ContentReaderExecutor">
    <constructor-arg ref="de.effms.gpsadapter.threadpool" />
    <constructor-arg ref="de.effms.jiactng.agentbean.gpxinputadapter.InputToMemoryWriterExecutor" />
</bean>

<!-- Parses the received content and writes it to the memory -->
<bean class="de.effms.jiactng.agentbean.gpxinputadapter.InputToMemoryWriterExecutor">
    <constructor-arg ref="de.effms.gpsadapter.threadpool" />
    <!-- Requires a reference to the agent's memory, see agent-definition below -->
    <constructor-arg ref="gpsAdapterAgentMemory" />
    <constructor-arg>
        <bean factory-bean="de.effms.jiactng.agentbean.gpxinputadapter.UnmarshallerFactory"
              factory-method="getInstance" />
    </constructor-arg>
</bean>
```

Usage for an exemplary agent would be:
```xml
<!-- The actual AgentBean -->
<bean class="de.effms.jiactng.agentbean.gpxinputadapter.GpxInputAdapterAgentBean">
    <constructor-arg ref="de.effms.gpsadapter.threadpool" />
    <constructor-arg ref="de.effms.jiactng.agentbean.gpxinputadapter.IncomingConnectionListener" />
</bean>

<!-- GPS Agent -->
<bean name="gpsAdapterAgentMemory" class="de.dailab.jiactng.agentcore.knowledge.Memory" />
<bean name="HelloWorldAgent" parent="RSBaseAgent" scope="prototype">
    <property name="memory" ref="gpsAdapterAgentMemory" />
    <property name="agentBeans">
        <list>
            <ref bean="HelloWorldBeanA" />
            <ref bean="de.effms.jiactng.agentbean.gpxinputadapter.GpxInputAdapterAgentBean" />
        </list>
    </property>
</bean>
```
