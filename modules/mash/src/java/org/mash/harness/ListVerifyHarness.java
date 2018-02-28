package org.mash.harness;

import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessName;

import java.util.List;

/**
 * @author
 * @since Jun 9, 2010 4:43:07 PM
 */
@HarnessName(name = "list")
public class ListVerifyHarness extends StandardVerifyHarness
{
    private int elementNumber = 0;
    private int expectedSize = -1;

    public void verify(RunHarness run, List<SetupHarness> setup)
    {
        if (run.getResponse() != null && run.getResponse() instanceof ListRunResponse)
        {
            ListRunResponse listRunResponse = (ListRunResponse) run.getResponse();
            listRunResponse.setElementNumber(elementNumber);

            if (expectedSize >= 0)
            {
                if (expectedSize != listRunResponse.getSize())
                {
                    getErrors().add(new HarnessError(this, "Verify", "Actual response size:" + listRunResponse.getSize() +
                                                                     " doesn't equal expected size:" + expectedSize));
                }
            }
        }

        //is there something to verify?
        if(expectedSize != 0)
        {
            super.verify(run, setup);
        }
    }

    public int getElementNumber()
    {
        return elementNumber;
    }

    @HarnessConfiguration(name = "element_number")
    public void setElementNumber(String elementNumber)
    {
        this.elementNumber = Integer.valueOf(elementNumber);
    }

    @HarnessConfiguration(name = "expected_size")
    public void setExpectedSize(String expectedSize)
    {
        this.expectedSize = Integer.valueOf(expectedSize);
    }
}
