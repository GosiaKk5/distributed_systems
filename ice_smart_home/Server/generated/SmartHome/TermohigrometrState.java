//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.10
//
// <auto-generated>
//
// Generated from file `smart_home.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package SmartHome;

public class TermohigrometrState implements java.lang.Cloneable,
                                            java.io.Serializable
{
    public double temperature;

    public double humidity;

    public TermohigrometrState()
    {
    }

    public TermohigrometrState(double temperature, double humidity)
    {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        TermohigrometrState r = null;
        if(rhs instanceof TermohigrometrState)
        {
            r = (TermohigrometrState)rhs;
        }

        if(r != null)
        {
            if(this.temperature != r.temperature)
            {
                return false;
            }
            if(this.humidity != r.humidity)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::SmartHome::TermohigrometrState");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, temperature);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, humidity);
        return h_;
    }

    public TermohigrometrState clone()
    {
        TermohigrometrState c = null;
        try
        {
            c = (TermohigrometrState)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeDouble(this.temperature);
        ostr.writeDouble(this.humidity);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.temperature = istr.readDouble();
        this.humidity = istr.readDouble();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, TermohigrometrState v)
    {
        if(v == null)
        {
            _nullMarshalValue.ice_writeMembers(ostr);
        }
        else
        {
            v.ice_writeMembers(ostr);
        }
    }

    static public TermohigrometrState ice_read(com.zeroc.Ice.InputStream istr)
    {
        TermohigrometrState v = new TermohigrometrState();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<TermohigrometrState> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, TermohigrometrState v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.VSize))
        {
            ostr.writeSize(16);
            ice_write(ostr, v);
        }
    }

    static public java.util.Optional<TermohigrometrState> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.VSize))
        {
            istr.skipSize();
            return java.util.Optional.of(TermohigrometrState.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final TermohigrometrState _nullMarshalValue = new TermohigrometrState();

    /** @hidden */
    public static final long serialVersionUID = -1914145173L;
}
