package Protocol.Exec;

import Shared.ClientServer.ExecError;

public class BadResponse extends Response
{
    public ExecError error;                  //obtem se uma mensagem por uma classe que traduz a chave do enum numa string de erro
    
    public BadResponse (ExecError error)
    {
        super(false);
        this.error= error;
    }
}
