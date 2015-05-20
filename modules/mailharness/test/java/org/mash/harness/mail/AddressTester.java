package org.mash.harness.mail;

import javax.mail.Address;

/**
 * @author
 * @since Jul 26, 2010 3:11:42 PM
 */
public class AddressTester extends Address
{
    String address;

    public AddressTester(String address)
    {
        this.address = address;
    }

    public String getType()
    {
        throw new UnsupportedOperationException("Method getType not yet implemented");
    }

    public String toString()
    {
        return address;
    }

    public boolean equals(Object o)
    {
        throw new UnsupportedOperationException("Method equals not yet implemented");
    }
}
