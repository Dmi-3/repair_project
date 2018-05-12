package com.ssau.repair.project.repair_project.util;

public class Pair<A, B>
{
    private A key;
    private B value;

    public Pair()
    {
        super();
    }

    public Pair(A key, B value)
    {
        super();
        this.key = key;
        this.value = value;
    }

    public int hashCode()
    {
        int hashKey = key != null ? key.hashCode() : 0;
        int hashValue = value != null ? value.hashCode() : 0;

        return (hashKey + hashValue) * hashValue + hashKey;
    }

    public boolean equals(Object other)
    {
        if (other instanceof Pair)
        {
            Pair otherPair = (Pair) other;
            return
                    ((this.key == otherPair.key ||
                            (this.key != null && otherPair.key != null &&
                                    this.key.equals(otherPair.key))) &&
                            (this.value == otherPair.value ||
                                    (this.value != null && otherPair.value != null &&
                                            this.value.equals(otherPair.value))));
        }

        return false;
    }

    public String toString()
    {
        return "(" + key + ", " + value + ")";
    }

    public A getKey()
    {
        return key;
    }

    public void setKey(A key)
    {
        this.key = key;
    }

    public B getValue()
    {
        return value;
    }

    public void setValue(B value)
    {
        this.value = value;
    }
}
