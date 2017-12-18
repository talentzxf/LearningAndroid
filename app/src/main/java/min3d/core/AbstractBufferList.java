package min3d.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by VincentZhang on 12/17/2017.
 */

public abstract class AbstractBufferList {
    protected FloatBuffer _b;
    protected int _numElements = 0;
    public FloatBuffer buffer()
    {
        return _b;
    }

    public abstract int getPropertiesPerElement();
    public abstract int getBytesPerProperty();

    public int size(){
        return _numElements;
    }

}
