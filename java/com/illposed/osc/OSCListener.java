package com.illposed.osc;

import java.util.Date;

public abstract interface OSCListener
{
  public abstract void acceptMessage(Date paramDate, OSCMessage paramOSCMessage);
}

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     com.illposed.osc.OSCListener
 * JD-Core Version:    0.6.2
 */