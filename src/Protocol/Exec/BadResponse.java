package Protocol.Exec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Shared.ClientServer.ExecError;

public class BadResponse extends Response
{
    public ExecError error;                  //obtem se uma mensagem por uma classe que traduz a chave do enum numa string de erro
    
    public BadResponse (ExecError error)
    {
        super(false);
        this.error= error;
    }

    @Override
    public void serialize (DataOutputStream out) throws IOException
    {
        super.serialize(out);
    }

    public static BadResponse deserialize (DataInputStream in, Response packet)
    {
        //@TODO
        return new BadResponse(null);
    }
}
