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

public class LampState extends com.zeroc.Ice.Value
{
    public LampState()
    {
        this.brightness = Brightness.low;
        this.color = new Color();
    }

    public LampState(boolean isOn)
    {
        this.isOn = isOn;
        this.brightness = Brightness.low;
        this.color = new Color();
    }

    public LampState(boolean isOn, Brightness brightness, Color color, boolean autoBrightness, boolean isDancing)
    {
        this.isOn = isOn;
        setBrightness(brightness);
        setColor(color);
        setAutoBrightness(autoBrightness);
        setIsDancing(isDancing);
    }

    public boolean isOn;

    private Brightness brightness;
    private boolean _brightness;

    public Brightness getBrightness()
    {
        if(!_brightness)
        {
            throw new java.util.NoSuchElementException("brightness is not set");
        }
        return brightness;
    }

    public void setBrightness(Brightness brightness)
    {
        _brightness = true;
        this.brightness = brightness;
    }

    public boolean hasBrightness()
    {
        return _brightness;
    }

    public void clearBrightness()
    {
        _brightness = false;
    }

    public void optionalBrightness(java.util.Optional<Brightness> v)
    {
        if(v == null || !v.isPresent())
        {
            _brightness = false;
        }
        else
        {
            _brightness = true;
            brightness = v.get();
        }
    }

    public java.util.Optional<Brightness> optionalBrightness()
    {
        if(_brightness)
        {
            return java.util.Optional.of(brightness);
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private Color color;
    private boolean _color;

    public Color getColor()
    {
        if(!_color)
        {
            throw new java.util.NoSuchElementException("color is not set");
        }
        return color;
    }

    public void setColor(Color color)
    {
        _color = true;
        this.color = color;
    }

    public boolean hasColor()
    {
        return _color;
    }

    public void clearColor()
    {
        _color = false;
    }

    public void optionalColor(java.util.Optional<Color> v)
    {
        if(v == null || !v.isPresent())
        {
            _color = false;
        }
        else
        {
            _color = true;
            color = v.get();
        }
    }

    public java.util.Optional<Color> optionalColor()
    {
        if(_color)
        {
            return java.util.Optional.of(color);
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private boolean autoBrightness;
    private boolean _autoBrightness;

    public boolean getAutoBrightness()
    {
        if(!_autoBrightness)
        {
            throw new java.util.NoSuchElementException("autoBrightness is not set");
        }
        return autoBrightness;
    }

    public void setAutoBrightness(boolean autoBrightness)
    {
        _autoBrightness = true;
        this.autoBrightness = autoBrightness;
    }

    public boolean hasAutoBrightness()
    {
        return _autoBrightness;
    }

    public void clearAutoBrightness()
    {
        _autoBrightness = false;
    }

    public void optionalAutoBrightness(java.util.Optional<java.lang.Boolean> v)
    {
        if(v == null || !v.isPresent())
        {
            _autoBrightness = false;
        }
        else
        {
            _autoBrightness = true;
            autoBrightness = v.get();
        }
    }

    public java.util.Optional<java.lang.Boolean> optionalAutoBrightness()
    {
        if(_autoBrightness)
        {
            return java.util.Optional.of(autoBrightness);
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    public boolean isAutoBrightness()
    {
        if(!_autoBrightness)
        {
            throw new java.util.NoSuchElementException("autoBrightness is not set");
        }
        return autoBrightness;
    }

    private boolean isDancing;
    private boolean _isDancing;

    public boolean getIsDancing()
    {
        if(!_isDancing)
        {
            throw new java.util.NoSuchElementException("isDancing is not set");
        }
        return isDancing;
    }

    public void setIsDancing(boolean isDancing)
    {
        _isDancing = true;
        this.isDancing = isDancing;
    }

    public boolean hasIsDancing()
    {
        return _isDancing;
    }

    public void clearIsDancing()
    {
        _isDancing = false;
    }

    public void optionalIsDancing(java.util.Optional<java.lang.Boolean> v)
    {
        if(v == null || !v.isPresent())
        {
            _isDancing = false;
        }
        else
        {
            _isDancing = true;
            isDancing = v.get();
        }
    }

    public java.util.Optional<java.lang.Boolean> optionalIsDancing()
    {
        if(_isDancing)
        {
            return java.util.Optional.of(isDancing);
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    public boolean isIsDancing()
    {
        if(!_isDancing)
        {
            throw new java.util.NoSuchElementException("isDancing is not set");
        }
        return isDancing;
    }

    public LampState clone()
    {
        return (LampState)super.clone();
    }

    public static String ice_staticId()
    {
        return "::SmartHome::LampState";
    }

    @Override
    public String ice_id()
    {
        return ice_staticId();
    }

    /** @hidden */
    public static final long serialVersionUID = -670316388L;

    /** @hidden */
    @Override
    protected void _iceWriteImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice(ice_staticId(), -1, true);
        ostr_.writeBool(isOn);
        if(_brightness)
        {
            Brightness.ice_write(ostr_, 1, brightness);
        }
        if(_color)
        {
            Color.ice_write(ostr_, 2, color);
        }
        if(_autoBrightness)
        {
            ostr_.writeBool(3, autoBrightness);
        }
        if(_isDancing)
        {
            ostr_.writeBool(4, isDancing);
        }
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _iceReadImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        isOn = istr_.readBool();
        if(_brightness = istr_.readOptional(1, com.zeroc.Ice.OptionalFormat.Size))
        {
            brightness = Brightness.ice_read(istr_);
        }
        if(_color = istr_.readOptional(2, com.zeroc.Ice.OptionalFormat.VSize))
        {
            istr_.skipSize();
            color = Color.ice_read(istr_);
        }
        if(_autoBrightness = istr_.readOptional(3, com.zeroc.Ice.OptionalFormat.F1))
        {
            autoBrightness = istr_.readBool();
        }
        if(_isDancing = istr_.readOptional(4, com.zeroc.Ice.OptionalFormat.F1))
        {
            isDancing = istr_.readBool();
        }
        istr_.endSlice();
    }
}
