# Introduction #

The harness error really is a holder for error information, to be used by run implementations to report what's happened.


# Details #
An error is pretty simple, containing the name of the harness that had the error, the value of the error, and a description.

This is really for use by the run implementations.  For instance, junit runner would use these to throw assertion failures, with the data contained in them as the error.  This would bubble up to the reporter and you get a nice(ish) ui.