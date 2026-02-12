package no.hvl.dat110.rpc;

import java.util.HashMap;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.MessageConnection;
import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessagingServer;

public class RPCServer {

	private MessagingServer msgserver;
	private MessageConnection connection;
	
	// hashmap to register RPC methods which are required to extend RPCRemoteImpl
	// the key in the hashmap is the RPC identifier of the method
	private HashMap<Byte,RPCRemoteImpl> services;
	
	public RPCServer(int port) {
		
		this.msgserver = new MessagingServer(port);
		this.services = new HashMap<Byte,RPCRemoteImpl>();
		
	}
	
	public void run() {
		
		// the stop RPC method is built into the server
		RPCRemoteImpl rpcstop = new RPCServerStopImpl(RPCCommon.RPIDSTOP,this);
		
		System.out.println("RPC SERVER RUN - Services: " + services.size());
			
		connection = msgserver.accept(); 
		
		System.out.println("RPC SERVER ACCEPTED");
		
		boolean stop = false;

        while (!stop) {
            try {
                byte rpcid = 0;
                Message requestmsg, replymsg;

                requestmsg = connection.receive();
                byte[] rpcdata = requestmsg.getData();

                rpcid = rpcdata[0]; // IKKE unsigned-cast her

                System.out.println("RPC SERVER received rpcid = " + rpcid);
                System.out.println("Registered RPC IDs: " + services.keySet());

                byte[] param = RPCUtils.decapsulate(rpcdata);
                RPCRemoteImpl impl = services.get(rpcid);

                if (impl == null) {
                    throw new RuntimeException("No rpc service registered for rpcid=" + rpcid);
                }

                byte[] returnval = impl.invoke(param);
                byte[] replydata = RPCUtils.encapsulate(rpcid, returnval);

                replymsg = new Message(replydata);
                connection.send(replymsg);

                if (rpcid == RPCCommon.RPIDSTOP) {
                    stop = true;
                }

            } catch (Exception e) {
                System.out.println("RPC SERVER ERROR:");
                e.printStackTrace();

                // Viktig: stopp serveren pent, ellers kan den stå i rar tilstand
                stop = true;
            }
        }
	
	}
	
	// used by server side method implementations to register themselves in the RPC server
	public void register(byte rpcid, RPCRemoteImpl impl) {
		services.put(rpcid, impl);
	}
	
	public void stop() {

		if (connection != null) {
			connection.close();
		} else {
			System.out.println("RPCServer.stop - connection was null");
		}
		
		if (msgserver != null) {
			msgserver.stop();
		} else {
			System.out.println("RPCServer.stop - msgserver was null");
		}
		
	}
}
