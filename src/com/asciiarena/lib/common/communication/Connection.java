package com.asciiarena.lib.common.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.asciiarena.lib.common.logging.Log;
import com.asciiarena.lib.common.logging.TermColor;

public class Connection
{
    private Socket socket;

    public Connection(Socket socket)
    {
        this.socket = socket; 
    }

    public boolean send(Object object)
    {
        String remote = TermColor.PURPLE + socket.getRemoteSocketAddress().toString().substring(1) + TermColor.RESET;
        try
        {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(object); 

            String message = TermColor.YELLOW + "[" + object.toString() + "]" + TermColor.RESET;
            Log.info("%s to: %s", message, remote);  

            return true;
        } 
        catch (IOException e)
        {
            Log.error("Connection lost: %s", remote);  
        }
        return false;
    }

    public Object receive()
    {
        String remote = TermColor.PURPLE + socket.getRemoteSocketAddress().toString().substring(1) + TermColor.RESET;
        try
        {
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
            Object object = objectInput.readObject();

            String message = TermColor.BLUE + "[" + object.toString() + "]" + TermColor.RESET;
            Log.info("%s from: %s", message, remote);  

            return object;
        }
        catch (IOException e)
        {
            Log.error("Connection lost: %s", remote);  
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void close()
    {
        try
        {
            this.socket.close();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
