# Introduction #

Just wait for the specified time, usually for waiting for asynchronous processes.

# Configuration #
Configuration values:
  * `wait_time` = time in milliseconds to wait

# Sample Usage #
```
    <Run type="org.mash.harness.wait.TimedWaitRunHarness">
        <Configuration name="wait_time"><Value>30000</Value></Configuration>
    </Run>
```

Here the wait harness sleeps for 30 seconds while other things happen.