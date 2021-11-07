package net.md_5.bungee.util;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.HashSet;

public class BoundedHashSet<E> extends HashSet<E>
{
    private final int maxSize;

    public BoundedHashSet(int maxSize)
    {
        this.maxSize = maxSize;
    }

    private void checkSize(int increment)
    {
        Preconditions.checkState( size() + increment <= maxSize, "Adding %s elements would exceed capacity of %s", increment, maxSize );
    }

    @Override
    public boolean add(E e)
    {
        checkSize( 1 );
        return super.add( e );
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        checkSize( c.size() );
        return super.addAll( c );
    }
}
